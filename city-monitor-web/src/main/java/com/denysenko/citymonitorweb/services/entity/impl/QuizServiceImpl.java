package com.denysenko.citymonitorweb.services.entity.impl;

import com.denysenko.citymonitorweb.enums.QuizStatus;
import com.denysenko.citymonitorweb.exceptions.EntityNotFoundException;
import com.denysenko.citymonitorweb.models.entities.Quiz;

import com.denysenko.citymonitorweb.repositories.hibernate.QuizRepository;
import com.denysenko.citymonitorweb.services.entity.QuizService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
@Log4j
@Service
public class QuizServiceImpl implements QuizService {
    @Autowired
    private QuizRepository quizRepository;

    @Override
    public List<Quiz> getLast10Quizzes() {
        return quizRepository.findFirst10ByOrderByStartDateDesc();
    }

    @Override
    public Page<Quiz> getPageOfQuizzes(int pageNumber, int size){
        if(pageNumber < 1 || size < 1) throw new IllegalArgumentException("Номер сторінки та кількість елементів мають бути більшими за нуль.");

        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC, "startDate"));
        Page<Quiz> quizzesPage = quizRepository.findAll(request);
        return quizzesPage;
    }

    @Override
    public void saveQuiz(Quiz quiz){
        quizRepository.save(quiz);
    }

    @Override
    public Quiz getById(Long id){
        Optional<Quiz> optionalQuiz = quizRepository.findById(id);
        return optionalQuiz.orElseThrow(() -> new EntityNotFoundException("Не вдалось знайти опитування з id = " + id));
    }

    @Override
    public List<Quiz> findQuizzesByLayoutId(Long id) {
        return quizRepository.findAllByLayoutId(id);
    }

    public void setQuizStatusById(Long id, QuizStatus quizStatus) {
        Optional<Quiz> quizOptional = quizRepository.findById(id);
        quizOptional.ifPresentOrElse((q)->{
            q.setStatus(quizStatus);
            quizRepository.save(q);
        }, ()->{
            throw new EntityNotFoundException("Не вдалось знайти опитування з id = " + id);
        });
    }

    public void deleteQuizById(Long quizId){
        log.info("deleting quiz with id = " + quizId);
        quizRepository.deleteById(quizId);
        log.info("quiz with id = " + quizId + " was deleted successfully");
    }


}
