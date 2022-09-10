package com.denysenko.citymonitorbot.models;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Table(name = "FILES")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;
    @Column(name = "name")
    private String name;
    @NaturalId
    @Column(name = "tg_file_id")
    private String fileID;

    public File() {
    }

    public File(String name, String fileID) {
        this.name = name;
        this.fileID = fileID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileID() {
        return fileID;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
