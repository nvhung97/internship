package com.example.cpu11398_local.etalk.presentation.model;

public class Message {

    private String key;
    private String data;
    private String time;
    private String sender;

    public Message() {
    }

    public Message(String key, String data, String time, String sender) {
        this.key    = key;
        this.data   = data;
        this.time   = time;
        this.sender = sender;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
