package com.denysenko.citymonitorweb.models.entities;

import com.denysenko.citymonitorweb.enums.QuizStatus;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "QUIZZES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private QuizStatus status;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;
    @JoinColumn(name = "layout_id")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Layout layout;

    @Setter(AccessLevel.PRIVATE)
    @BatchSize(size = 100)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "quiz")
    private List<File> files = new LinkedList<>();

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "quiz")
    private List<Option> options = new LinkedList<>();

    @PrePersist
    void onCreate(){
        if(Objects.isNull(startDate)){
            startDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            status = QuizStatus.IN_PROGRESS;
        }else {
            status = QuizStatus.PLANNED;
        }
    }

    public void addFile(File file){
        file.setQuiz(this);
        this.files.add(file);
    }

    public void addOption(Option option){
        option.setQuiz(this);
        this.options.add(option);
    }
}
