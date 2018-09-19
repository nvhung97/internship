package com.example.cpu11398_local.etalk.presentation.model;

public class Message {

    public static final long TEXT  = 0;
    public static final long IMAGE = 1;
    public static final long SOUND = 2;
    public static final long VIDEO = 3;
    public static final long FILE  = 4;

    private String sender;
    private String data;
    private long   type;
    private long   time;

    public Message() {
    }

    public Message(String data, long type) {
        this(null, data, type);
    }

    public Message(String sender, String data, long type) {
        this(sender, data, type, System.currentTimeMillis());
    }

    public Message(String sender, String data, long type, long time) {
        this.sender = sender;
        this.data   = data;
        this.type   = type;
        this.time   = time;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
