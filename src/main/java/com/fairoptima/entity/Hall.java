package com.fairoptima.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "halls")
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "area_square_meters", nullable = false)
    private Double areaSquareMeters;

    @Column(name = "max_power_kw", nullable = false)
    private Double maxPowerKw;

    @Column(name = "max_network_ports", nullable = false)
    private Integer maxNetworkPorts;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fair_id", nullable = false)
    private Fair fair;

    @OneToMany(mappedBy = "hall", fetch = FetchType.LAZY)
    private List<Booth> booths = new ArrayList<>();

    public Hall() {}

    public Hall(String name, Double areaSquareMeters, Double maxPowerKw, Integer maxNetworkPorts, Fair fair) {
        this.name = name;
        this.areaSquareMeters = areaSquareMeters;
        this.maxPowerKw = maxPowerKw;
        this.maxNetworkPorts = maxNetworkPorts;
        this.fair = fair;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getAreaSquareMeters() { return areaSquareMeters; }
    public void setAreaSquareMeters(Double areaSquareMeters) { this.areaSquareMeters = areaSquareMeters; }
    public Double getMaxPowerKw() { return maxPowerKw; }
    public void setMaxPowerKw(Double maxPowerKw) { this.maxPowerKw = maxPowerKw; }
    public Integer getMaxNetworkPorts() { return maxNetworkPorts; }
    public void setMaxNetworkPorts(Integer maxNetworkPorts) { this.maxNetworkPorts = maxNetworkPorts; }
    public Fair getFair() { return fair; }
    public void setFair(Fair fair) { this.fair = fair; }
    public List<Booth> getBooths() { return booths; }
    public void setBooths(List<Booth> booths) { this.booths = booths; }
}