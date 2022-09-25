package com.denysenko.citymonitorweb.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "RESULTS")
@Data
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long resultId;
    @Column(name = "option_id")
    private Long optionId;
    @Column(name = "polygon_id")
    private Long polygonId;
    @Column(name = "answers_count")
    private Integer answersCount;
}
