package com.denysenko.citymonitorbot.handlers;


import com.denysenko.citymonitorbot.commands.impl.MainMenuCommand;
import com.denysenko.citymonitorbot.commands.impl.StartCommand;
import com.denysenko.citymonitorbot.commands.impl.StopCommand;
import com.denysenko.citymonitorbot.commands.impl.answer.SaveAnswerCommand;
import com.denysenko.citymonitorbot.commands.impl.appeal.AppealAttachFilesCommand;
import com.denysenko.citymonitorbot.commands.impl.appeal.AppealEnterDescriptionCommand;
import com.denysenko.citymonitorbot.commands.impl.appeal.AppealEnterLocationCommand;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterLocationCommand;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterNameCommand;
import com.denysenko.citymonitorbot.commands.impl.profile.ProfileEnterPhoneNumberCommand;
import com.denysenko.citymonitorbot.enums.BotStates;
import com.denysenko.citymonitorbot.enums.Commands;
import com.denysenko.citymonitorbot.exceptions.NotAllowedQuizStatusException;
import com.denysenko.citymonitorbot.handlers.impl.*;
import com.denysenko.citymonitorbot.services.CacheManager;
import com.denysenko.citymonitorbot.services.TelegramService;
import com.denysenko.citymonitorbot.services.entity.BotUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.objects.*;

import java.util.Optional;

