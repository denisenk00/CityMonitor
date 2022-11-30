package com.denysenko.citymonitorweb.services;

import com.denysenko.citymonitorweb.enums.QuizStatus;
import com.denysenko.citymonitorweb.models.domain.async.SendQuizTask;
import com.denysenko.citymonitorweb.models.entities.Layout;
import com.denysenko.citymonitorweb.models.entities.Quiz;
import com.denysenko.citymonitorweb.services.entity.LocalService;
import com.denysenko.citymonitorweb.services.entity.QuizService;
import com.denysenko.citymonitorweb.services.telegram.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuizSenderImpl implements QuizSender {
    @Autowired
    private TelegramService telegramService;
    @Autowired
    private LocalService localService;
    @Autowired
    private QuizService quizService;

    private Map<Long, Timer> scheduledTasks = new HashMap<>();

    @Override
    public void schedule(Quiz quiz) {
        if(quiz == null) throw new IllegalArgumentException();

        SendQuizTask sendQuizTask = new SendQuizTask(quiz);
        Timer timer = new Timer();
        Calendar calendar = Calendar.getInstance();
        calendar.set(quiz.getStartDate().getYear(), quiz.getStartDate().getMonthValue() - 1, quiz.getStartDate().getDayOfMonth(),
                quiz.getStartDate().getHour(), quiz.getStartDate().getMinute(), quiz.getStartDate().getSecond());
        timer.schedule(sendQuizTask, calendar.getTime());
        scheduledTasks.put(quiz.getId(), timer);
    }

    @Override
    public void sendImmediate(Quiz quiz) {
        if(quiz == null) throw new IllegalArgumentException();
        removeScheduledQuiz(quiz.getId());

        Layout quizLayout = quiz.getLayout();
        List<Long> chatIDs = localService.getChatIdsLocatedWithinLayout(quizLayout);

        chatIDs.forEach(chatId -> {
            telegramService.sendQuizByChatId(chatId, quiz);
        });
        if(!quiz.getStatus().equals(QuizStatus.IN_PROGRESS)) {
            quizService.setQuizStatusById(quiz.getId(), QuizStatus.IN_PROGRESS);
        }
    }

    public void removeScheduledQuiz(Long quizId){
        if (quizId == null) throw new IllegalArgumentException();

        Timer timer = scheduledTasks.get(quizId);
        if(timer != null){
            timer.cancel();
            scheduledTasks.remove(quizId);
        }
    }

}
