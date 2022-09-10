package com.denysenko.citymonitorbot.handlers.impl;

import com.denysenko.citymonitorbot.commands.CommandSequence;
import com.denysenko.citymonitorbot.commands.impl.MainMenuCommand;
import com.denysenko.citymonitorbot.commands.impl.StartCommand;
import com.denysenko.citymonitorbot.commands.impl.StopCommand;
import com.denysenko.citymonitorbot.commands.impl.appeal.AppealAttachFilesCommand;
import com.denysenko.citymonitorbot.commands.impl.appeal.AppealEnterDescriptionCommand;
import com.denysenko.citymonitorbot.commands.impl.appeal.SaveAppealCommand;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterLocationCommand;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterNameCommand;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterPhoneNumberCommand;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileMenuCommand;
import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.enums.Commands;
import com.denysenko.citymonitorbot.handlers.Handler;
import com.denysenko.citymonitorbot.services.AppealService;
import com.denysenko.citymonitorbot.services.BotUserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class ReplyButtonHandler implements Handler {

    private static final Logger LOG = Logger.getLogger(ReplyButtonHandler.class);
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
    private AppealService appealService;
    @Autowired
    private SaveAppealCommand saveAppealCommand;
    @Autowired
    private MainMenuCommand mainMenuCommand;
    @Autowired
    private StopCommand stopCommand;
    @Autowired
    private AppealEnterDescriptionCommand appealEnterDescriptionCommand;
    @Autowired
    private AppealAttachFilesCommand appealAttachFilesCommand;

    private Map<BotStates, CommandSequence<Long>> sequenceCommandMap;

    @PostConstruct
    private void assignActionMap(){
        sequenceCommandMap = new HashMap<>();
        sequenceCommandMap.put(BotStates.EDITING_PROFILE_NAME, profileEnterNameCommand);
        sequenceCommandMap.put(BotStates.EDITING_PROFILE_PHONE, profileEnterPhoneNumberCommand);
        sequenceCommandMap.put(BotStates.EDITING_PROFILE_LOCATION, profileEnterLocationCommand);
        sequenceCommandMap.put(BotStates.APPEAL_ATTACHING_FILES, appealAttachFilesCommand);
    }

    @Override
    public boolean isApplicable(Update update) {
        if(!update.hasMessage() || !update.getMessage().hasText()) return false;
        Message message = update.getMessage();
        String text = message.getText();
        Long chatId = message.getChatId();
        Optional<BotStates> botUserState = botUserService.findBotStateByChatId(chatId);

        if(botUserState.isPresent()){
            BotStates botState = botUserState.get();
            return (text.equals(Commands.NEXT_STEP_COMMAND.getTitle()) && sequenceCommandMap.containsKey(botState))
                    || (text.equals(Commands.PREVIOUS_STEP_COMMAND.getTitle()) && sequenceCommandMap.containsKey(botState))
                    || (text.equals(Commands.PROFILE_COMMAND.getTitle()) && botState.equals(BotStates.MAIN_MENU))
                    || (text.equals(Commands.SEND_APPEAL_COMMAND.getTitle()) && botState.equals(BotStates.MAIN_MENU))
                    || (text.equals(Commands.CANCEL_GENERAL_COMMAND.getTitle())
                            && (botState.equals(BotStates.APPEAL_ENTERING_DESCRIPTION) || botState.equals(BotStates.PROFILE_MENU)
                                || botState.equals(BotStates.APPEAL_ATTACHING_FILES) || botState.equals(BotStates.APPEAL_ENTERING_LOCATION)))
                    || (text.equals(Commands.SAVE_APPEAL_COMMAND.getTitle()) && botState.equals(BotStates.APPEAL_ENTERING_LOCATION))
                    || (text.equals(Commands.STOP_BOT_COMMAND.getTitle()) && botState.equals(BotStates.PROFILE_MENU))
                    || (text.equals(Commands.EDIT_PROFILE_COMMAND.getTitle()) && botState.equals(BotStates.PROFILE_MENU));
        }else {
            return text.startsWith(Commands.START_COMMAND.getTitle()) || text.equalsIgnoreCase(Commands.COMEBACK_COMMAND.getTitle());
        }
    }

    @Override
    public void handle(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        LOG.info("Update handled by ReplyButtonHandler: updateId = " + update.getUpdateId() + ", chatId = " + chatId.toString());
        String text = message.getText();

        if(text.equalsIgnoreCase(Commands.START_COMMAND.getTitle()) || text.equalsIgnoreCase(Commands.COMEBACK_COMMAND.getTitle())){
            startCommand.execute(chatId);
        }else if(text.equalsIgnoreCase(Commands.NEXT_STEP_COMMAND.getTitle())) {
            BotStates botUserState = botUserService.findBotStateByChatId(chatId).get();
            LOG.info("next bot state = " + botUserState.getTitle());
            sequenceCommandMap.get(botUserState).executeNext(chatId);
        }else if(text.equalsIgnoreCase(Commands.PREVIOUS_STEP_COMMAND.getTitle())){
            BotStates botUserState = botUserService.findBotStateByChatId(chatId).get();
            sequenceCommandMap.get(botUserState).executePrevious(chatId);
        }else if(text.equalsIgnoreCase(Commands.PROFILE_COMMAND.getTitle())){
            profileMenuCommand.execute(chatId);
        }else if(text.equalsIgnoreCase(Commands.SEND_APPEAL_COMMAND.getTitle())){
            appealEnterDescriptionCommand.execute(chatId);
        }else if(text.equalsIgnoreCase(Commands.CANCEL_GENERAL_COMMAND.getTitle())){
            BotStates botUserState = botUserService.findBotStateByChatId(chatId).get();
            if(botUserState.equals(BotStates.APPEAL_ENTERING_DESCRIPTION) || botUserState.equals(BotStates.APPEAL_ENTERING_LOCATION) || botUserState.equals(BotStates.APPEAL_ATTACHING_FILES)){
                appealService.removeAppealByChatIdFromCache(chatId);
            }
            mainMenuCommand.execute(chatId);
        }else if(text.equalsIgnoreCase(Commands.SAVE_APPEAL_COMMAND.getTitle())){
            saveAppealCommand.execute(chatId);
            mainMenuCommand.execute(chatId);
        }else if(text.equalsIgnoreCase(Commands.STOP_BOT_COMMAND.getTitle())){
            stopCommand.execute(chatId);
        }else if(text.equalsIgnoreCase(Commands.EDIT_PROFILE_COMMAND.getTitle())){
            profileEnterNameCommand.execute(chatId);
        }
    }
}
