package com.denysenko.citymonitorweb.models.entities;

import com.denysenko.citymonitorweb.enums.AppealStatus;
import com.vividsolutions.jts.geom.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "APPEALS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appeal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appeal_id")
    private Long id;
    @JoinColumn(name = "local_id")
    @OneToOne
    private Local local;
    @Column(name = "text")
    private String text;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AppealStatus status;
    @Column(name = "post_date")
    private LocalDateTime postDate;
    @Column(name = "point_id")
    private Point locationPoint;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "appeal_id", nullable = false)
    private List<File> files = new LinkedList<>();

}

