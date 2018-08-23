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
     * Used when need to return an event but do nothing or default action.
     */
    public static final int NONE = 0x7fffffff;

    /**
     * Bellow commands for {@code ContentActivity}.
     */
    private static final int CONTENT_ACTIVITY_MARK              = 0 << MODE_SHIFT;
    public static final int CONTENT_ACTIVITY_SHOW_POPUP_MENU    = CONTENT_ACTIVITY_MARK | 0;
    public static final int CONTENT_ACTIVITY_SHOW_POPUP_SETTING = CONTENT_ACTIVITY_MARK | 1;
    /*public static final int CONTENT_ACTIVITY_SHOW_LOADING       = CONTENT_ACTIVITY_MARK | 2;
    public static final int CONTENT_ACTIVITY_HIDE_LOADING       = CONTENT_ACTIVITY_MARK | 3;*/
    public static final int CONTENT_ACTIVITY_LOGOUT             = CONTENT_ACTIVITY_MARK | 4;

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
    public static final int LOGIN_ACTIVITY_SHOW_LOADING     = LOGIN_ACTIVITY_MARK | 3;
    public static final int LOGIN_ACTIVITY_HIDE_LOADING     = LOGIN_ACTIVITY_MARK | 4;
    public static final int LOGIN_ACTIVITY_LOGIN_FAILED     = LOGIN_ACTIVITY_MARK | 5;
    public static final int LOGIN_ACTIVITY_TIMEOUT          = LOGIN_ACTIVITY_MARK | 6;

    /**
     * Bellow commands for {@code RegisterActivity}.
     */
    private static final int REGISTER_ACTIVITY_MARK             = 3 << MODE_SHIFT;
    public static final int REGISTER_ACTIVITY_BACK              = REGISTER_ACTIVITY_MARK | 0;
    public static final int REGISTER_ACTIVITY_FINISH_OK         = REGISTER_ACTIVITY_MARK | 1;
    public static final int REGISTER_ACTIVITY_FINISH_CANCELED   = REGISTER_ACTIVITY_MARK | 2;
    public static final int REGISTER_ACTIVITY_SHOW_LOADING      = REGISTER_ACTIVITY_MARK | 3;
    public static final int REGISTER_ACTIVITY_HIDE_LOADING      = REGISTER_ACTIVITY_MARK | 4;
    public static final int REGISTER_ACTIVITY_REGISTER_FAILED   = REGISTER_ACTIVITY_MARK | 5;
    public static final int REGISTER_ACTIVITY_TIMEOUT           = REGISTER_ACTIVITY_MARK | 6;

    /**
     * Bellow commands for {@code WelcomeActivity}.
     */
    private static final int WELCOME_ACTIVITY_MARK          = 4 << MODE_SHIFT;
    public static final int WELCOME_ACTIVITY_SHOW_LOADING   = WELCOME_ACTIVITY_MARK | 0;
    public static final int WELCOME_ACTIVITY_HIDE_LOADING   = WELCOME_ACTIVITY_MARK | 1;
    public static final int WELCOME_ACTIVITY_USER_LOGGED_IN = WELCOME_ACTIVITY_MARK | 2;


    /***********************************************************************************************
     * Class implementation.
     **********************************************************************************************/

    private int         type;
    private Object[]    data;

    public static Event create(int type) {
        return new Event(type, (Object)null);
    }

    public static Event create(int type, Object... data) {
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
