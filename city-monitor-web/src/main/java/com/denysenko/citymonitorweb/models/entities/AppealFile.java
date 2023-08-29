package com.denysenko.citymonitorweb.models.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "APPEAL_FILES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppealFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;
    @Column(name = "name")
    private String name;

    @Column(name = "tg_file_id")
    private String tgFileId;

    @JoinColumn(name = "appeal_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Appeal appeal;

}
