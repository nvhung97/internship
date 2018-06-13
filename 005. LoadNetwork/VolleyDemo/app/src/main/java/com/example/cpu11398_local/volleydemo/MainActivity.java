package com.example.cpu11398_local.volleydemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cpu11398_local.volleydemo.model.StackOverflowQuestions;
import com.google.gson.Gson;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    final String    URL_API = "https://api.stackexchange.com/2.2/search?order=desc&sort=activity&tagged=android&site=stackoverflow";
    ListView        lst_question;
    ListViewAdapter listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lst_question = findViewById(R.id.lst_question);

        Volley
                .newRequestQueue(this)
                .add(
                        new JsonObjectRequest(
                                Method.GET,
                                URL_API,
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        listViewAdapter = new ListViewAdapter(
                                                MainActivity.this,
                                                new Gson().fromJson(response.toString(), StackOverflowQuestions.class).getQuestions()
                                        );
                                        lst_question.setAdapter(listViewAdapter);
                                        listViewAdapter.notifyDataSetChanged();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(
                                                MainActivity.this,
                                                error.getMessage(),
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    }
                                }
                        )
                );
    }
}
