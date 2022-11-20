package com.denysenko.citymonitorweb.models.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "RESULTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long id;
    @Column(name = "option_id")
    private Long optionId;
    @Column(name = "polygon_id")
    private Long polygonId;
    @Column(name = "answers_count")
    private Integer answersCount;
}
