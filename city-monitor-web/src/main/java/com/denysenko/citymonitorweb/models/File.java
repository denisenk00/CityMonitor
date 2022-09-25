package com.denysenko.citymonitorweb.models;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "FILES")
@Data
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;
    @Column(name = "name")
    private String name;

    @Column(name = "tg_file_id")
    private String fileID;

    @Lob
    @Column(name = "file_object")
    private byte[] fileObject;
}
