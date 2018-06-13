package com.example.cpu11398_local.retrofitdemo.data.remote;

public class ApiUtils {

    public static final String BASE_URL = "https://api.stackexchange.com";

    public static RetrofitService getRetrofitService() {
        return RetrofitClient.getClient(BASE_URL).create(RetrofitService.class);
    }
}