import static com.denysenko.citymonitorbot.util.InstanceHelper.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UpdateHandlersTest {
    @Autowired
    private SuperUpdateHandler superUpdateHandler;
    @SpyBean
    private ReplyButtonHandler replyButtonHandler;
    @SpyBean
    private TextHandler textHandler;
    @SpyBean
    private LocationHandler locationHandler;
    @SpyBean
    private ContactHandler contactHandler;
    @SpyBean
    private CallBackHandler callBackHandler;
    @SpyBean
    private ChatMemberHandler chatMemberHandler;
    @SpyBean
    private FileHandler fileHandler;
    @MockBean
    private StartCommand startCommand;
    @MockBean
    private StopCommand stopCommand;
    @MockBean
    private ProfileEnterNameCommand profileEnterNameCommand;
    @MockBean
    private ProfileEnterPhoneNumberCommand profileEnterPhoneNumberCommand;
    @MockBean
    private ProfileEnterLocationCommand profileEnterLocationCommand;
    @MockBean
    private SaveAnswerCommand saveAnswerCommand;
    @MockBean
    private MainMenuCommand mainMenuCommand;
    @MockBean
    private CacheManager cacheManager;
    @MockBean
    private BotUserService botUserService;
    @MockBean
    private TelegramService telegramService;
    @MockBean
    private AppealEnterDescriptionCommand appealEnterDescriptionCommand;
    @MockBean
    private AppealEnterLocationCommand appealEnterLocationCommand;
    @MockBean
    private AppealAttachFilesCommand appealAttachFilesCommand;

    @Test
    void handleStartCommand() {
        var user = createUser(1L);
        var msg = createMessage(1, user, new Chat(1L, ""), Commands.START_COMMAND.getTitle());
        var update = createUpdate(1, msg);

        superUpdateHandler.handle(update);
        Mockito.verify(replyButtonHandler).handle(update);
        Mockito.verify(startCommand).execute(1L);
    }

    @Test
    void handleStopCommand() {
        Long chatId = 1L;
        var user = createUser(1L);
        var msg = createMessage(1, user, new Chat(chatId, ""), Commands.STOP_BOT_COMMAND.getTitle());
        var update = createUpdate(1, msg);

        Mockito.when(cacheManager.findBotStateByChatId(chatId)).thenReturn(Optional.of(BotStates.PROFILE_MENU));
        superUpdateHandler.handle(update);
        Mockito.verify(replyButtonHandler).handle(update);
        Mockito.verify(stopCommand).execute(1L);
    }

    @Test
    void handleEnteredProfileName() {
        Long chatId = 1L;
        String name = "UserName";
        var user = createUser(1L);
        var msg = createMessage(1, user, new Chat(chatId, ""), name);
        var update = createUpdate(1, msg);

        Mockito.when(cacheManager.findBotStateByChatId(chatId)).thenReturn(Optional.of(BotStates.EDITING_PROFILE_NAME));
        Mockito.doNothing().when(profileEnterPhoneNumberCommand).execute(chatId);
        superUpdateHandler.handle(update);
        Mockito.verify(textHandler).handle(update);
        Mockito.verify(profileEnterNameCommand).saveUserName(chatId, name);
    }

    @Test
    void handleEnteredProfileNumber() {
        Long chatId = 1L;
        String phone = "+380999003300";
        var user = createUser(1L);
        var msg = createMessage(1, user, new Chat(chatId, ""), phone);
        var update = createUpdate(1, msg);

        Mockito.when(cacheManager.findBotStateByChatId(chatId)).thenReturn(Optional.of(BotStates.EDITING_PROFILE_PHONE));
        Mockito.doNothing().when(profileEnterLocationCommand).execute(chatId);
        superUpdateHandler.handle(update);
        Mockito.verify(textHandler).handle(update);
        Mockito.verify(profileEnterPhoneNumberCommand).savePhoneNumber(chatId, phone);
    }

    @Test
    void handleEnteredProfileNumberAsContact() {
        Long chatId = 1L;
        String phone = "+380999003300";
        var contact = new Contact();
        contact.setPhoneNumber(phone);
        var user = createUser(1L);
        var msg = createMessage(1, user, new Chat(chatId, ""), contact);
        var update = createUpdate(1, msg);

        Mockito.when(cacheManager.findBotStateByChatId(chatId)).thenReturn(Optional.of(BotStates.EDITING_PROFILE_PHONE));
        Mockito.doNothing().when(profileEnterLocationCommand).execute(chatId);
        superUpdateHandler.handle(update);
        Mockito.verify(contactHandler).handle(update);
        Mockito.verify(profileEnterPhoneNumberCommand).savePhoneNumber(chatId, phone);
    }

    @Test
    void handleEnteredProfileLocation() {
        Long chatId = 1L;
        double latitude = 134214.323;
        double longitude = 423234.434;
        var location = createLocation(latitude, longitude);
        var user = createUser(1L);
        var msg = createMessage(1, user, new Chat(chatId, ""), location);
        var update = createUpdate(1, msg);

        Mockito.when(cacheManager.findBotStateByChatId(chatId)).thenReturn(Optional.of(BotStates.EDITING_PROFILE_LOCATION));
        Mockito.doNothing().when(mainMenuCommand).execute(chatId);
        Mockito.doNothing().when(botUserService).saveToDBAndCleanCache(chatId);
        superUpdateHandler.handle(update);
        Mockito.verify(locationHandler).handle(update);
        Mockito.verify(profileEnterLocationCommand).saveLocation(chatId, longitude, latitude);
    }


    private void handleCancelGeneralCommand(BotStates botState) {
        Long chatId = 1L;
        var user = createUser(1L);
        var msg = createMessage(1, user, new Chat(chatId, ""), Commands.CANCEL_GENERAL_COMMAND.getTitle());
        var update = createUpdate(1, msg);

        Mockito.when(cacheManager.findBotStateByChatId(chatId)).thenReturn(Optional.of(botState));
        Mockito.doNothing().when(mainMenuCommand).execute(chatId);
        superUpdateHandler.handle(update);
        Mockito.verify(replyButtonHandler).handle(update);
        Mockito.verify(mainMenuCommand).execute(chatId);
    }

    @Test
    public void handleCancelGeneralCommandInProfileMenu() {
        handleCancelGeneralCommand(BotStates.PROFILE_MENU);
    }

    @Test
    public void handleCancelGeneralCommandInAppeal() {
        Mockito.doNothing().when(cacheManager).removeAppealByChatId(Mockito.anyLong());
        handleCancelGeneralCommand(BotStates.APPEAL_ENTERING_DESCRIPTION);
    }

    @Test
    public void handleQuizAnswer(){
        Long chatId = 1L;
        long quizId = 13123;
        long optionId = 214241;
        String data = "answer:quizId=" + quizId + ",optionId=" + optionId;
        var user = createUser(1L);
        var update = createUpdate(1, createCallbackQuery("1", user, data));

        Mockito.when(cacheManager.findBotStateByChatId(chatId)).thenReturn(Optional.of(BotStates.MAIN_MENU));
        Mockito.doNothing().when(telegramService).sendAnswerCallbackQuery(Mockito.anyString(), Mockito.anyString());
        superUpdateHandler.handle(update);
        Mockito.verify(callBackHandler).handle(update);
        Mockito.verify(saveAnswerCommand).saveAnswer(chatId, quizId, optionId);
    }

    @Test
    public void handleInactiveQuizAnswer(){
        Long chatId = 1L;
        long quizId = 13123;
        long optionId = 214241;
        String data = "answer:quizId=" + quizId + ",optionId=" + optionId;
        var user = createUser(1L);
        var update = createUpdate(1, createCallbackQuery("1", user, data));
        Mockito.doThrow(NotAllowedQuizStatusException.class)
                .when(saveAnswerCommand).saveAnswer(chatId, quizId, optionId);
        Mockito.when(cacheManager.findBotStateByChatId(chatId)).thenReturn(Optional.of(BotStates.MAIN_MENU));

        superUpdateHandler.handle(update);

        Mockito.verify(callBackHandler).handle(update);
        Mockito.verify(telegramService).sendAnswerCallbackQuery(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void handleWriteAppealCommand(){
        Long chatId = 1L;
        var user = createUser(1L);
        var msg = createMessage(1, user, new Chat(chatId, ""), Commands.SEND_APPEAL_COMMAND.getTitle());
        var update = createUpdate(1, msg);

        Mockito.when(cacheManager.findBotStateByChatId(chatId)).thenReturn(Optional.of(BotStates.MAIN_MENU));
        superUpdateHandler.handle(update);
        Mockito.verify(replyButtonHandler).handle(update);
        Mockito.verify(appealEnterDescriptionCommand).execute(chatId);
    }

    @Test
    public void handleEnteredAppealDescription(){
        Long chatId = 1L;
        String description = "appeal_description";
        var user = createUser(1L);
        var msg = createMessage(1, user, new Chat(chatId, ""), description);
        var update = createUpdate(1, msg);

        Mockito.when(cacheManager.findBotStateByChatId(chatId)).thenReturn(Optional.of(BotStates.APPEAL_ENTERING_DESCRIPTION));
        superUpdateHandler.handle(update);
        Mockito.verify(textHandler).handle(update);
        Mockito.verify(appealEnterDescriptionCommand).saveDescription(chatId, description);
    }

    @Test
    public void handleAttachedAppealDocument(){
        Long chatId = 1L;
        String fileName = "name";
        String fileId = "fileID";
        var document = createDocument(fileName, fileId);
        var user = createUser(1L);
        var msg = createMessage(1, user, new Chat(chatId, ""), document);
        var update = createUpdate(1, msg);

        Mockito.when(cacheManager.findBotStateByChatId(chatId)).thenReturn(Optional.of(BotStates.APPEAL_ATTACHING_FILES));
        superUpdateHandler.handle(update);
        Mockito.verify(fileHandler).handle(update);
        Mockito.verify(appealAttachFilesCommand).saveFile(chatId, fileName, fileId);
    }

    @Test
    public void handleAttachedAppealLocation(){
        Long chatId = 1L;
        double latitude = 134214.323;
        double longitude = 423234.434;
        var location = createLocation(latitude, longitude);
        var user = createUser(1L);
        var msg = createMessage(1, user, new Chat(chatId, ""), location);
        var update = createUpdate(1, msg);

        Mockito.when(cacheManager.findBotStateByChatId(chatId)).thenReturn(Optional.of(BotStates.APPEAL_ENTERING_LOCATION));
        superUpdateHandler.handle(update);
        Mockito.verify(locationHandler).handle(update);
        Mockito.verify(appealEnterLocationCommand).saveLocation(chatId, longitude, latitude);
    }

    @Test
    public void handleBlockBotCommand(){
        Long chatId = 1L;
        var update = createUpdateWithKicking(chatId);
        superUpdateHandler.handle(update);
        Mockito.verify(chatMemberHandler).handle(update);
        Mockito.verify(botUserService).deactivateBotUser(chatId);
        Mockito.verify(cacheManager).removeBotStateByChatId(chatId);
        Mockito.verify(cacheManager).removeBotUserByChatId(chatId);
    }


}