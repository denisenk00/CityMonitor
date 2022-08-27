package com.denysenko.citymonitorbot.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "LOCALS")
public class BotUser {
    @Id
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "name")
    private String name;
    @Column(name = "phone")
    private String phone;
    @OneToOne
    @MapsId
    @JoinColumn(name = "point_id")
    private LocationPoint location;
    @Column(name = "is_active")
    private boolean isActive;

    public BotUser(Long chatId) {
        this.chatId = chatId;
    }

    public BotUser() {
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String firstName) {
        this.name = firstName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocationPoint getLocation() {
        return location;
    }

    public void setLocation(LocationPoint location) {
        this.location = location;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
