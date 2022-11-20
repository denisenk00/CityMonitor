package com.denysenko.citymonitorweb.models.entities;


import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;

@Entity
@Table(name = "LOCALS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Local {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "local_id")
    private Long id;
    @NaturalId
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "name")
    private String name;
    @Column(name = "phone")
    private String phone;
    @Column(name = "point_id")
    private Point location;
    @Column(name = "is_active")
    private boolean isActive;
}
