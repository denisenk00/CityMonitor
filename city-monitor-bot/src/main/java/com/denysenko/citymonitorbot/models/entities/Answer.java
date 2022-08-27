package com.denysenko.citymonitorbot.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "ANSWERS")
public class Answer {
    @Id
    @GeneratedValue
    @Column(name = "answer_id")
    private Long id;
    @Column(name = "option_id")
    private Integer optionId;
    @Column(name = "quiz_id")
    private Integer quizId;
    @Column(name = "chat_id")
    private Long chatId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public Integer getQuizId() {
        return quizId;
    }

    public void setQuizId(Integer quizId) {
        this.quizId = quizId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
}
