package com.denysenko.citymonitorweb.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ANSWERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;
    @JoinColumn(name = "option_id")
    @OneToOne
    private Option option;
    @JoinColumn(name = "local_id")
    @ManyToOne
    private Local local;


}
