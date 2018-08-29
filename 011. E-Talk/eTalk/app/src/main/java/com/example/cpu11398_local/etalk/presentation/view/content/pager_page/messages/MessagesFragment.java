package com.example.cpu11398_local.etalk.presentation.view.content.pager_page.messages;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.databinding.FragmentMessagesBinding;

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
        View view = fragmentMessagesBinding.getRoot();
        return view;
    }
}
