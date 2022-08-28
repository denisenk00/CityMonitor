package com.denysenko.citymonitorbot.handlers.impl;

import com.denysenko.citymonitorbot.commands.CommandSequence;
import com.denysenko.citymonitorbot.commands.impl.MainMenuCommand;
import com.denysenko.citymonitorbot.commands.impl.SendAppealMenuCommand;
import com.denysenko.citymonitorbot.commands.impl.StartCommand;
import com.denysenko.citymonitorbot.commands.impl.StopCommand;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterLocationCommand;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterNameCommand;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterPhoneNumberCommand;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileMenuCommand;
import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.enums.Commands;
import com.denysenko.citymonitorbot.handlers.Handler;
import com.denysenko.citymonitorbot.services.BotUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class ReplyButtonHandler implements Handler {
    @Autowired
    private StartCommand startCommand;
    @Autowired
    private ProfileEnterNameCommand profileEnterNameCommand;
    @Autowired
    private ProfileEnterPhoneNumberCommand profileEnterPhoneNumberCommand;
    @Autowired
    private ProfileEnterLocationCommand profileEnterLocationCommand;
    @Autowired
    private ProfileMenuCommand profileMenuCommand;
    @Autowired
    private BotUserService botUserService;
    @Autowired
    private SendAppealMenuCommand sendAppealMenuCommand;
    @Autowired
    private MainMenuCommand mainMenuCommand;
    @Autowired
    private StopCommand stopCommand;

    private Map<BotStates, CommandSequence<Long>> sequenceCommandMap = createActionMap();

    private Map<BotStates, CommandSequence<Long>> createActionMap(){
        Map<BotStates, CommandSequence<Long>> map = new HashMap<>();
        map.put(BotStates.EDITING_PROFILE_NAME, profileEnterNameCommand);
        map.put(BotStates.EDITING_PROFILE_PHONE, profileEnterPhoneNumberCommand);
        map.put(BotStates.EDITING_PROFILE_LOCATION, profileEnterLocationCommand);
        return map;
    }

    @Override
    public boolean isApplicable(Update update) {
        if(!update.hasMessage() && !update.getMessage().hasText()) return false;
        String text = update.getMessage().getText();
        return text.startsWith(Commands.START_COMMAND.getTitle())
                || text.equalsIgnoreCase(Commands.NEXT_STEP_COMMAND.getTitle())
                || text.equalsIgnoreCase(Commands.PREVIOUS_STEP_COMMAND.getTitle())
                || text.equalsIgnoreCase(Commands.PROFILE_COMMAND.getTitle())
                || text.equalsIgnoreCase(Commands.SEND_APPEAL_COMMAND.getTitle());
    }

    @Override
    public void handle(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        String text = message.getText();
        if(text.equalsIgnoreCase(Commands.START_COMMAND.getTitle())){
            startCommand.execute(chatId);
        }else if(text.equalsIgnoreCase(Commands.NEXT_STEP_COMMAND.getTitle())){
            Optional<BotStates> botUserState = botUserService.findBotStateByChatId(chatId);
            botUserState.ifPresentOrElse(existingBotState -> {
                sequenceCommandMap.get(existingBotState).executeNext(chatId);
            },
                    ()->{});
        }else if(text.equalsIgnoreCase(Commands.PREVIOUS_STEP_COMMAND.getTitle())){
            Optional<BotStates> botUserState = botUserService.findBotStateByChatId(chatId);
            botUserState.ifPresentOrElse(existingBotState -> {
                        sequenceCommandMap.get(existingBotState).executePrevious(chatId);
                    },
                    ()->{});
        }else if(text.equalsIgnoreCase(Commands.PROFILE_COMMAND.getTitle())){
            profileMenuCommand.execute(chatId);
        }else if(text.equalsIgnoreCase(Commands.SEND_APPEAL_COMMAND.getTitle())){
            sendAppealMenuCommand.execute(chatId);
        }else if(text.equalsIgnoreCase(Commands.CANCEL_GENERAL_COMMAND.getTitle())){
            mainMenuCommand.execute(chatId);
        }else if(text.equalsIgnoreCase(Commands.STOP_BOT_COMMAND.getTitle())){
            stopCommand.execute(chatId);
        }
    }
}
