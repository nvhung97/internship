package com.example.cpu11398_local.etalk.presentation.view.content.pager_page.groups;

import android.annotation.SuppressLint;
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
import com.example.cpu11398_local.etalk.presentation.view_model.content.GroupViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private final int SINGLE_AVATAR     = 0;
    private final int MULTIPLE_AVATAR   = 1;

    private List<Conversation>              conversations;
    private Map<String, User>               friends;
    private GroupViewModel.ActionCallback   actionCallback;

    public abstract class GroupViewHolder extends RecyclerView.ViewHolder {
        public GroupViewHolder(View view) {
            super(view);
        }
        public abstract void bindView(Conversation conversation);
    }

    public class SingleAvatarViewHolder extends GroupViewHolder {
        public FrameLayout      row;
        public AvatarImageView  avatar;
        public TextView         name;
        public ShortMessage     data;
        public TimeTextView     time;
        public SingleAvatarViewHolder(View itemView) {
            super(itemView);
            row     = itemView.findViewById(R.id.lyt_row_message_person);
            avatar  = itemView.findViewById(R.id.lyt_row_message_person_avatar);
            name    = itemView.findViewById(R.id.lyt_row_message_person_name);
            data    = itemView.findViewById(R.id.lyt_row_message_person_message);
            time    = itemView.findViewById(R.id.lyt_row_message_person_time);
        }
        @Override
        public void bindView(Conversation conversation) {
            row.setOnClickListener(v -> actionCallback.chatWith(conversation));
            avatar.setImageFromObject(conversation.getAvatar());
            name.setText(conversation.getName());
            data.setData(conversation.getLastMessage().getData());
            time.setTime(conversation.getLastMessage().getTime());
        }
    }

    public class MultipleAvatarViewHolder extends GroupViewHolder {
        public FrameLayout      row;
        public AvatarImageView  avatar1;
        public AvatarImageView  avatar2;
        public AvatarImageView  avatar3;
        public TextView         number;
        public TextView         name;
        public ShortMessage     data;
        public TimeTextView     time;
        public MultipleAvatarViewHolder(View itemView) {
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
            row.setOnClickListener(v -> actionCallback.chatWith(conversation));
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
        }
    }

    public GroupAdapter(List<Conversation> conversations,
                        Map<String, User> friends,
                        GroupViewModel.ActionCallback actionCallback) {
        this.conversations  = conversations;
        this.friends        = friends;
        this.actionCallback = actionCallback;
    }

    @Override
    public int getItemViewType(int position) {
        if (conversations.get(position).getAvatar() == null) {
            return MULTIPLE_AVATAR;
        }
        return SINGLE_AVATAR;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SINGLE_AVATAR) {
            return new SingleAvatarViewHolder(
                    LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.lyt_row_person_chat, parent, false)
            );
        }
        return new MultipleAvatarViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.lyt_row_group_chat, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        holder.bindView(conversations.get(position));
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder abstractHolder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(abstractHolder, position, payloads);
        } else {
            Bundle bundle = (Bundle)payloads.get(0);
            if (abstractHolder.getItemViewType() == SINGLE_AVATAR) {
                SingleAvatarViewHolder holder = (SingleAvatarViewHolder)abstractHolder;
                holder.row.setOnClickListener(v -> actionCallback.chatWith(conversations.get(position)));
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
                    }
                }
            }
            else {
                MultipleAvatarViewHolder holder = (MultipleAvatarViewHolder)abstractHolder;
                holder.row.setOnClickListener(v -> actionCallback.chatWith(conversations.get(position)));
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
    public void onNewData(List<Conversation> conversations,
                          Map<String, User> friends) {
        Single
                .just(DiffUtil.calculateDiff(
                        new GroupDiffUtil(
                                this.conversations,
                                this.friends,
                                conversations,
                                friends
                        )
                ))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(diffResult -> {
                    this.conversations  = conversations;
                    this.friends        = friends;
                    diffResult.dispatchUpdatesTo(this);
                });
    }
}
