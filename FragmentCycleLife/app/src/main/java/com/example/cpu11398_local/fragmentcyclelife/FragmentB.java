package com.example.cpu11398_local.fragmentcyclelife;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentB extends Fragment {

    static final String FRAGMENT_NAME = "CycleFragmentB";


    public FragmentB() {
        // Required empty public constructor
        Log.w(FRAGMENT_NAME, "constructor");
    }

    @Override
    public void onAttach(Context context) {
        Log.w(FRAGMENT_NAME, "onAttach");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null)
            Log.w(FRAGMENT_NAME, "onCreate");
        else

            Log.w(FRAGMENT_NAME, "onCreate " + savedInstanceState.toString());
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.w(FRAGMENT_NAME, "onCreateView");
        return inflater.inflate(R.layout.fragment_b, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.w(FRAGMENT_NAME, "onActivityCreate");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.w(FRAGMENT_NAME, "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.w(FRAGMENT_NAME, "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.w(FRAGMENT_NAME, "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.w(FRAGMENT_NAME, "onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.w(FRAGMENT_NAME, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.w(FRAGMENT_NAME, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.w(FRAGMENT_NAME, "onDetach");
        super.onDetach();
    }
}
