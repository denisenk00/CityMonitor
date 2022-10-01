package com.denysenko.citymonitorweb.models.entities;

import com.denysenko.citymonitorweb.enums.LayoutStatus;
import lombok.Data;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "LAYOUTS")
@Data
public class Layout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "layout_id")
    private Long layoutId;
    @Column(name = "name")
    private String name;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private LayoutStatus status;
    @OneToMany
    @JoinColumn(name = "layout_id")
    private List<Polygon> polygons = new LinkedList<>();

    @PrePersist
    void onCreate(){
        status = LayoutStatus.AVAILABLE;
    }


}