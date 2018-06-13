package com.example.cpu11398_local.retrofitdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import com.example.cpu11398_local.retrofitdemo.data.model.StackOverflowQuestions;
import com.example.cpu11398_local.retrofitdemo.data.remote.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ListView        lst_question;
    ListViewAdapter listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lst_question = findViewById(R.id.lst_question);

        ApiUtils
                .getRetrofitService()
                .loadQuestions("android")
                .enqueue(new Callback<StackOverflowQuestions>() {
                    @Override
                    public void onResponse(Call<StackOverflowQuestions> call, Response<StackOverflowQuestions> response) {
                        if (response.isSuccessful()) {
                            listViewAdapter = new ListViewAdapter(MainActivity.this, response.body().getQuestions());
                            lst_question.setAdapter(listViewAdapter);
                            listViewAdapter.notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<StackOverflowQuestions> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Load questions failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
