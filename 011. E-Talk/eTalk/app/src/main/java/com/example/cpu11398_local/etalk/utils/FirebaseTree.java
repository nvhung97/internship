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
    }
}
