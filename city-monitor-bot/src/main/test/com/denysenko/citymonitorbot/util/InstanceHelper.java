package com.denysenko.citymonitorbot.util;

import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberBanned;

public class InstanceHelper {

    public static User createUser(Long id){
        return new User(id, "", false, null, null, null, null,null,null);
    }

    public static Message createMessage(Integer id, User user, Chat chat, String text, Contact contact, Location location,
                                        Document document){
        var message = new Message();
        message.setMessageId(id);
        message.setFrom(user);
        message.setContact(contact);
        message.setLocation(location);
        message.setText(text);
        message.setChat(chat);
        message.setDocument(document);
        return message;
    }

    public static Message createMessage(Integer id, User user, Chat chat, String text){
        return createMessage(id, user, chat, text, null, null, null);
    }

    public static Message createMessage(Integer id, User user, Chat chat, Contact contact){
        return createMessage(id, user, chat, null, contact, null, null);
    }

    public static Message createMessage(Integer id, User user, Chat chat, Location location){
        return createMessage(id, user, chat, null, null, location, null);
    }

    public static Message createMessage(Integer id, User user, Chat chat, Document document){
        return createMessage(id, user, chat, null, null, null, document );
    }


    public static Update createUpdate(Integer id, Message message){
        var update = new Update();
        update.setUpdateId(id);
        update.setMessage(message);
        return update;
    }

    public static Update createUpdate(Integer id, CallbackQuery callbackQuery){
        var update = new Update();
        update.setUpdateId(id);
        update.setCallbackQuery(callbackQuery);
        return update;
    }

    public static Location createLocation(double latitude, double longitude){
        return new Location(longitude, latitude, null, null, null, null);
    }

    public static CallbackQuery createCallbackQuery(String id, User from, String data){
        var callback = new CallbackQuery();
        callback.setData(data);
        callback.setFrom(from);
        callback.setId(id);
        return callback;
    }

    public static Document createDocument(String name, String fileID) {
        var doc = new Document();
        doc.setFileId(fileID);
        doc.setFileName(name);
        return doc;
    }

    public static Update createUpdateWithKicking(Long chatId){
        var update = new Update();
        var user = new User();
        user.setId(chatId);
        update.setUpdateId(1);
        var chatMemberUpdated = new ChatMemberUpdated();
        chatMemberUpdated.setFrom(user);
        var newChatMember = new ChatMemberBanned();
        chatMemberUpdated.setNewChatMember(newChatMember);
        update.setMyChatMember(chatMemberUpdated);
        return update;
    }


}
