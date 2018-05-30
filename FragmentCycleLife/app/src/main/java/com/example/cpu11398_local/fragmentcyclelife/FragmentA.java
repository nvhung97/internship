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
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentA extends Fragment {

    static final String FRAGMENT_NAME = "CycleFragmentA";

    TextView txt_fragment_a;

    public FragmentA() {
        // Required empty public constructor
        Log.w(FRAGMENT_NAME, "constructor " + this.toString());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.w(FRAGMENT_NAME, "onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Log.w(FRAGMENT_NAME, "onCreate");
        } else {
            Log.w(FRAGMENT_NAME, "onCreate " + savedInstanceState.toString());
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_a, container, false);
        Log.w(FRAGMENT_NAME, "onCreateView");
        txt_fragment_a = view.findViewById(R.id.txt_fragment_a);
        txt_fragment_a.setOnClickListener(new View.OnClickListener() {
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
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.w(FRAGMENT_NAME, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
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
