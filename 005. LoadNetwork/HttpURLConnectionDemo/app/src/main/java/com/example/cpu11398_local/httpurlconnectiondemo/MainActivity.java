package com.example.cpu11398_local.httpurlconnectiondemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    final String    URL_API = "https://api.stackexchange.com/2.2/search?order=desc&sort=activity&tagged=android&site=stackoverflow";
    ListView        lst_question;
    ListViewAdapter listViewAdapter;
    String ret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lst_question = findViewById(R.id.lst_question);

        try {
            sendGet();
            URL url = new URL(URL_API);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        httpURLConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String inputStreamToString(InputStream inputStream) {

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder result = new StringBuilder();
        String readLine = "";

        try {
            while ((readLine = bufferedReader.readLine()) != null) {
                result.append(readLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, ret, Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    private void sendGet() throws Exception {

        String url = "http://www.google.com/search?q=mkyong";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();

    }
}
