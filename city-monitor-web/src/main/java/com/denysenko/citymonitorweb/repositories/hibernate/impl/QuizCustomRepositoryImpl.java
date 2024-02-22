package com.denysenko.citymonitorweb.repositories.hibernate.impl;

import com.denysenko.citymonitorweb.models.entities.Quiz;
import com.denysenko.citymonitorweb.repositories.hibernate.QuizCustomRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizCustomRepositoryImpl implements QuizCustomRepository {

    private final EntityManager em;

    private final String QUERY_GET_QUIZ_WITH_FULL_LAYOUT = "select distinct q " +
            "from Quiz q" +
            " inner join fetch q.layout l" +
            " left join fetch l.polygons p" +
            " where q.id = ?1";

    private final String QUERY_BIND_QUIZ_OPTIONS_TO_EXISTING = "select distinct q " +
            " from Quiz q" +
            "   left join fetch q.options o" +
            " where q in ?1";

    private final String QUERY_BIND_QUIZ_FILES_TO_EXISTING = "select distinct q " +
            " from Quiz q" +
            "   left join fetch q.files f" +
            " where q in ?1";


    public Quiz getQuizWithOptionsAndFullLayoutById(Long id) {

        Quiz quiz = em.createQuery(QUERY_GET_QUIZ_WITH_FULL_LAYOUT, Quiz.class)
                .setParameter(1, id)
                .setHint(QueryHints.PASS_DISTINCT_THROUGH, "false")
                .getSingleResult();

        quiz = em.createQuery(QUERY_BIND_QUIZ_OPTIONS_TO_EXISTING, Quiz.class)
                .setParameter(1, quiz)
                .setHint(QueryHints.PASS_DISTINCT_THROUGH, "false")
                .getSingleResult();

        return quiz;
    }

    public Quiz getFullQuizById(Long id) {

        Quiz quiz = em.createQuery(QUERY_GET_QUIZ_WITH_FULL_LAYOUT, Quiz.class)
                .setParameter(1, id)
                .setHint(QueryHints.PASS_DISTINCT_THROUGH, "false")
                .getSingleResult();
        quiz = em.createQuery(QUERY_BIND_QUIZ_OPTIONS_TO_EXISTING, Quiz.class)
                .setParameter(1, quiz)
                .setHint(QueryHints.PASS_DISTINCT_THROUGH, "false")
                .getSingleResult();
        quiz = em.createQuery(QUERY_BIND_QUIZ_FILES_TO_EXISTING, Quiz.class)
                .setParameter(1, quiz)
                .setHint(QueryHints.PASS_DISTINCT_THROUGH, "false")
                .getSingleResult();

        return quiz;
    }


}
