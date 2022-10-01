package com.denysenko.citymonitorweb.models.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "ANSWERS")
@Data
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long answerId;
    @JoinColumn(name = "option_id")
    @OneToOne
    private Option option;
    @JoinColumn(name = "local_id")
    @ManyToOne
    private Local local;


}
