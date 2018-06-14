package com.example.cpu11398_local.httpurlconnectiondemo;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import com.example.cpu11398_local.httpurlconnectiondemo.model.StackOverflowQuestions;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    final String    URL_API = "https://api.stackexchange.com/2.2/search?order=desc&sort=activity&tagged=android&site=stackoverflow";
    ListView        lst_question;
    ListViewAdapter listViewAdapter;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lst_question = findViewById(R.id.lst_question);

        new AsyncTask<String, Void, String> () {

            @Override
            protected String doInBackground(String... urls) {

                String result = null;

                try {
                    URL url = new URL(urls[0]);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    int responseCode = httpURLConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        result = inputStreamToString(httpURLConnection.getInputStream());
                    }
                    else {
                        throw new Exception("HttpResponseCode = " + responseCode);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                listViewAdapter = new ListViewAdapter(
                  MainActivity.this,
                  new Gson().fromJson(result, StackOverflowQuestions.class).getQuestions()
                );
                lst_question.setAdapter(listViewAdapter);
                listViewAdapter.notifyDataSetChanged();
            }
        }.execute(URL_API);

    }

    private String inputStreamToString(InputStream inputStream) {

        BufferedReader  bufferedReader  = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder   result          = new StringBuilder();

        try {
            String readLine;
            while ((readLine = bufferedReader.readLine()) != null) {
                result.append(readLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }
}
