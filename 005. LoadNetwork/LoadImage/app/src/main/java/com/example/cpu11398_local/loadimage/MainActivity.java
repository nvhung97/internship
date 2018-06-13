package com.example.cpu11398_local.loadimage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

public class MainActivity extends AppCompatActivity {

    ImageView           img_picasso;
    ImageView           img_glide;
    ImageView           img_uimageloader;
    SimpleDraweeView    img_fresco;
    final String        URL = "https://i.pinimg.com/736x/be/0a/b7/be0ab7e1a7f2f5a319e190ec0bad1e31--cute-girls-vietnam.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(MainActivity.this); // Init Fresco view
        setContentView(R.layout.activity_main);

        img_picasso         = findViewById(R.id.img_picasso);
        img_glide           = findViewById(R.id.img_glide);
        img_uimageloader    = findViewById(R.id.img_uimageloader);
        img_fresco          = findViewById(R.id.img_fresco);

        Picasso
                .get()
                .load(URL)
                .into(img_picasso);

        Glide
                .with(MainActivity.this)
                .load(URL)
                .into(img_glide);

        ImageLoader
                .getInstance()
                .init(ImageLoaderConfiguration.createDefault(MainActivity.this));
        ImageLoader
                .getInstance()
                .displayImage(URL, img_uimageloader);

        img_fresco.setImageURI(URL);

    }
}