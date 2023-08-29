package com.denysenko.citymonitorweb.models.entities;

import com.denysenko.citymonitorweb.enums.LayoutStatus;
import lombok.*;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "LAYOUTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Layout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "layout_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private LayoutStatus status;

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "layout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Polygon> polygons = new LinkedList<>();

    @PrePersist
    void onCreate(){
        status = LayoutStatus.AVAILABLE;
    }

    public void addPolygon(Polygon polygon){
        polygon.setLayout(this);
        polygons.add(polygon);
    }

}
