package com.denysenko.citymonitorbot.models.entities;



import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "APPEALS")
public class Appeal {
    @Id
    @GeneratedValue
    @Column(name = "appeal_id")
    private Integer id;
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "text")
    private String text;
    @Column(name = "status")
    private String status;
    @Column(name = "post_date")
    private LocalDateTime postDate;
    @OneToOne
    @MapsId
    @JoinColumn(name = "point_id")
    private LocationPoint locationPoint;
    @Column(name = "image")
    @Type(type = "org.hibernate.type.BlobType")
    @Lob
    private byte[] image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDateTime postDate) {
        this.postDate = postDate;
    }

    public LocationPoint getLocationPoint() {
        return locationPoint;
    }

    public void setLocationPoint(LocationPoint locationPoint) {
        this.locationPoint = locationPoint;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
