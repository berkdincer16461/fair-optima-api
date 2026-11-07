package com.fairoptima.entity;

import com.fairoptima.entity.enums.BoothStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "booths")
public class Booth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "booth_code", nullable = false, unique = true, length = 50)
    private String boothCode;

    @Column(name = "area_square_meters", nullable = false)
    private Double areaSquareMeters;

    @Column(name = "base_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "required_power_kw", nullable = false)
    private Double requiredPowerKw;

    @Column(name = "network_ports", nullable = false)
    private Integer networkPorts;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private BoothStatus status = BoothStatus.AVAILABLE;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booth_type_id", nullable = false)
    private BoothType boothType;

    public Booth() {}

    public Booth(String boothCode, Double areaSquareMeters, BigDecimal basePrice, Double requiredPowerKw, Integer networkPorts, BoothStatus status, Hall hall, BoothType boothType) {
        this.boothCode = boothCode;
        this.areaSquareMeters = areaSquareMeters;
        this.basePrice = basePrice;
        this.requiredPowerKw = requiredPowerKw;
        this.networkPorts = networkPorts;
        this.status = status;
        this.hall = hall;
        this.boothType = boothType;
    }

    public Long getId() { return id; }
    public String getBoothCode() { return boothCode; }
    public void setBoothCode(String boothCode) { this.boothCode = boothCode; }
    public Double getAreaSquareMeters() { return areaSquareMeters; }
    public void setAreaSquareMeters(Double areaSquareMeters) { this.areaSquareMeters = areaSquareMeters; }
    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }
    public Double getRequiredPowerKw() { return requiredPowerKw; }
    public void setRequiredPowerKw(Double requiredPowerKw) { this.requiredPowerKw = requiredPowerKw; }
    public Integer getNetworkPorts() { return networkPorts; }
    public void setNetworkPorts(Integer networkPorts) { this.networkPorts = networkPorts; }
    public BoothStatus getStatus() { return status; }
    public void setStatus(BoothStatus status) { this.status = status; }
    public Hall getHall() { return hall; }
    public void setHall(Hall hall) { this.hall = hall; }
    public BoothType getBoothType() { return boothType; }
    public void setBoothType(BoothType boothType) { this.boothType = boothType; }
}