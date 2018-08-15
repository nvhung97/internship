package com.example.cpu11398_local.viewpager;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "ViewPager_MainActivity";
    ViewPager pager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        pager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        Log.w(TAG, "new PagerAdapter");
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        Log.w(TAG, "set pager to adapter");
        pager.setAdapter(adapter);
        Log.w(TAG, "set off screen page limit");
        pager.setOffscreenPageLimit(1);
        Log.w(TAG, "setup tabLayout with pager");
        tabLayout.setupWithViewPager(pager);
        pager.setCurrentItem();
    }
}
