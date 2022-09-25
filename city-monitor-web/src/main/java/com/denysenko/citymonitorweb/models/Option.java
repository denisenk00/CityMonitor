package com.denysenko.citymonitorweb.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "OPTIONS")
@Data
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Long optionId;
    @Column(name = "title")
    private String title;
    @Column(name = "quiz_id")
    private Long quizId;

}
