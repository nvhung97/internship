package com.example.cpu11398_local.etalk.data.local;

import android.arch.persistence.room.TypeConverter;
import com.example.cpu11398_local.etalk.presentation.model.Message;
import com.example.cpu11398_local.etalk.presentation.view.chat.group.MessageGroupItem;
import com.example.cpu11398_local.etalk.presentation.view.chat.person.MessagePersonItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import java.util.Map;

public class Converter {

    @TypeConverter
    public static String fromObjectObject(Object object) {
        return new Gson().toJson(object);
    }

    @TypeConverter
    public static Object fromObjectString(String object) {
        return new Gson().fromJson(object, new TypeToken<Object>(){}.getType());
    }

    @TypeConverter
    public static String fromMapMembersObject(Map<String, Long> members) {
        return new Gson().toJson(members);
    }

    @TypeConverter
    public static Map<String, Long> fromMapMembersString(String members) {
        return new Gson().fromJson(members, new TypeToken<Map<String, Long>>(){}.getType());
    }

    @TypeConverter
    public static String fromMessageObject(Message messages) {
        return new Gson().toJson(messages);
    }

    @TypeConverter
    public static Message fromMessageString(String message) {
        return new Gson().fromJson(message, new TypeToken<Message>(){}.getType());
    }

    @TypeConverter
    public static String fromMapMessageObject(Map<String, Message> messages) {
        return new Gson().toJson(messages);
    }

    @TypeConverter
    public static Map<String, Message> fromMapMessageString(String message) {
        return new Gson().fromJson(message, new TypeToken<Map<String, Message>>(){}.getType());
    }

    @TypeConverter
    public static String fromListMessagePersonItemObject(List<MessagePersonItem> items) {
        return new Gson().toJson(items);
    }

    @TypeConverter
    public static List<MessagePersonItem> fromListMessagePersonItemString(String item) {
        return new Gson().fromJson(item, new TypeToken<List<MessagePersonItem>>(){}.getType());
    }

    @TypeConverter
    public static String fromListMessageGroupItemObject(List<MessageGroupItem> items) {
        return new Gson().toJson(items);
    }

    @TypeConverter
    public static List<MessageGroupItem> fromListMessageGroupItemString(String item) {
        return new Gson().fromJson(item, new TypeToken<List<MessageGroupItem>>(){}.getType());
    }
}
