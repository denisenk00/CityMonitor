package com.denysenko.citymonitorbot.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "LOCALS")
public class BotUser {
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
    @Column(name = "location_point")
    private Point location;
    @Column(name = "is_active")
    private boolean isActive;

    public BotUser(Long chatId) {
        this.chatId = chatId;
    }

    public BotUser(Long chatId, String name, String phone, Point location, boolean isActive) {
        this.chatId = chatId;
        this.name = name;
        this.phone = phone;
        this.location = location;
        this.isActive = isActive;
    }

}
