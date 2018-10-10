package com.example.cpu11398_local.etalk.presentation.view.content.pager_page.messages;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.databinding.FragmentMessagesBinding;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.presentation.view.chat.group.ChatGroupActivity;
import com.example.cpu11398_local.etalk.presentation.view.chat.person.ChatPersonActivity;
import com.example.cpu11398_local.etalk.presentation.view.welcome.WelcomeActivity;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.content.MessageViewModel;
import com.example.cpu11398_local.etalk.utils.Event;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment {

    @Inject
    @Named("MessageViewModel")
    public ViewModel viewModel;

    private Disposable disposable;
    private ProgressBar progressBar;

    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        WelcomeActivity.getAppComponent(getContext()).inject(this);
        FragmentMessagesBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_messages,
                container,
                false
        );
        binding.setViewModel((MessageViewModel) viewModel);
        binding.messageFragmentRvMessage.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.messageFragmentRvMessage.addItemDecoration(
                new MessageDivider(
                        getContext().getDrawable(R.drawable.divider),
                        (int)getContext().getResources().getDimension(R.dimen.divider_padding_left)
                )
        );
        binding.messageFragmentRvMessage.setHasFixedSize(true);
        progressBar = binding.messageFragmentLoading;
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.subscribeObserver(new MessageObserver());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!disposable.isDisposed()){
            disposable.dispose();
        }
    }

    @Override
    public void onDestroy() {
        viewModel.endTask();
        super.onDestroy();
    }

    private class MessageObserver implements Observer<Event> {
        @Override
        public void onSubscribe(Disposable d) {
            disposable = d;
        }

        @Override
        public void onNext(Event event) {
            Object[] data = event.getData();
            switch (event.getType()) {
                case Event.MESSAGE_FRAGMENT_CHAT:
                    User user = (User)data[0];
                    Conversation conversation = (Conversation)data[1];
                    User friend = (User)data[2];
                    Intent intent = new Intent(getActivity(), ChatGroupActivity.class);
                    intent.putExtra("user", user.getUsername());
                    intent.putExtra("key", conversation.getKey());
                    intent.putExtra("type", conversation.getType());
                    if (conversation.getType() == Conversation.PERSON) {
                        intent.putExtra("name", friend.getName());
                        intent.putExtra("number", friend.getActive());
                    } else {
                        intent.putExtra("name", conversation.getName());
                        intent.putExtra("number", conversation.getMembers().size());
                    }
                    startActivity(intent);
                    break;
                case Event.MESSAGE_FRAGMENT_HIDE_PROGRESS_BAR:
                    progressBar.setVisibility(View.GONE);
                    break;
            }
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }
}
