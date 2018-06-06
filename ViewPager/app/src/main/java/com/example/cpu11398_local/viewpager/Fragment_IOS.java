package com.example.cpu11398_local.viewpager;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_IOS extends Fragment {

    private final String TAG = "ViewPager_Frgmt____IOS";

    public Fragment_IOS() {
        Log.w(TAG, "constructor " + this.toString());
    }

    @Override
    public void onAttach(Context context) {
        Log.w(TAG, "onAttach");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null){
            Log.w(TAG, "onCreate " + savedInstanceState);
        }
        else {
            Log.w(TAG, "onCreate");
        }
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.w(TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment__ios, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.w(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.w(TAG, "onActivityCreate");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.w(TAG, "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.w(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.w(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.w(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.w(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.w(TAG, "onDestroy");
        super.onDestroy();
    }
}
