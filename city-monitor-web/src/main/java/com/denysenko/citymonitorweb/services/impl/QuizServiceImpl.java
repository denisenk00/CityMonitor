package com.denysenko.citymonitorweb.services.impl;

import com.denysenko.citymonitorweb.models.Quiz;
import com.denysenko.citymonitorweb.models.paging.Paged;
import com.denysenko.citymonitorweb.models.paging.Paging;
import com.denysenko.citymonitorweb.repositories.hibernate.QuizRepository;
import com.denysenko.citymonitorweb.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {
    @Autowired
    private QuizRepository quizRepository;

    @Override
    public List<Quiz> getFirstNQuizzes(int n) {
        return null;
    }

    public Paged<Quiz> getPageOfQuizzes(int pageNumber, int size){
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC, "startDate"));
        Page<Quiz> postPage = quizRepository.findAll(request);
        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), pageNumber, size));
    }
}
