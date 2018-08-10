package com.example.cpu11398_local.etalk.utils;

/**
 * A container object which contain an event code and {@code @nullable} data of it.
 * It may be use to emit event to some listener.
 */

public class Event {

    private int     type;
    private Object  data;

    public Event(int type) {
        this(type, null);
    }

    public Event(int type, Object data) {
        this.type = type;
        this.data = data;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public Object getData() {
        return data;
    }
}
