package com.example.cpu11398_local.etalk.utils;

public final class FirebaseTree {


    public final class Database {

        public static final String NODE_NAME = "etalkchat";

        public final class Users {
            public static final String NODE_NAME = "users";
            public final class Key {
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
                }
            }
        }

        public final class Relationships {
            public static final String NODE_NAME = "relationships";
        }

        public final class Conversations {
            public static final String NODE_NAME = "conversations";
            public final class ConversationKey {
                public final class Key {
                    public static final String NODE_NAME = "key";
                }
                public final class Type {
                    public static final String NODE_NAME = "type";
                }
                public final class Name {
                    public static final String NODE_NAME = "name";
                }
                public final class Creator {
                    public static final String NODE_NAME = "creator";
                }
                public final class CreateTime {
                    public static final String NODE_NAME = "create_time";
                }
                public final class Members {
                    public static final String NODE_NAME = "members";
                }
                public final class LastMessage {
                    public static final String NODE_NAME = "last_message";
                }
            }
        }

        public final class Messages {
            public static final String NODE_NAME = "messages";
            public final class ConversationKey {
                public final class MessageKey {
                    public final class Key {
                        public static final String NODE_NAME = "key";
                    }
                    public final class Type {
                        public static final String NODE_NAME = "type";
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

    public final class Storage {

        public static final String NODE_NAME = "gs://etalkchat.appspot.com";

        public final class Avatars {
            public static final String NODE_NAME = "avatars";
            public final class Avatar {
                public static final String POSTFIX = ".png";
            }
        }

        public final class Conversations {
            public static final String NODE_NAME = "conversations";
            public final class Key {
                public final class Avatar {
                    public static final String NODE_NAME = "avatar";
                    public static final String POSTFIX = ".png";
                }
            }
        }
    }
}
