package com.example.cpu11398_local.fragmentcyclelife;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentA extends Fragment {

    static final String FRAGMENT_NAME = "CycleFragmentA";

    public FragmentA() {
        // Required empty public constructor
        Log.w(FRAGMENT_NAME, "constructor " + this.toString());
    }

    @Override
    public void onAttach(Context context) {
        Log.w(FRAGMENT_NAME, "onAttach begin");
        super.onAttach(context);
        Log.w(FRAGMENT_NAME, "onAttach finish");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null){
            Log.w(FRAGMENT_NAME, "onCreate begin " + savedInstanceState);
        }
        else {
            Log.w(FRAGMENT_NAME, "onCreate begin");
        }
        super.onCreate(savedInstanceState);
        Log.w(FRAGMENT_NAME, "onCreate finish");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.w(FRAGMENT_NAME, "onCreateView begin");
        View view = inflater.inflate(R.layout.fragment_a, container, false);
        view.findViewById(R.id.txt_fragment_a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w(FRAGMENT_NAME, "change to fragment B");
                FragmentB fragmentB = new FragmentB();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_a, fragmentB);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        Log.w(FRAGMENT_NAME, "onCreateView finish");
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.w(FRAGMENT_NAME, "onSaveInstanceState begin");
        super.onSaveInstanceState(outState);
        Log.w(FRAGMENT_NAME, "onSaveInstanceState finish");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.w(FRAGMENT_NAME, "onActivityCreate begin");
        super.onActivityCreated(savedInstanceState);
        Log.w(FRAGMENT_NAME, "onActivityCreate finish");
    }

    @Override
    public void onStart() {
        Log.w(FRAGMENT_NAME, "onStart begin");
        super.onStart();
        Log.w(FRAGMENT_NAME, "onStart finish");
    }

    @Override
    public void onResume() {
        Log.w(FRAGMENT_NAME, "onResume begin");
        super.onResume();
        Log.w(FRAGMENT_NAME, "onResume finish");
    }

    @Override
    public void onPause() {
        Log.w(FRAGMENT_NAME, "onPause begin");
        super.onPause();
        Log.w(FRAGMENT_NAME, "onPause finish");
    }

    @Override
    public void onStop() {
        Log.w(FRAGMENT_NAME, "onStop begin");
        super.onStop();
        Log.w(FRAGMENT_NAME, "onStop finish");
    }

    @Override
    public void onDestroyView() {
        Log.w(FRAGMENT_NAME, "onDestroyView begin");
        super.onDestroyView();
        Log.w(FRAGMENT_NAME, "onDestroyView finish");
    }

    @Override
    public void onDestroy() {
        Log.w(FRAGMENT_NAME, "onDestroy begin");
        super.onDestroy();
        Log.w(FRAGMENT_NAME, "onDestroy finish");
    }

    @Override
    public void onDetach() {
        Log.w(FRAGMENT_NAME, "onDetach begin");
        super.onDetach();
        Log.w(FRAGMENT_NAME, "onDetach finish");
    }
}
