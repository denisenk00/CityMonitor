package com.denysenko.citymonitorbot.models;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.sql.rowset.serial.SerialBlob;
import java.time.LocalDateTime;

@Entity
@Table(name = "APPEALS")
public class Appeal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appeal_id")
    private Integer id;
    @Column(name = "local_id")
    private Long botUserId;
    @Column(name = "text")
    private String text;
    @Column(name = "status")
    private String status;
    @Column(name = "post_date")
    private LocalDateTime postDate;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = true)
    @Cascade(value = org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "point_id")
    private LocationPoint locationPoint;
    @Column(name = "image")
    @Type(type = "org.hibernate.type.BlobType")
    @Lob

    private SerialBlob image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getBotUserId() {
        return botUserId;
    }

    public void setBotUserId(Long chatId) {
        this.botUserId = chatId;
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

    public SerialBlob getImage() {
        return image;
    }

    public void setImage(SerialBlob image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Appeal{" +
                "id=" + id +
                ", botUserId=" + botUserId +
                ", text='" + text + '\'' +
                ", status='" + status + '\'' +
                ", postDate=" + postDate.toString() +
                ", locationPoint=" + locationPoint +
                ", image=" + image +
                '}';
    }
}
