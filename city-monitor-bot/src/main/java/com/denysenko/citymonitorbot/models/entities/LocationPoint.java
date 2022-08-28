package com.denysenko.citymonitorbot.models.entities;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "POINTS")
public class LocationPoint {
    @Id
    @GeneratedValue
    @Column(name = "point_id")
    private Long id;
    @Column(name = "latitude", precision = 11, scale = 8)
    private BigDecimal latitude;
    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;

    public LocationPoint(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationPoint() {
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
