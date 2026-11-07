package com.fairoptima.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "booth_types")
public class BoothType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "price_multiplier", nullable = false, precision = 5, scale = 2)
    private BigDecimal priceMultiplier;

    @Column(name = "default_power_kw", nullable = false)
    private Double defaultPowerKw;

    @Column(name = "default_network_ports", nullable = false)
    private Integer defaultNetworkPorts;

    @OneToMany(mappedBy = "boothType", fetch = FetchType.LAZY)
    private List<Booth> booths = new ArrayList<>();

    public BoothType() {}

    public BoothType(String name, BigDecimal priceMultiplier, Double defaultPowerKw, Integer defaultNetworkPorts) {
        this.name = name;
        this.priceMultiplier = priceMultiplier;
        this.defaultPowerKw = defaultPowerKw;
        this.defaultNetworkPorts = defaultNetworkPorts;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPriceMultiplier() { return priceMultiplier; }
    public void setPriceMultiplier(BigDecimal priceMultiplier) { this.priceMultiplier = priceMultiplier; }
    public Double getDefaultPowerKw() { return defaultPowerKw; }
    public void setDefaultPowerKw(Double defaultPowerKw) { this.defaultPowerKw = defaultPowerKw; }
    public Integer getDefaultNetworkPorts() { return defaultNetworkPorts; }
    public void setDefaultNetworkPorts(Integer defaultNetworkPorts) { this.defaultNetworkPorts = defaultNetworkPorts; }
    public List<Booth> getBooths() { return booths; }
    public void setBooths(List<Booth> booths) { this.booths = booths; }
}