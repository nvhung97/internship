package com.example.cpu11398_local.etalk.utils;

/**
 * A container object which contain an event code and {@code @nullable} data of it.
 * It may be use to emit event to some listener.
 */

public class Event {

    /**
     * Shift flag bit to distinguish between views.
     */
    private static final int MODE_SHIFT = 28; //Maximum 16 different views.

    /**
     * Bellow commands for {@code ContentActivity}.
     */
    private static final int CONTENT_ACTIVITY_MARK              = 0 << MODE_SHIFT;
    public static final int CONTENT_ACTIVITY_SHOW_POPUP_MENU    = CONTENT_ACTIVITY_MARK | 0;

    /**
     * Bellow commands for {@code ChatActivity}.
     */
    private static final int CHAT_ACTIVITY_MARK = 1 << MODE_SHIFT;
    public static final int CHAT_ACTIVITY_BACK  = CHAT_ACTIVITY_MARK | 0;

    /**
     * Bellow commands for {@code LoginActivity}.
     */
    private static final int LOGIN_ACTIVITY_MARK            = 2 << MODE_SHIFT;
    public static final int LOGIN_ACTIVITY_BACK             = LOGIN_ACTIVITY_MARK | 0;
    public static final int LOGIN_ACTIVITY_FINISH_OK        = LOGIN_ACTIVITY_MARK | 1;
    public static final int LOGIN_ACTIVITY_FINISH_CANCELED  = LOGIN_ACTIVITY_MARK | 2;

    /**
     * Bellow commands for {@code RegisterActivity}.
     */
    private static final int REGISTER_ACTIVITY_MARK             = 3 << MODE_SHIFT;
    public static final int REGISTER_ACTIVITY_BACK              = REGISTER_ACTIVITY_MARK | 0;
    public static final int REGISTER_ACTIVITY_FINISH_OK         = LOGIN_ACTIVITY_MARK | 1;
    public static final int REGISTER_ACTIVITY_FINISH_CANCELED   = LOGIN_ACTIVITY_MARK | 2;

    private int         type;
    private Object[]    data;

    public static Event Create(int type) {
        return new Event(type, (Object)null);
    }

    public static Event Create(int type, Object... data) {
        return new Event(type, data);
    }

    private Event(int type, Object... data) {
        this.type = type;
        this.data = data;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setData(Object... data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public Object[] getData() {
        return data;
    }
}
