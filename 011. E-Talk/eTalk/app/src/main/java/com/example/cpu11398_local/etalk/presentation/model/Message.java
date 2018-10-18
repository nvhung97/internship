package com.example.cpu11398_local.etalk.presentation.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;
import com.google.firebase.database.ServerValue;

public class Message {

    public static final long TEXT  = 0;
    public static final long IMAGE = 1;
    public static final long SOUND = 2;
    public static final long VIDEO = 3;
    public static final long FILE  = 4;
    public static final long MAP   = 5;

    private String key;
    private String sender;
    private String data;
    private long   type;
    private Object time;

    public Message() {
    }

    public Message(Message message) {
        this.key    = message.key;
        this.sender = message.sender;
        this.data   = message.data;
        this.type   = message.type;
        this.time   = message.time;
    }

    public Message(String data, long type) {
        this(null, data, type);
    }

    public Message(String sender, String data, long type) {
        this(sender + System.currentTimeMillis(), sender, data, type, ServerValue.TIMESTAMP);
    }

    public Message(String key, String sender, String data, long type, Object time) {
        this.key    = key;
        this.sender = sender;
        this.data   = data;
        this.type   = type;
        this.time   = time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    @Exclude
    public long getTime() {
        if (time instanceof Long) {
            return (long)time;
        }
        if (time instanceof Double) {
            return ((Double)time).longValue();
        }
        return 0L;
    }

    @PropertyName("time")
    public Object getServerTime() {
        return time;
    }

    @PropertyName("time")
    public void setServerTime(Object time) {
        this.time = time;
    }
}
