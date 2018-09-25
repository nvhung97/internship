package com.example.cpu11398_local.etalk.data.local;

import android.arch.persistence.room.TypeConverter;
import com.example.cpu11398_local.etalk.presentation.model.Message;
import com.example.cpu11398_local.etalk.presentation.view.chat.person.MessagePersonItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import java.util.Map;

public class Converter {

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
}
