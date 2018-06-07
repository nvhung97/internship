package com.example.cpu11398_local.loadnetwork;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Button      button;
    ImageView   imageView;
    String      urlImage = "https://i.pinimg.com/736x/be/0a/b7/be0ab7e1a7f2f5a319e190ec0bad1e31--cute-girls-vietnam.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button      = findViewById(R.id.btn);
        imageView   = findViewById(R.id.img);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Picasso
                        .get()
                        .load(urlImage)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(imageView);*/
                Glide
                        .with(MainActivity.this)
                        .load(urlImage)
                        .into(imageView);
            }
        });
    }
}
