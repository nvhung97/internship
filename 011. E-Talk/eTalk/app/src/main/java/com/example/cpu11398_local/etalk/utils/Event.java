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
    private static final int CONTENT_ACTIVITY_MARK                      = 0 << MODE_SHIFT;
    public static final int CONTENT_ACTIVITY_SHOW_POPUP_MENU            = CONTENT_ACTIVITY_MARK | 0;
    public static final int CONTENT_ACTIVITY_MENU_ADD_FRIEND            = CONTENT_ACTIVITY_MARK | 1;
    public static final int CONTENT_ACTIVITY_MENU_CREATE_GROUP          = CONTENT_ACTIVITY_MARK | 2;
    public static final int CONTENT_ACTIVITY_LOGOUT                     = CONTENT_ACTIVITY_MARK | 3;
    public static final int CONTENT_ACTIVITY_EMIT_DATA                  = CONTENT_ACTIVITY_MARK | 4;

    /**
     * Bellow commands for {@code ChatPersonActivity} and {@code ChatGroupActivity}.
     */
    private static final int CHAT_ACTIVITY_MARK             = 1 << MODE_SHIFT;
    public static final int CHAT_ACTIVITY_BACK              = CHAT_ACTIVITY_MARK | 0;
    public static final int CHAT_ACTIVITY_HELPER            = CHAT_ACTIVITY_MARK | 1;
    public static final int CHAT_ACTIVITY_VALUE             = CHAT_ACTIVITY_MARK | 2;
    public static final int CHAT_ACTIVITY_FRIEND            = CHAT_ACTIVITY_MARK | 3;
    public static final int CHAT_ACTIVITY_MESSAGES          = CHAT_ACTIVITY_MARK | 4;
    public static final int CHAT_ACTIVITY_GOTO_LAST         = CHAT_ACTIVITY_MARK | 5;
    public static final int CHAT_ACTIVITY_GET_MEDIA         = CHAT_ACTIVITY_MARK | 6;
    public static final int CHAT_ACTIVITY_SEND_IMAGE_URI    = CHAT_ACTIVITY_MARK | 7;
    public static final int CHAT_ACTIVITY_ATTACH            = CHAT_ACTIVITY_MARK | 8;
    public static final int CHAT_ACTIVITY_SEND_FILE         = CHAT_ACTIVITY_MARK | 9;
    public static final int CHAT_ACTIVITY_START_DOWNLOAD    = CHAT_ACTIVITY_MARK | 10;
    public static final int CHAT_ACTIVITY_STOP_DOWNLOAD     = CHAT_ACTIVITY_MARK | 11;
    public static final int CHAT_ACTIVITY_DOWNLOAD_PROGRESS = CHAT_ACTIVITY_MARK | 12;
    public static final int CHAT_ACTIVITY_DOWNLOAD_OK       = CHAT_ACTIVITY_MARK | 13;
    public static final int CHAT_ACTIVITY_DOWNLOAD_FAILED   = CHAT_ACTIVITY_MARK | 14;
    public static final int CHAT_ACTIVITY_SHOW_POPUP_MENU   = CHAT_ACTIVITY_MARK | 15;
    public static final int CHAT_ACTIVITY_SHOW_MAP          = CHAT_ACTIVITY_MARK | 16;
    public static final int CHAT_ACTIVITY_SEND_LOCATION     = CHAT_ACTIVITY_MARK | 17;
    public static final int CHAT_ACTIVITY_EMOTICON          = CHAT_ACTIVITY_MARK | 18;
    public static final int CHAT_ACTIVITY_AUDIO             = CHAT_ACTIVITY_MARK | 19;
    public static final int CHAT_ACTIVITY_AUDIO_RECORDING   = CHAT_ACTIVITY_MARK | 20;
    public static final int CHAT_ACTIVITY_AUDIO_COMPLETE    = CHAT_ACTIVITY_MARK | 21;
    public static final int CHAT_ACTIVITY_AUDIO_RESET       = CHAT_ACTIVITY_MARK | 22;
    public static final int CHAT_ACTIVITY_START_PLAY        = CHAT_ACTIVITY_MARK | 23;
    public static final int CHAT_ACTIVITY_STOP_PLAY         = CHAT_ACTIVITY_MARK | 24;
    public static final int CHAT_ACTIVITY_SEND_VIDEO_URI    = CHAT_ACTIVITY_MARK | 25;

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

    /**
     * Bellow commands for {@code MoreFragment}.
     */
    private static final int MORE_FRAGMENT_MARK             = 5 << MODE_SHIFT;
    public static final int MORE_FRAGMENT_MY_PRORILE        = MORE_FRAGMENT_MARK | 0;
    public static final int MORE_FRAGMENT_LOGOUT            = MORE_FRAGMENT_MARK | 1;
    public static final int MORE_FRAGMENT_HIDE_PROGRESS_BAR = MORE_FRAGMENT_MARK | 2;

    /**
     * Bellow commands for {@code Profile acticity}.
     */
    private static final int PROFILE_ACTIVITY_MARK              = 6 << MODE_SHIFT;
    public static final int PROFILE_ACTIVITY_SHOW_LOADING       = PROFILE_ACTIVITY_MARK | 0;
    public static final int PROFILE_ACTIVITY_HIDE_LOADING       = PROFILE_ACTIVITY_MARK | 1;
    public static final int PROFILE_ACTIVITY_SHOW_IMAGE_OPTION  = PROFILE_ACTIVITY_MARK | 2;
    public static final int PROFILE_ACTIVITY_BACK               = PROFILE_ACTIVITY_MARK | 3;
    public static final int PROFILE_ACTIVITY_TIME_OUT           = PROFILE_ACTIVITY_MARK | 4;
    public static final int PROFILE_ACTIVITY_UPDATE_OK          = PROFILE_ACTIVITY_MARK | 5;
    public static final int PROFILE_ACTIVITY_UPDATE_FAILED      = PROFILE_ACTIVITY_MARK | 6;

    /**
     * Bellow commands for {@code AddFriendActivity}.
     */
    private static final int ADD_FRIEND_ACTIVITY_MARK           = 7 << MODE_SHIFT;
    public static final int ADD_FRIEND_ACTIVITY_BACK            = ADD_FRIEND_ACTIVITY_MARK | 0;
    public static final int ADD_FRIEND_ACTIVITY_SHOW_LOADING    = ADD_FRIEND_ACTIVITY_MARK | 1;
    public static final int ADD_FRIEND_ACTIVITY_HIDE_LOADING    = ADD_FRIEND_ACTIVITY_MARK | 2;
    public static final int ADD_FRIEND_ACTIVITY_NOT_FOUND       = ADD_FRIEND_ACTIVITY_MARK | 3;
    public static final int ADD_FRIEND_ACTIVITY_TIMEOUT         = ADD_FRIEND_ACTIVITY_MARK | 4;

    /**
     * Bellow commands for {@code CreateGroupActivity}.
     */
    private static final int CREATE_GROUP_ACTIVITY_MARK             = 8 << MODE_SHIFT;
    public static final int CREATE_GROUP_ACTIVITY_BACK              = CREATE_GROUP_ACTIVITY_MARK | 0;
    public static final int CREATE_GROUP_ACTIVITY_SHOW_LOADING      = CREATE_GROUP_ACTIVITY_MARK | 1;
    public static final int CREATE_GROUP_ACTIVITY_HIDE_LOADING      = CREATE_GROUP_ACTIVITY_MARK | 2;
    public static final int CREATE_GROUP_ACTIVITY_SHOW_IMAGE_OPTION = CREATE_GROUP_ACTIVITY_MARK | 3;
    public static final int CREATE_GROUP_ACTIVITY_CREATE_OK         = CREATE_GROUP_ACTIVITY_MARK | 4;
    public static final int CREATE_GROUP_ACTIVITY_CREATE_FAILED     = CREATE_GROUP_ACTIVITY_MARK | 5;
    public static final int CREATE_GROUP_ACTIVITY_TIMEOUT           = CREATE_GROUP_ACTIVITY_MARK | 6;

    /**
     * Bellow commands for {@code ContactFragment}.
     */
    private static final int CONTACT_FRAGMENT_MARK              = 9 << MODE_SHIFT;
    public static final int CONTACT_FRAGMENT_CHAT               = CONTACT_FRAGMENT_MARK | 0;
    public static final int CONTACT_FRAGMENT_HIDE_PROGRESS_BAR  = CONTACT_FRAGMENT_MARK | 1;

    /**
     * Bellow commands for {@code GroupFragment}.
     */
    private static final int GROUP_FRAGMENT_MARK                = 10 << MODE_SHIFT;
    public static final int GROUP_FRAGMENT_CHAT                 = GROUP_FRAGMENT_MARK | 0;
    public static final int GROUP_FRAGMENT_HIDE_PROGRESS_BAR    = GROUP_FRAGMENT_MARK | 1;

    /**
     * Bellow commands for {@code MessageFragment}.
     */
    private static final int MESSAGE_FRAGMENT_MARK                = 11 << MODE_SHIFT;
    public static final int MESSAGE_FRAGMENT_CHAT                 = MESSAGE_FRAGMENT_MARK | 0;
    public static final int MESSAGE_FRAGMENT_HIDE_PROGRESS_BAR    = MESSAGE_FRAGMENT_MARK | 1;


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
