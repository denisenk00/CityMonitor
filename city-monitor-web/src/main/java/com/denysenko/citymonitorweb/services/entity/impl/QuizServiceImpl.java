package com.denysenko.citymonitorweb.services.entity.impl;

import com.denysenko.citymonitorweb.enums.LayoutStatus;
import com.denysenko.citymonitorweb.enums.QuizStatus;
import com.denysenko.citymonitorweb.exceptions.EntityNotFoundException;
import com.denysenko.citymonitorweb.models.dto.QuizDTO;
import com.denysenko.citymonitorweb.models.dto.QuizPreviewDTO;
import com.denysenko.citymonitorweb.models.entities.Layout;
import com.denysenko.citymonitorweb.models.entities.Quiz;

import com.denysenko.citymonitorweb.repositories.hibernate.QuizCustomRepository;
import com.denysenko.citymonitorweb.repositories.hibernate.QuizRepository;
import com.denysenko.citymonitorweb.services.converters.impl.QuizEntityToDTOConverter;
import com.denysenko.citymonitorweb.services.entity.LayoutService;
import com.denysenko.citymonitorweb.services.entity.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Log4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final QuizEntityToDTOConverter quizConverter;
    private final QuizCustomRepository quizCustomRepository;
    private final LayoutService layoutService;

    @Override
    public List<QuizPreviewDTO> getLast10QuizzesPreviews() {
        return quizRepository.findFirst10PreviewsByOrderByStartDateDesc();
    }

    @Override
    public Page<QuizPreviewDTO> getPageOfQuizzesPreviews(int pageNumber, int size) {
        if (pageNumber < 1 || size < 1)
            throw new IllegalArgumentException("Номер сторінки та кількість елементів мають бути більшими за нуль.");

        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.DESC, "startDate"));
        Page<QuizPreviewDTO> quizzesPage = quizRepository.findAllPreviewsBy(request);
        return quizzesPage;
    }

    @Transactional
    @Override
    public Quiz saveQuizWithLayout(QuizDTO quizDTO, Long layoutId) {
        Layout layout = layoutService.getFullLayoutById(layoutId);
        layout.setStatus(LayoutStatus.IN_USE);

        Quiz quiz = quizConverter.convertDTOToEntity(quizDTO);
        quiz.setLayout(layout);

        return quizRepository.save(quiz);
    }

    @Override
    public Quiz getById(Long id) {
        Optional<Quiz> optionalQuiz = quizRepository.findById(id);
        return optionalQuiz.orElseThrow(() -> new EntityNotFoundException("Не вдалось знайти опитування з id = " + id));
    }

    @Override
    public Quiz getEntityWithFullLayoutAndOptionsById(Long id) {
        return quizCustomRepository.getQuizWithOptionsAndFullLayoutById(id);
    }


    @Override
    public QuizDTO getFullDTOById(Long id) {
        Quiz quiz = quizCustomRepository.getFullQuizById(id);
        return quizConverter.convertEntityToDTO(quiz);
    }

    @Override
    public List<QuizPreviewDTO> findQuizzesPreviewsByLayoutId(Long id) {
        return quizRepository.findAllPreviewsByLayoutId(id);
    }

    @Transactional
    public void setQuizStatusById(Long id, QuizStatus quizStatus) {
        Optional<Quiz> quizOptional = quizRepository.findById(id);
        quizOptional.ifPresentOrElse((q) -> q.setStatus(quizStatus),
                () -> new EntityNotFoundException("Не вдалось знайти опитування з id = " + id));
    }

    @Transactional
    public void deleteQuizById(Long quizId) {
        log.info("deleting quiz with id = " + quizId);
        quizRepository.deleteById(quizId);
        log.info("quiz with id = " + quizId + " was deleted successfully");
    }


}
