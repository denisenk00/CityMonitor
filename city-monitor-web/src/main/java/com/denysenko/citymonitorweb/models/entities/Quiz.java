package com.denysenko.citymonitorweb.models.entities;

import com.denysenko.citymonitorweb.enums.QuizStatus;
import com.denysenko.citymonitorweb.models.entities.File;
import com.denysenko.citymonitorweb.models.entities.Layout;
import com.denysenko.citymonitorweb.models.entities.Option;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "QUIZZES")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private QuizStatus status;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;
    @JoinColumn(name = "layout_id")
    @ManyToOne
    private Layout layout;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "quiz_id", nullable = false)
    private List<File> files = new LinkedList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "quiz_id", nullable = false)
    private List<Option> options = new LinkedList<>();

    @PrePersist
    void onCreate(){
        if(Objects.isNull(startDate)){
            startDate = LocalDateTime.now();
            status = QuizStatus.IN_PROGRESS;
        }
    }
}
