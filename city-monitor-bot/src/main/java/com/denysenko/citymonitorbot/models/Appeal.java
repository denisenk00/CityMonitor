package com.denysenko.citymonitorbot.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "point_id")
    private LocationPoint locationPoint;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "appeal_id", nullable = false)
    private List<File> files = new LinkedList<>();

    @PrePersist
    protected void onCreate(){
        status = "POSTED";
        postDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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


    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
