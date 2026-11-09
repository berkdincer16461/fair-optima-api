package com.fairoptima.service.impl;

import com.fairoptima.dto.ReservationPatchRequest;
import com.fairoptima.dto.ReservationRequest;
import com.fairoptima.dto.ReservationResponse;
import com.fairoptima.entity.Booth;
import com.fairoptima.entity.Company;
import com.fairoptima.entity.Fair;
import com.fairoptima.entity.Hall;
import com.fairoptima.entity.Reservation;
import com.fairoptima.entity.enums.BoothStatus;
import com.fairoptima.entity.enums.FairStatus;
import com.fairoptima.entity.enums.ReservationStatus;
import com.fairoptima.exception.BadRequestException;
import com.fairoptima.exception.ResourceNotFoundException;
import com.fairoptima.repository.BoothRepository;
import com.fairoptima.repository.CompanyRepository;
import com.fairoptima.repository.ReservationRepository;
import com.fairoptima.service.ReservationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final BoothRepository boothRepository;
    private final CompanyRepository companyRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  BoothRepository boothRepository,
                                  CompanyRepository companyRepository) {
        this.reservationRepository = reservationRepository;
        this.boothRepository = boothRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> getAll() {
        return reservationRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponse getById(Long id) {
        Reservation r = findReservationOrThrow(id);
        return mapToResponse(r);
    }

    @Override
    @Transactional
    public ReservationResponse create(ReservationRequest request) {
        Booth booth = boothRepository.findById(request.boothId())
                .orElseThrow(() -> new ResourceNotFoundException("Stant bulunamadı: " + request.boothId()));
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Şirket bulunamadı: " + request.companyId()));

        validateBusinessRules(booth, request.startDate(), request.endDate(), request.requestedPowerKw(), request.requestedNetworkPorts(), null);

        Reservation r = new Reservation();
        r.setBooth(booth);
        r.setCompany(company);
        r.setStartDate(request.startDate());
        r.setEndDate(request.endDate());
        r.setRequestedPowerKw(request.requestedPowerKw());
        r.setRequestedNetworkPorts(request.requestedNetworkPorts());
        r.setStatus(ReservationStatus.PENDING);

        return mapToResponse(reservationRepository.save(r));
    }

    @Override
    @Transactional
    public ReservationResponse update(Long id, ReservationRequest request) {
        Reservation r = findReservationOrThrow(id);
        
        if (r.getStatus() != ReservationStatus.PENDING) {
            throw new BadRequestException("Sadece PENDING durumundaki rezervasyonlar güncellenebilir.");
        }

        Booth booth = boothRepository.findById(request.boothId())
                .orElseThrow(() -> new ResourceNotFoundException("Stant bulunamadı: " + request.boothId()));
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Şirket bulunamadı: " + request.companyId()));

        validateBusinessRules(booth, request.startDate(), request.endDate(), request.requestedPowerKw(), request.requestedNetworkPorts(), id);

        r.setBooth(booth);
        r.setCompany(company);
        r.setStartDate(request.startDate());
        r.setEndDate(request.endDate());
        r.setRequestedPowerKw(request.requestedPowerKw());
        r.setRequestedNetworkPorts(request.requestedNetworkPorts());

        return mapToResponse(reservationRepository.update(r));
    }

    @Override
    @Transactional
    public ReservationResponse patch(Long id, ReservationPatchRequest request) {
        Reservation r = findReservationOrThrow(id);
        
        if (r.getStatus() != ReservationStatus.PENDING) {
            throw new BadRequestException("Sadece PENDING durumundaki rezervasyonlar güncellenebilir.");
        }

        LocalDate newStart = request.startDate() != null ? request.startDate() : r.getStartDate();
        LocalDate newEnd = request.endDate() != null ? request.endDate() : r.getEndDate();
        Double newPower = request.requestedPowerKw() != null ? request.requestedPowerKw() : r.getRequestedPowerKw();
        Integer newPorts = request.requestedNetworkPorts() != null ? request.requestedNetworkPorts() : r.getRequestedNetworkPorts();

        validateBusinessRules(r.getBooth(), newStart, newEnd, newPower, newPorts, id);

        r.setStartDate(newStart);
        r.setEndDate(newEnd);
        r.setRequestedPowerKw(newPower);
        r.setRequestedNetworkPorts(newPorts);

        return mapToResponse(reservationRepository.update(r));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Reservation r = findReservationOrThrow(id);
        if (r.getStatus() == ReservationStatus.APPROVED) {
            throw new BadRequestException("Onaylanmış rezervasyon doğrudan silinemez, önce iptal edilmelidir.");
        }
        reservationRepository.delete(r);
    }

    @Override
    @Transactional
    public ReservationResponse approve(Long id) {
        Reservation r = findReservationOrThrow(id);
        if (r.getStatus() != ReservationStatus.PENDING) {
            throw new BadRequestException("Sadece PENDING durumundaki rezervasyon onaylanabilir.");
        }
        
        r.setStatus(ReservationStatus.APPROVED);
        reservationRepository.update(r);

        Booth booth = r.getBooth();
        booth.setStatus(BoothStatus.RESERVED);
        boothRepository.update(booth);

        return mapToResponse(r);
    }

    @Override
    @Transactional
    public ReservationResponse reject(Long id) {
        Reservation r = findReservationOrThrow(id);
        if (r.getStatus() != ReservationStatus.PENDING) {
            throw new BadRequestException("Only PENDING reservations can be rejected.");
        }
        r.setStatus(ReservationStatus.REJECTED);
        reservationRepository.update(r);
        return mapToResponse(r);
    }

    @Override
    @Transactional
    public ReservationResponse cancel(Long id) {
        Reservation r = findReservationOrThrow(id);
        ReservationStatus previous = r.getStatus();
        if (previous != ReservationStatus.PENDING && previous != ReservationStatus.APPROVED) {
            throw new BadRequestException("Only PENDING or APPROVED reservations can be cancelled.");
        }
        r.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.update(r);

        if (previous == ReservationStatus.APPROVED) {
            Booth booth = r.getBooth();
            if (booth.getStatus() == BoothStatus.RESERVED) {
                booth.setStatus(BoothStatus.AVAILABLE);
                boothRepository.update(booth);
            }
        }
        return mapToResponse(r);
    }

    private Reservation findReservationOrThrow(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rezervasyon bulunamadı: " + id));
    }

    private void validateBusinessRules(Booth booth, LocalDate start, LocalDate end, Double powerKw, Integer networkPorts, Long excludeId) {
        if (end.isBefore(start)) {
            throw new BadRequestException("Bitiş tarihi başlangıç tarihinden önce olamaz.");
        }
        if (booth.getStatus() == BoothStatus.OUT_OF_SERVICE) {
            throw new BadRequestException("Stant kullanım dışı (OUT_OF_SERVICE).");
        }
        
        Fair fair = booth.getHall().getFair();
        if (fair.getStatus() != FairStatus.ACTIVE) {
            throw new BadRequestException("Fuar aktif değil. Sadece aktif fuarlara rezervasyon yapılabilir.");
        }
        if (start.isBefore(fair.getStartDate()) || end.isAfter(fair.getEndDate())) {
            throw new BadRequestException("Rezervasyon tarihleri fuar tarihleri dışında.");
        }

        List<ReservationStatus> activeStatuses = List.of(ReservationStatus.PENDING, ReservationStatus.APPROVED);
        long conflicts = reservationRepository.countConflictingReservations(
                booth.getId(), activeStatuses, start, end, excludeId);
        if (conflicts > 0) {
            throw new BadRequestException("Bu stant için belirtilen tarihlerde çakışan rezervasyon mevcut.");
        }

        Hall hall = booth.getHall();
        Double currentPower = reservationRepository.sumRequestedPowerByHallId(
                hall.getId(), activeStatuses, start, end, excludeId);
        if (currentPower + powerKw > hall.getMaxPowerKw()) {
            throw new BadRequestException("Salonun elektrik kapasitesi yetersiz. Aşım riski.");
        }

        Integer currentPorts = reservationRepository.sumRequestedNetworkPortsByHallId(
                hall.getId(), activeStatuses, start, end, excludeId);
        if (currentPorts + networkPorts > hall.getMaxNetworkPorts()) {
            throw new BadRequestException("Salonun ağ portu kapasitesi yetersiz.");
        }
    }

    private ReservationResponse mapToResponse(Reservation r) {
        return new ReservationResponse(
                r.getId(),
                r.getBooth().getId(),
                r.getBooth().getBoothCode(),
                r.getCompany().getId(),
                r.getCompany().getName(),
                r.getStartDate(),
                r.getEndDate(),
                r.getStatus(),
                r.getRequestedPowerKw(),
                r.getRequestedNetworkPorts(),
                r.getCreatedAt()
        );
    }
}