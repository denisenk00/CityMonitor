package com.denysenko.citymonitorweb.models.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "POLYGONS")
@Data
public class Polygon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "polygon_id")
    private Long polygonId;
    @Column(name = "name")
    private String name;
    @Column(name = "polygon")
    private com.vividsolutions.jts.geom.Polygon polygon;
    @Column(name = "layout_id")
    private Long layoutId;
}
