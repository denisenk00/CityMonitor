package com.denysenko.citymonitorweb.services.entity.impl;

import com.denysenko.citymonitorweb.enums.LayoutStatus;
import com.denysenko.citymonitorweb.enums.QuizStatus;
import com.denysenko.citymonitorweb.models.entities.Layout;
import com.denysenko.citymonitorweb.models.entities.Quiz;

import com.denysenko.citymonitorweb.repositories.hibernate.QuizRepository;
import com.denysenko.citymonitorweb.services.entity.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

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
        if(pageNumber < 1 || size < 1) throw new IllegalArgumentException();

        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC, "startDate"));
        Page<Quiz> quizzesPage = quizRepository.findAll(request);
        return quizzesPage;
    }

    @Override
    public void saveQuiz(Quiz quiz){
        if(quiz == null) throw new IllegalArgumentException("Quiz should be not NULL");
        quizRepository.save(quiz);
    }

    @Override
    public Quiz getById(Long id){
        if(id == null) throw new IllegalArgumentException("Id should be not NULL");
        Optional<Quiz> optionalQuiz = quizRepository.findById(id);
        return optionalQuiz.orElseThrow(() -> new NoSuchElementException(""));
    }

    @Override
    public List<Quiz> findQuizzesByLayoutId(Long id) {
        if(id == null) throw new IllegalArgumentException("Id should be not NULL");
        return quizRepository.findAllByLayoutId(id);
    }

    public void setQuizStatusById(Long id, QuizStatus quizStatus) {
        if(id == null) throw new IllegalArgumentException();
        if(quizStatus == null) throw new IllegalArgumentException();

        Optional<Quiz> quizOptional = quizRepository.findById(id);
        quizOptional.ifPresentOrElse((q)->{
            q.setStatus(quizStatus);
            quizRepository.save(q);
        }, ()->{
            throw new NoSuchElementException();
        });
    }


}
