package com.denysenko.citymonitorweb.models.entities;

import com.denysenko.citymonitorweb.enums.LayoutStatus;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @OneToMany(mappedBy = "layout", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Polygon> polygons = new LinkedList<>();

    @PrePersist
    void onCreate(){
        status = LayoutStatus.AVAILABLE;
    }

}
