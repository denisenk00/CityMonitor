package com.denysenko.citymonitorweb.models.entities;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "FILES")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;
    @Column(name = "name")
    private String name;

    @Column(name = "tg_file_id")
    private String fileID;
    @Column(name = "appeal_id", insertable = false, updatable = false)
    private Long appealId;
    @Column(name = "quiz_id", insertable = false, updatable = false)
    private Long quizId;
    @Lob
    @Column(name = "file_object")
    private byte[] content;
}
