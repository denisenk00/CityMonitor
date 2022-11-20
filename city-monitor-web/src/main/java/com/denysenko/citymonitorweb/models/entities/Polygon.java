package com.denysenko.citymonitorweb.models.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "POLYGONS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Polygon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "polygon_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "polygon")
    private org.locationtech.jts.geom.Polygon polygon;

    @ManyToOne(optional = false)
    @JoinColumn(name = "layout_id")
    private Layout layout;

}

