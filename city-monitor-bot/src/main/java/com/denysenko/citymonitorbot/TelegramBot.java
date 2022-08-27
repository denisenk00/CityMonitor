package com.denysenko.citymonitorbot;

import com.denysenko.citymonitorbot.handlers.SuperUpdateHandler;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private static final Logger LOG = Logger.getLogger(TelegramBot.class);

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Autowired
    private SuperUpdateHandler superUpdateHandler;



    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        LOG.info("Update received.." + update.getMessage().getChatId() + "      " + update.getMessage().getFrom().getId());
        superUpdateHandler.handle(update);
//        try {
//            if (update.hasCallbackQuery()){
//                CallbackQuery callbackQuery = update.getCallbackQuery();
//                LOG.info("Callback: message = " + callbackQuery.getMessage() + " , " + callbackQuery.getInlineMessageId() + " , " + callbackQuery.getChatInstance() +
//                        ", c" + callbackQuery.getData() + ", " + callbackQuery.getId() + " , ") ;
//                User user = callbackQuery.getFrom();
//                LOG.info(user.getId() + ", ");
//            }
//            if(update.hasMessage()){
//                Message mes = update.getMessage();
//                LOG.info("message: " + mes.getChatId());
//                if(mes.hasContact()){
//                    LOG.info("contact " + mes.getContact().getFirstName()+ ", " + mes.getContact().getLastName() + ", " +
//                            mes.getContact().getPhoneNumber());
//                }
//                if(mes.hasLocation()){
//                    LOG.info("loc: " + mes.getLocation().getLatitude() + ", " + mes.getLocation().getLatitude());
//                }
//            }
//            //update.getCallbackQuery()
//            if(" " == null ) throw new TelegramApiException();
//            execute(new SendMessage(String.valueOf(update.getMessage().getChatId()), "Hii!"));
//            InlineKeyboardMarkup.InlineKeyboardMarkupBuilder ikmBuilder = InlineKeyboardMarkup.builder();
//            InlineKeyboardButton button = new InlineKeyboardButton();
//            button.setText("1");
//            button.setCallbackData("1");
//            InlineKeyboardButton button2 = new InlineKeyboardButton();
//            button2.setText("2");
//            button2.setCallbackData("2");
//            InlineKeyboardButton button3 = new InlineKeyboardButton();
//            button3.setText("3");
//            button3.setCallbackData("3");
//            ikmBuilder.keyboardRow(Arrays.asList(button, button2, button3));
//
//            SendMessage.SendMessageBuilder message = SendMessage.builder();
//            message.text("Привет житель, как ты оцениваешь чистоту в своем районе?");
//            message.replyMarkup(ikmBuilder.build());
//            message.chatId(String.valueOf(update.getMessage().getChatId()));
//            execute(message.build());
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }

    }
}
