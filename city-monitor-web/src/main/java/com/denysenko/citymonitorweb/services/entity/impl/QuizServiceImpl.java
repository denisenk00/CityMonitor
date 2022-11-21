package com.denysenko.citymonitorweb.services.entity.impl;

import com.denysenko.citymonitorweb.enums.QuizStatus;
import com.denysenko.citymonitorweb.models.dto.QuizDTO;
import com.denysenko.citymonitorweb.models.entities.Quiz;
import com.denysenko.citymonitorweb.models.domain.paging.Paged;
import com.denysenko.citymonitorweb.models.domain.paging.Paging;
import com.denysenko.citymonitorweb.repositories.hibernate.QuizRepository;
import com.denysenko.citymonitorweb.services.converters.EntityDTOConverter;
import com.denysenko.citymonitorweb.services.converters.impl.QuizEntityToDTOConverter;
import com.denysenko.citymonitorweb.services.entity.FileService;
import com.denysenko.citymonitorweb.services.entity.LayoutService;
import com.denysenko.citymonitorweb.services.entity.OptionService;
import com.denysenko.citymonitorweb.services.entity.QuizService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class QuizServiceImpl implements QuizService {
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private QuizEntityToDTOConverter converter;


    @Override
    public List<Quiz> getLast10Quizzes() {
        return quizRepository.findFirst10ByOrderByStartDateDesc();
    }

    @Override
    public Paged<QuizDTO> getPageOfQuizzes(int pageNumber, int size){
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC, "startDate"));
        Page<Quiz> quizzesPage = quizRepository.findAll(request);
        Page<QuizDTO> dtoPage = quizzesPage.map(quiz -> converter.convertEntityToDTO(quiz));
        return new Paged<QuizDTO>(dtoPage, Paging.of(dtoPage.getTotalPages(), pageNumber, size));
    }

    @Override
    public void saveQuiz(Quiz quiz){
        if(quiz == null) throw new IllegalArgumentException("Quiz should be not NULL");
        quizRepository.save(quiz);
    }


}
