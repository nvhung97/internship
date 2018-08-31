package com.example.cpu11398_local.etalk.utils;

public final class FirebaseTree {

    public static final String NODE_NAME = "etalkchat";

    public final class Users {
        public static final String NODE_NAME = "users";
        public final class Name {
            public static final String NODE_NAME = "name";
        }
        public final class Username {
            public static final String NODE_NAME = "username";
        }
        public final class Password {
            public static final String NODE_NAME = "password";
        }
        public final class Phone {
            public static final String NODE_NAME = "phone";
        }
        public final class Active {
            public static final String NODE_NAME = "active";
        }
        public final class Avatar {
            public static final String NODE_NAME = "avatar";
            public static final String PREFIX    = "avatar_";
            public static final String POSTFIX   = ".png";
        }
        public final class Friends {
            public static final String NODE_NAME = "friends";
        }
        public final class Conversations {
            public static final String NODE_NAME = "conversations";
        }
    }

    public final class Conversations {
        public static final String NODE_NAME = "conversations";
        public final class ConversationKey {
            public final class Key {
                public static final String NODE_NAME = "key";
            }
            public final class Type {
                public static final String NODE_NAME = "type";
                public static final String GROUP = "GROUP";
                public static final String PERSON = "PERSON";
            }
            public final class Name {
                public static final String NODE_NAME = "name";
            }
            public final class Members {
                public static final String NODE_NAME = "members";
            }
            public final class Messages {
                public static final String NODE_NAME = "messages";
            }
            public final class Last {
                public static final String NODE_NAME = "last";
            }
        }
    }

    public final class Messages {
        public static final String NODE_NAME = "messages";
        public final class MessagesKey {
            public final class MessageKey {
                public final class Key {
                    public static final String NODE_NAME = "key";
                }
                public final class Data {
                    public static final String NODE_NAME = "data";
                }
                public final class Time {
                    public static final String NODE_NAME = "time";
                }
                public final class Sender {
                    public static final String NODE_NAME = "sender";
                }
            }
        }
    }
}
