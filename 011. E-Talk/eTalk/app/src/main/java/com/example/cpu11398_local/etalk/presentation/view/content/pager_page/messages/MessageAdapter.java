package com.example.cpu11398_local.etalk.presentation.view.content.pager_page.messages;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.presentation.custom.AvatarImageView;
import com.example.cpu11398_local.etalk.presentation.custom.ShortMessage;
import com.example.cpu11398_local.etalk.presentation.custom.TimeTextView;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.presentation.view_model.content.MessageViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final int PERSON                = 0;
    private final int GROUP_SINGLE_AVATAR   = 1;
    private final int GROUP_MULTIPLE_AVATAR = 2;

    private User                            currentUser;
    private List<Conversation>              conversations;
    private Map<String, User>               friends;
    private MessageViewModel.ActionCallback actionCallback;

    public abstract class MessageViewHolder extends RecyclerView.ViewHolder {
        public MessageViewHolder(View view) {
            super(view);
        }
        public abstract void bindView(Conversation conversation);
    }

    public class PersonViewHolder extends MessageViewHolder {
        public FrameLayout      row;
        public AvatarImageView  avatar;
        public TextView         name;
        public ShortMessage     data;
        public TimeTextView     time;
        public PersonViewHolder(View itemView) {
            super(itemView);
            row     = itemView.findViewById(R.id.lyt_row_message_person);
            avatar  = itemView.findViewById(R.id.lyt_row_message_person_avatar);
            name    = itemView.findViewById(R.id.lyt_row_message_person_name);
            data    = itemView.findViewById(R.id.lyt_row_message_person_message);
            time    = itemView.findViewById(R.id.lyt_row_message_person_time);
        }
        @Override
        public void bindView(Conversation conversation) {
            for (String key : conversation.getMembers().keySet()) {
                if (!key.equals(currentUser.getUsername())) {
                    User friend = friends.get(key);
                    if (friend != null) {
                        avatar.setImageFromObject(friend.getAvatar());
                        name.setText(friend.getName());
                        row.setOnClickListener(v -> actionCallback.chatWith(conversation, friend));
                    }
                    break;
                }
            }
            data.setData(conversation.getLastMessage().getData());
            time.setTime(conversation.getLastMessage().getTime());
            if (conversation.getMembers().get(currentUser.getUsername()) < conversation.getLastMessage().getTime()) {
                name.setTypeface(null, Typeface.BOLD);
                data.setTypeface(null, Typeface.BOLD);
                time.setTypeface(null, Typeface.BOLD);
            } else {
                name.setTypeface(null, Typeface.NORMAL);
                data.setTypeface(null, Typeface.NORMAL);
                time.setTypeface(null, Typeface.NORMAL);
            }
        }
    }

    public class GroupSingleAvatarViewHolder extends MessageViewHolder {
        public FrameLayout      row;
        public AvatarImageView  avatar;
        public TextView         name;
        public ShortMessage     data;
        public TimeTextView     time;
        public GroupSingleAvatarViewHolder(View itemView) {
            super(itemView);
            row     = itemView.findViewById(R.id.lyt_row_message_person);
            avatar  = itemView.findViewById(R.id.lyt_row_message_person_avatar);
            name    = itemView.findViewById(R.id.lyt_row_message_person_name);
            data    = itemView.findViewById(R.id.lyt_row_message_person_message);
            time    = itemView.findViewById(R.id.lyt_row_message_person_time);
        }
        @Override
        public void bindView(Conversation conversation) {
            row.setOnClickListener(v -> actionCallback.chatWith(conversation, null));
            avatar.setImageFromObject(conversation.getAvatar());
            name.setText(conversation.getName());
            data.setData(conversation.getLastMessage().getData());
            time.setTime(conversation.getLastMessage().getTime());
            if (conversation.getMembers().get(currentUser.getUsername()) < conversation.getLastMessage().getTime()) {
                name.setTypeface(null, Typeface.BOLD);
                data.setTypeface(null, Typeface.BOLD);
                time.setTypeface(null, Typeface.BOLD);
            } else {
                name.setTypeface(null, Typeface.NORMAL);
                data.setTypeface(null, Typeface.NORMAL);
                time.setTypeface(null, Typeface.NORMAL);
            }
        }
    }

    public class GroupMultipleAvatarViewHolder extends MessageViewHolder {
        public FrameLayout      row;
        public AvatarImageView  avatar1;
        public AvatarImageView  avatar2;
        public AvatarImageView  avatar3;
        public TextView         number;
        public TextView         name;
        public ShortMessage     data;
        public TimeTextView     time;
        public GroupMultipleAvatarViewHolder(View itemView) {
            super(itemView);
            row     = itemView.findViewById(R.id.lyt_row_message_group);
            avatar1 = itemView.findViewById(R.id.lyt_row_message_group_avatar1);
            avatar2 = itemView.findViewById(R.id.lyt_row_message_group_avatar2);
            avatar3 = itemView.findViewById(R.id.lyt_row_message_group_avatar3);
            number  = itemView.findViewById(R.id.lyt_row_message_group_number);
            name    = itemView.findViewById(R.id.lyt_row_message_group_name);
            data    = itemView.findViewById(R.id.lyt_row_message_group_message);
            time    = itemView.findViewById(R.id.lyt_row_message_group_time);
        }
        @Override
        public void bindView(Conversation conversation) {
            row.setOnClickListener(v -> actionCallback.chatWith(conversation, null));
            name.setText(conversation.getName());
            data.setData(conversation.getLastMessage().getData());
            time.setTime(conversation.getLastMessage().getTime());
            number.setText(String.valueOf(conversation.getMembers().size()));
            List<String> keyFriends = new ArrayList<>(conversation.getMembers().keySet());
            if (friends.get(keyFriends.get(0)) != null) {
                avatar1.setImageFromObject(friends.get(keyFriends.get(0)).getAvatar());
            }
            if (friends.get(keyFriends.get(1)) != null) {
                avatar2.setImageFromObject(friends.get(keyFriends.get(1)).getAvatar());
            }
            if (friends.get(keyFriends.get(2)) != null) {
                avatar3.setImageFromObject(friends.get(keyFriends.get(2)).getAvatar());
            }
            if (conversation.getMembers().get(currentUser.getUsername()) < conversation.getLastMessage().getTime()) {
                name.setTypeface(null, Typeface.BOLD);
                data.setTypeface(null, Typeface.BOLD);
                time.setTypeface(null, Typeface.BOLD);
            } else {
                name.setTypeface(null, Typeface.NORMAL);
                data.setTypeface(null, Typeface.NORMAL);
                time.setTypeface(null, Typeface.NORMAL);
            }
        }
    }

    public MessageAdapter(User currentUser,
                          List<Conversation> conversations,
                          Map<String, User> friends,
                          MessageViewModel.ActionCallback actionCallback) {
        this.currentUser    = currentUser;
        this.conversations  = conversations;
        this.friends        = friends;
        this.actionCallback = actionCallback;
    }

    @Override
    public int getItemViewType(int position) {
        if (conversations.get(position).getType() == Conversation.PERSON) {
            return PERSON;
        }
        if (conversations.get(position).getAvatar() != null) {
            return GROUP_SINGLE_AVATAR;
        }
        return GROUP_MULTIPLE_AVATAR;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == PERSON) {
            return new PersonViewHolder(
                    LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.lyt_row_person_chat, parent, false)
            );
        }
        if (viewType == GROUP_SINGLE_AVATAR) {
            return new GroupSingleAvatarViewHolder(
                    LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.lyt_row_person_chat, parent, false)
            );
        }
        return new GroupMultipleAvatarViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.lyt_row_group_chat, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        if (currentUser != null) {
            holder.bindView(conversations.get(position));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder abstractHolder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(abstractHolder, position, payloads);
        } else {
            Bundle bundle = (Bundle) payloads.get(0);
            if (abstractHolder.getItemViewType() == PERSON) {
                PersonViewHolder holder = (PersonViewHolder)abstractHolder;
                for (String key : bundle.keySet()) {
                    switch (key) {
                        case "Key":
                            holder.row.setOnClickListener(v -> actionCallback.chatWith(conversations.get(position), friends.get(bundle.getString(key))));
                            break;
                        case "data":
                            holder.data.setData(bundle.getString(key));
                            break;
                        case "time":
                            holder.time.setTime(bundle.getLong(key));
                            break;
                        case "name":
                            holder.name.setText(bundle.getString(key));
                            break;
                        case "avatar":
                            holder.avatar.setImageFromObject(bundle.getString(key));
                            break;
                        case "type":
                            holder.name.setTypeface(null, bundle.getInt(key));
                            holder.data.setTypeface(null, bundle.getInt(key));
                            holder.time.setTypeface(null, bundle.getInt(key));
                            break;
                    }
                }
            } else if (abstractHolder.getItemViewType() == GROUP_SINGLE_AVATAR) {
                GroupSingleAvatarViewHolder holder = (GroupSingleAvatarViewHolder)abstractHolder;
                holder.row.setOnClickListener(v -> actionCallback.chatWith(conversations.get(position), null));
                for (String key : bundle.keySet()) {
                    switch (key) {
                        case "data":
                            holder.data.setData(bundle.getString(key));
                            break;
                        case "time":
                            holder.time.setTime(bundle.getLong(key));
                            break;
                        case "name":
                            holder.name.setText(bundle.getString(key));
                            break;
                        case "avatar":
                            holder.avatar.setImageFromObject(bundle.getString(key));
                            break;
                        case "type":
                            holder.name.setTypeface(null, bundle.getInt(key));
                            holder.data.setTypeface(null, bundle.getInt(key));
                            holder.time.setTypeface(null, bundle.getInt(key));
                            break;
                    }
                }
            } else {
                GroupMultipleAvatarViewHolder holder = (GroupMultipleAvatarViewHolder)abstractHolder;
                holder.row.setOnClickListener(v -> actionCallback.chatWith(conversations.get(position), null));
                for (String key : bundle.keySet()) {
                    switch (key) {
                        case "data":
                            holder.data.setData(bundle.getString(key));
                            break;
                        case "time":
                            holder.time.setTime(bundle.getLong(key));
                            break;
                        case "name":
                            holder.name.setText(bundle.getString(key));
                            break;
                        case "avatar1":
                            holder.avatar1.setImageFromObject(bundle.getString(key));
                            break;
                        case "avatar2":
                            holder.avatar2.setImageFromObject(bundle.getString(key));
                            break;
                        case "avatar3":
                            holder.avatar3.setImageFromObject(bundle.getString(key));
                            break;
                        case "number":
                            holder.number.setText(String.valueOf(bundle.getLong(key)));
                            break;
                        case "type":
                            holder.name.setTypeface(null, bundle.getInt(key));
                            holder.data.setTypeface(null, bundle.getInt(key));
                            holder.time.setTypeface(null, bundle.getInt(key));
                            break;
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    @SuppressLint("CheckResult")
    public void onNewData(User currentUser,
                          List<Conversation> conversations,
                          Map<String, User> friends) {
        Single
                .just(DiffUtil.calculateDiff(
                        new MessageDiffUtil(
                                this.currentUser,
                                currentUser,
                                this.conversations,
                                this.friends,
                                conversations,
                                friends
                        )
                ))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(diffResult -> {
                    this.currentUser    = currentUser;
                    this.conversations  = conversations;
                    this.friends        = friends;
                    diffResult.dispatchUpdatesTo(this);
                });
    }
}
