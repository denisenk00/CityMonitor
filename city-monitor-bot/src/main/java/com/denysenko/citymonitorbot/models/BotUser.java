package com.denysenko.citymonitorbot.models;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

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
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "point_id")
    private LocationPoint location;
    @Column(name = "is_active")
    private boolean isActive;

    public BotUser(Long chatId) {
        this.chatId = chatId;
    }

    public BotUser() {
    }

    public BotUser(Long chatId, String name, String phone, LocationPoint location, boolean isActive) {
        this.chatId = chatId;
        this.name = name;
        this.phone = phone;
        this.location = location;
        this.isActive = isActive;
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

    public Long getBotUserId() {
        return botUserId;
    }

    public void setBotUserId(Long botUserId) {
        this.botUserId = botUserId;
    }
}
