package com.denysenko.citymonitorweb.models.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "OPTIONS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Long id;
    @Column(name = "title")
    private String title;
//    @Column(name = "quiz_id")
//    private Long quizId;

}
