package com.denysenko.citymonitorweb.models.entities;

import com.vividsolutions.jts.geom.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "LOCALS")
@Data
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
