package com.example.cpu11398_local.etalk.presentation.view.content.pager_page.messages;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.databinding.FragmentMessagesBinding;
import com.example.cpu11398_local.etalk.utils.Tool;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment {
    FragmentMessagesBinding fragmentMessagesBinding;

    public MessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         fragmentMessagesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_messages, container, false);
        //View view = inflater.inflate(R.layout.fragment_messages, container, false);
        if (savedInstanceState != null) {
            Log.e("Test", "1 "+ savedInstanceState.get("viewModel"));
        } else {
            fragmentMessagesBinding.setViewModel(100);
            Log.e("Test", "2 "+ savedInstanceState);
        }
        View view = fragmentMessagesBinding.getRoot();
        return view;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("Test", "save" + fragmentMessagesBinding.getViewModel());
    }
}
