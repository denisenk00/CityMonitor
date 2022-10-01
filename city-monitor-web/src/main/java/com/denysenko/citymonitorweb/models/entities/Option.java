package com.denysenko.citymonitorweb.models.entities;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "OPTIONS")
@Data
@Builder
@NoArgsConstructor
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Long optionId;
    @Column(name = "title")
    private String title;
//    @Column(name = "quiz_id")
//    private Long quizId;

}
