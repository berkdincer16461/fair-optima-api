package com.fairoptima.entity;

import com.fairoptima.entity.enums.FairStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fairs")
public class Fair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "theme", length = 200)
    private String theme;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "location", nullable = false, length = 200)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private FairStatus status = FairStatus.DRAFT;

    @OneToMany(mappedBy = "fair", fetch = FetchType.LAZY)
    private List<Hall> halls = new ArrayList<>();

    public Fair() {}

    public Fair(String name, String theme, LocalDate startDate, LocalDate endDate, String location, FairStatus status) {
        this.name = name;
        this.theme = theme;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.status = status;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public FairStatus getStatus() { return status; }
    public void setStatus(FairStatus status) { this.status = status; }
    public List<Hall> getHalls() { return halls; }
    public void setHalls(List<Hall> halls) { this.halls = halls; }
}