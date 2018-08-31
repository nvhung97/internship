package com.example.cpu11398_local.etalk.presentation.model;

import java.util.Map;

public class Conversation {

    private String              key;
    private String              type;
    private String              name;
    private Map<String, Long>   members;
    private String              messages;
    private Message             last;

    public Conversation() {
    }

    public Conversation(String key,
                        String type,
                        String name,
                        Map<String, Long> members) {
        this.key        = key;
        this.type       = type;
        this.name       = name;
        this.members    = members;
    }

    public Conversation(String key,
                        String type,
                        String name,
                        Map<String, Long> members,
                        String messages,
                        Message last) {
        this.key        = key;
        this.type       = type;
        this.name       = name;
        this.members    = members;
        this.messages   = messages;
        this.last       = last;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Long> getMembers() {
        return members;
    }

    public void setMembers(Map<String, Long> members) {
        this.members = members;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public Message getLast() {
        return last;
    }

    public void setLast(Message last) {
        this.last = last;
    }
}
