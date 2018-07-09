package com.example.cpu11398_local.cleanarchitecturedemo.presentation.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Hung-pc on 7/9/2018.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private boolean isCreating;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onDataBinding();
        isCreating = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        onSubscribeViewModel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        onUnSubscribeViewModel();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return onSaveViewModel();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        isCreating = true;
    }

    @Override
    protected void onDestroy() {
        if (!isCreating) {
            onEndTaskViewModel();
        }
        super.onDestroy();
    }

    abstract void onDataBinding();
    abstract void onSubscribeViewModel();
    abstract void onUnSubscribeViewModel();
    abstract Object onSaveViewModel();
    abstract void onEndTaskViewModel();
}
