package com.denysenko.citymonitorbot.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "APPEALS")
public class Appeal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appeal_id")
    private Long id;
    @Column(name = "local_id")
    private Long botUserId;
    @Column(name = "text")
    private String text;
    @Column(name = "status")
    private String status;
    @Column(name = "post_date")
    private LocalDateTime postDate;
    @Column(name = "point")
    private Point locationPoint;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "appeal_id", nullable = false)
    private List<File> files = new LinkedList<>();

    @PrePersist
    protected void onCreate(){
        status = "UNREAD";
        postDate = LocalDateTime.now();
    }

}
