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
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "option_id")
    private Option option;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "polygon_id")
    private Polygon polygon;
    @Column(name = "answers_count")
    private Integer answersCount;

    public Result(Option option, Polygon polygon, Integer answersCount) {
        this.option = option;
        this.polygon = polygon;
        this.answersCount = answersCount;
    }
}
