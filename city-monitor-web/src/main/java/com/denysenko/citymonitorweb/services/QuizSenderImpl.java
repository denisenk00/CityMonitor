package com.denysenko.citymonitorweb.services;

import com.denysenko.citymonitorweb.enums.QuizStatus;
import com.denysenko.citymonitorweb.exceptions.SendQuizException;
import com.denysenko.citymonitorweb.models.domain.async.SendQuizTask;
import com.denysenko.citymonitorweb.models.entities.Layout;
import com.denysenko.citymonitorweb.models.entities.Quiz;
import com.denysenko.citymonitorweb.services.entity.LocalService;
import com.denysenko.citymonitorweb.services.entity.QuizService;
import com.denysenko.citymonitorweb.services.telegram.TelegramService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiValidationException;

import java.util.*;

@Log4j
@Service
public class QuizSenderImpl implements QuizSender {
    @Autowired
    private TelegramService telegramService;
    @Autowired
    private LocalService localService;
    @Autowired
    private QuizService quizService;

    private Map<Long, Timer> scheduledTasks = new HashMap<>();

    @Transactional
    @Override
    public void schedule(Quiz quiz) {
        log.info("Scheduling quiz with id = " + quiz.getId());
        SendQuizTask sendQuizTask = new SendQuizTask(quiz, this);
        Timer timer = new Timer();
        Calendar calendar = Calendar.getInstance();
        calendar.set(quiz.getStartDate().getYear(), quiz.getStartDate().getMonthValue() - 1, quiz.getStartDate().getDayOfMonth(),
                quiz.getStartDate().getHour(), quiz.getStartDate().getMinute(), quiz.getStartDate().getSecond());
        timer.schedule(sendQuizTask, calendar.getTime());
        log.info("calendar = " + calendar.getTime().toString());
        scheduledTasks.put(quiz.getId(), timer);
        log.info("Quiz with id = " + quiz.getId() + " scheduled successfully");
    }

    @Transactional
    @Override
    public void sendImmediate(Quiz quiz) {
        log.info("Sending quiz with id = " + quiz.getId());
        removeScheduledSending(quiz.getId());

        Layout quizLayout = quiz.getLayout();
        List<Long> chatIDs = localService.getChatIdsLocatedWithinLayout(quizLayout);

        try {
            telegramService.sendQuizToChats(chatIDs, quiz);
        } catch (TelegramApiValidationException e){
            log.warn("Exception raised", e);
        } catch (TelegramApiRequestException e){
            log.warn("Exception raised:", e);
            int code = e.getErrorCode();
            if(code == 420 || code == 406 || code == 404 || code == 400){
                throw new SendQuizException(e.getMessage(), e, quiz, chatIDs);
            }
        }catch (TelegramApiException | InterruptedException e) {
            String reason = "Помилка при надсиланні даних до Telegram API";
            throw new SendQuizException(reason, e, quiz, chatIDs);
        }
        log.info("Sent quiz id = " + quiz.getId() + " successfully");
        log.info("current status = " + quiz.getStatus() + ", " + !quiz.getStatus().equals(QuizStatus.IN_PROGRESS));
        if(!quiz.getStatus().equals(QuizStatus.IN_PROGRESS)) {
            quizService.setQuizStatusById(quiz.getId(), QuizStatus.IN_PROGRESS);
            log.info("Status of quiz id = " + quiz.getId() + " changed to IN_PROGRESS");
        }
    }

    public void removeScheduledSending(Long quizId){
        log.info("removing quiz from scheduled with id = " + quizId + " if exists");
        Timer timer = scheduledTasks.get(quizId);
        if(timer != null){
            timer.cancel();
            scheduledTasks.remove(quizId);
        }
    }

}
