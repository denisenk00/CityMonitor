package com.denysenko.citymonitorweb.models.entities;

import com.denysenko.citymonitorweb.enums.AppealStatus;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "APPEALS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appeal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appeal_id")
    private Long id;
    @JoinColumn(name = "local_id")
    @ManyToOne
    private Local local;
    @Column(name = "text")
    private String text;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AppealStatus status;
    @Column(name = "post_date")
    private LocalDateTime postDate;
    @Column(name = "point")
    private Point locationPoint;

    @Setter(AccessLevel.PRIVATE)
    @BatchSize(size = 100)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "appeal")
    private List<AppealFile> files = new LinkedList<>();

}

