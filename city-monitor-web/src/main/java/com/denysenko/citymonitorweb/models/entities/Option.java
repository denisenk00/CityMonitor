package com.denysenko.citymonitorweb.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "OPTIONS")
@Data
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
