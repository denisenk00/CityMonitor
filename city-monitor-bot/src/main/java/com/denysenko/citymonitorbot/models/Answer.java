package com.denysenko.citymonitorbot.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ANSWERS")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;
    @Column(name = "option_id")
    private Long optionId;
    @Column(name = "local_id")
    private Long localId;
    @ManyToOne(optional = false)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    public Answer(Long localId, Long optionId, Quiz quiz) {
        this.localId = localId;
        this.optionId = optionId;
        this.quiz = quiz;
    }

}
