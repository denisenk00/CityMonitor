package com.denysenko.citymonitorbot.handlers.impl;

import com.denysenko.citymonitorbot.commands.impl.MainMenuCommand;
import com.denysenko.citymonitorbot.commands.impl.StartCommand;
import com.denysenko.citymonitorbot.commands.impl.StopCommand;
import com.denysenko.citymonitorbot.commands.impl.appeal.AppealEnterDescriptionCommand;
import com.denysenko.citymonitorbot.commands.impl.appeal.AppealEnterLocationCommand;
import com.denysenko.citymonitorbot.commands.impl.appeal.SaveAppealCommand;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterLocationCommand;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterNameCommand;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterPhoneNumberCommand;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileMenuCommand;
import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.enums.Commands;
import com.denysenko.citymonitorbot.handlers.Handler;
import com.denysenko.citymonitorbot.services.CacheManager;
import com.denysenko.citymonitorbot.services.entity.BotUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@Log4j
@RequiredArgsConstructor
@Component
public class ReplyButtonHandler implements Handler {

    private final StartCommand startCommand;
    private final ProfileEnterNameCommand profileEnterNameCommand;
    private final ProfileEnterPhoneNumberCommand profileEnterPhoneNumberCommand;
    private final ProfileEnterLocationCommand profileEnterLocationCommand;
    private final ProfileMenuCommand profileMenuCommand;
    private final SaveAppealCommand saveAppealCommand;
    private final MainMenuCommand mainMenuCommand;
    private final StopCommand stopCommand;
    private final AppealEnterDescriptionCommand appealEnterDescriptionCommand;
    private final AppealEnterLocationCommand appealEnterLocationCommand;

    private final BotUserService botUserService;
    private final CacheManager cacheManager;

    private Map<BotStates, ImmutablePair<Consumer, Consumer>> sequenceCommandMap;

    @PostConstruct
    private void configureCommandSequences(){
        sequenceCommandMap = new HashMap<>();
        sequenceCommandMap.put(
                BotStates.EDITING_PROFILE_NAME,
                new ImmutablePair(null, (Consumer<Long>) profileEnterPhoneNumberCommand::execute)
        );
        sequenceCommandMap.put(
                BotStates.EDITING_PROFILE_PHONE,
                new ImmutablePair((Consumer<Long>) profileEnterNameCommand::execute, (Consumer<Long>) profileEnterLocationCommand::execute)
        );
        sequenceCommandMap.put(
                BotStates.EDITING_PROFILE_LOCATION,
                new ImmutablePair((Consumer<Long>) profileEnterPhoneNumberCommand::execute,
                        (Consumer<Long>)(Long chatId) -> {
                            botUserService.saveToDBAndCleanCache(chatId);
                            mainMenuCommand.execute(chatId);
                        }
                )
        );
        sequenceCommandMap.put(
                BotStates.APPEAL_ATTACHING_FILES,
                new ImmutablePair<>(null, (Consumer<Long>) appealEnterLocationCommand::execute)
        );
    }

    @Override
    public boolean isApplicable(Update update) {
        if(!update.hasMessage() || !update.getMessage().hasText()) return false;
        Message message = update.getMessage();
        String text = message.getText();
        Long chatId = message.getChatId();
        Optional<BotStates> botUserState = cacheManager.findBotStateByChatId(chatId);

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
        log.info("Update handled by ReplyButtonHandler: updateId = " + update.getUpdateId() + ", chatId = " + chatId.toString());
        String text = message.getText();

        if(text.equalsIgnoreCase(Commands.START_COMMAND.getTitle()) || text.equalsIgnoreCase(Commands.COMEBACK_COMMAND.getTitle())){
            startCommand.execute(chatId);
        }else if(text.equalsIgnoreCase(Commands.NEXT_STEP_COMMAND.getTitle())) {
            BotStates botUserState = cacheManager.findBotStateByChatId(chatId).get();
            log.info("next bot state = " + botUserState.getTitle());
            sequenceCommandMap.get(botUserState).getRight().accept(chatId);
        }else if(text.equalsIgnoreCase(Commands.PREVIOUS_STEP_COMMAND.getTitle())){
            BotStates botUserState = cacheManager.findBotStateByChatId(chatId).get();
            sequenceCommandMap.get(botUserState).getLeft().accept(chatId);
        }else if(text.equalsIgnoreCase(Commands.PROFILE_COMMAND.getTitle())){
            profileMenuCommand.execute(chatId);
        }else if(text.equalsIgnoreCase(Commands.SEND_APPEAL_COMMAND.getTitle())){
            cacheManager.createEmptyAppeal(chatId);
            appealEnterDescriptionCommand.execute(chatId);
        }else if(text.equalsIgnoreCase(Commands.CANCEL_GENERAL_COMMAND.getTitle())){
            BotStates botUserState = cacheManager.findBotStateByChatId(chatId).get();
            if(botUserState.equals(BotStates.APPEAL_ENTERING_DESCRIPTION) || botUserState.equals(BotStates.APPEAL_ENTERING_LOCATION) || botUserState.equals(BotStates.APPEAL_ATTACHING_FILES)){
                cacheManager.removeAppealByChatId(chatId);
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
