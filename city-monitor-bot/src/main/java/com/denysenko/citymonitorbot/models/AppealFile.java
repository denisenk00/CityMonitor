package com.denysenko.citymonitorbot.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "APPEAL_FILES")
public class AppealFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;
    @Column(name = "name")
    private String name;
    @NaturalId
    @Column(name = "tg_file_id")
    private String fileID;

    @ManyToOne
    @JoinColumn(name = "appeal_id")
    private Appeal appeal;

    public AppealFile(String name, String fileID) {
        this.name = name;
        this.fileID = fileID;
    }

}
