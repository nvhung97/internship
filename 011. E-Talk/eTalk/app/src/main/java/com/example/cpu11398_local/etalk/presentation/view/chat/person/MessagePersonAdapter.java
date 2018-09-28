package com.example.cpu11398_local.etalk.presentation.view.chat.person;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.presentation.custom.AvatarImageView;
import com.example.cpu11398_local.etalk.presentation.model.Message;
import java.util.List;
import java.util.concurrent.Callable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MessagePersonAdapter extends RecyclerView.Adapter<MessagePersonAdapter.MessagePersonViewHolder> {

    private final int ME_TEXT       = 0;
    private final int FRIEND_TEXT   = 5;

    private List<MessagePersonItem> messages;

    public abstract class MessagePersonViewHolder extends RecyclerView.ViewHolder {
        public MessagePersonViewHolder(View view) {
            super(view);
        }
        public abstract void bindView(MessagePersonItem item);
    }

    public class MessageTextMeViewHolder extends MessagePersonViewHolder {
        public ConstraintLayout content;
        public TextView         data;
        public TextView         time;
        public AvatarImageView  avatar;
        public MessageTextMeViewHolder(View view) {
            super(view);
            content = view.findViewById(R.id.lyt_message_person_text_me_content);
            data    = view.findViewById(R.id.lyt_message_person_text_me_data);
            time    = view.findViewById(R.id.lyt_message_person_text_me_time);
            avatar  = view.findViewById(R.id.lyt_message_person_text_me_status);
            content.setOnClickListener(v ->
                time.setVisibility(
                        time.getVisibility() == View.VISIBLE
                        ? View.GONE
                        : View.VISIBLE
                )
            );
        }
        @Override
        public void bindView(MessagePersonItem item) {
            data.setText(item.getTextData());
            time.setText(item.getTime());
            time.setVisibility(item.getTimeVisible());
            avatar.setImageFromObject(item.getAvatar());
            avatar.setVisibility(item.getAvatarVisible());
        }
    }

    public class MessageTextFriendViewHolder extends MessagePersonViewHolder {
        public ConstraintLayout content;
        public TextView         data;
        public TextView         time;
        public AvatarImageView  avatar;
        public MessageTextFriendViewHolder(View view) {
            super(view);
            content = view.findViewById(R.id.lyt_message_person_text_friend_content);
            data    = view.findViewById(R.id.lyt_message_person_text_friend_data);
            time    = view.findViewById(R.id.lyt_message_person_text_friend_time);
            avatar  = view.findViewById(R.id.lyt_message_person_text_friend_avatar);
            content.setOnClickListener(v ->
                    time.setVisibility(
                            time.getVisibility() == View.VISIBLE
                                    ? View.GONE
                                    : View.VISIBLE
                    )
            );
        }
        @Override
        public void bindView(MessagePersonItem item) {
            data.setText(item.getTextData());
            time.setText(item.getTime());
            time.setVisibility(item.getTimeVisible());
            avatar.setImageFromObject(item.getAvatar());
            avatar.setVisibility(item.getAvatarVisible());
        }
    }

    public MessagePersonAdapter(List<MessagePersonItem> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        MessagePersonItem item      = messages.get(position);
        Message           message   = item.getMessage();
        if (item.isMe()) {
            if (message.getType() == Message.TEXT) {
                return ME_TEXT;
            }
        } else {
            if (message.getType() == Message.TEXT) {
                return FRIEND_TEXT;
            }
        }
        return 0;
    }

    @NonNull
    @Override
    public MessagePersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ME_TEXT:
                return new MessageTextMeViewHolder(
                        LayoutInflater
                                .from(parent.getContext())
                                .inflate(R.layout.lyt_message_person_text_me, parent, false)
                );
            case FRIEND_TEXT:
                return new MessageTextFriendViewHolder(
                        LayoutInflater
                                .from(parent.getContext())
                                .inflate(R.layout.lyt_message_person_text_friend, parent, false)
                );
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MessagePersonViewHolder holder, int position) {
        holder.bindView(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @SuppressLint("CheckResult")
    public void onNewData(List<MessagePersonItem> messages, Callable<Void> func) {
        Single
                .just(DiffUtil.calculateDiff(
                        new MessagePersonDiffUtil(
                                this.messages,
                                messages
                        )
                ))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(diffResult -> {
                    this.messages = messages;
                    diffResult.dispatchUpdatesTo(this);
                    func.call();
                });
    }
}
