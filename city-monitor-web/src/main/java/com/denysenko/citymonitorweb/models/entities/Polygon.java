package com.denysenko.citymonitorweb.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "POLYGONS")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
