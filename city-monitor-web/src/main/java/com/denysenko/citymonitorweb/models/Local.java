package com.denysenko.citymonitorweb.models;

import com.vividsolutions.jts.geom.Point;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "LOCALS")
@Data
public class Local {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "local_id")
    private Long botUserId;
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
