package com.example.cpu11398_local.retrofitdemo.data.remote;

import com.example.cpu11398_local.retrofitdemo.data.model.StackOverflowQuestions;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("/2.2/search?order=desc&sort=activity&site=stackoverflow")
    Call<StackOverflowQuestions> loadQuestions(@Query("tagged") String tags);
}
