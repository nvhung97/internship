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

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder>{

    private List<Conversation>              conversations;
    private Map<String, User>               friends;
    private GroupViewModel.ActionCallback   actionCallback;

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        public FrameLayout      row;
        public AvatarImageView  avatar0;
        public AvatarImageView  avatar1;
        public AvatarImageView  avatar2;
        public AvatarImageView  avatar3;
        public TextView         number;
        public TextView         name;
        public ShortMessage     data;
        public TimeTextView     time;
        public GroupViewHolder(View itemView) {
            super(itemView);
            row         = itemView.findViewById(R.id.lyt_row_message_group);
            avatar0     = itemView.findViewById(R.id.lyt_row_message_group_avatar0);
            avatar1     = itemView.findViewById(R.id.lyt_row_message_group_avatar1);
            avatar2     = itemView.findViewById(R.id.lyt_row_message_group_avatar2);
            avatar3     = itemView.findViewById(R.id.lyt_row_message_group_avatar3);
            number      = itemView.findViewById(R.id.lyt_row_message_group_number);
            name        = itemView.findViewById(R.id.lyt_row_message_group_name);
            data        = itemView.findViewById(R.id.lyt_row_message_group_message);
            time        = itemView.findViewById(R.id.lyt_row_message_group_time);
        }
    }

    public GroupAdapter(List<Conversation> conversations,
                        Map<String, User> friends,
                        GroupViewModel.ActionCallback actionCallback) {
        this.conversations  = conversations;
        this.friends        = friends;
        this.actionCallback = actionCallback;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.lyt_row_group_chat, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Conversation conversation = conversations.get(position);
        if (conversation.getAvatar() != null) {
            holder.avatar0.setImageFromObject(conversation.getAvatar());
            holder.avatar0.setVisibility(View.VISIBLE);
            holder.avatar1.setVisibility(View.INVISIBLE);
            holder.avatar2.setVisibility(View.INVISIBLE);
            holder.avatar3.setVisibility(View.INVISIBLE);
            holder.number.setVisibility(View.INVISIBLE);
        } else {
            List<String> keyFriends = new ArrayList<>(conversation.getMembers().keySet());
            if (friends.get(keyFriends.get(0)) != null) {
                holder.avatar1.setImageFromObject(friends.get(keyFriends.get(0)).getAvatar());
            }
            if (friends.get(keyFriends.get(1)) != null) {
                holder.avatar2.setImageFromObject(friends.get(keyFriends.get(1)).getAvatar());
            }
            if (friends.get(keyFriends.get(2)) != null) {
                holder.avatar3.setImageFromObject(friends.get(keyFriends.get(2)).getAvatar());
            }
            holder.number.setText(String.valueOf(conversation.getMembers().size()));
            holder.avatar0.setVisibility(View.INVISIBLE);
            holder.avatar1.setVisibility(View.VISIBLE);
            holder.avatar2.setVisibility(View.VISIBLE);
            holder.avatar3.setVisibility(View.VISIBLE);
            holder.number.setVisibility(View.VISIBLE);
        }
        holder.name.setText(conversation.getName());
        holder.data.setData(conversation.getLastMessage().getData());
        holder.time.setTime(conversation.getLastMessage().getTime());
        holder.row.setOnClickListener(v -> actionCallback.chatWith(conversation));
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            Bundle bundle = (Bundle)payloads.get(0);
            for (String key : bundle.keySet()) {
                switch (key) {
                    case "data":
                        holder.data.setData(bundle.getString(key));
                        break;
                    case "time":
                        holder.time.setTime(bundle.getLong(key));
                        break;
                    case "name":
                        holder.number.setText(bundle.getString(key));
                        break;
                    case "avatar0":
                        holder.avatar0.setImageFromObject(bundle.getString(key));
                        holder.avatar0.setVisibility(View.VISIBLE);
                        holder.avatar1.setVisibility(View.INVISIBLE);
                        holder.avatar2.setVisibility(View.INVISIBLE);
                        holder.avatar3.setVisibility(View.INVISIBLE);
                        holder.number.setVisibility(View.INVISIBLE);
                        break;
                    case "avatar1":
                        holder.avatar1.setImageFromObject(bundle.getString(key));
                        holder.avatar0.setVisibility(View.INVISIBLE);
                        holder.avatar1.setVisibility(View.VISIBLE);
                        holder.avatar2.setVisibility(View.VISIBLE);
                        holder.avatar3.setVisibility(View.VISIBLE);
                        holder.number.setVisibility(View.VISIBLE);
                        break;
                    case "avatar2":
                        holder.avatar2.setImageFromObject(bundle.getString(key));
                        holder.avatar0.setVisibility(View.INVISIBLE);
                        holder.avatar1.setVisibility(View.VISIBLE);
                        holder.avatar2.setVisibility(View.VISIBLE);
                        holder.avatar3.setVisibility(View.VISIBLE);
                        holder.number.setVisibility(View.VISIBLE);
                        break;
                    case "avatar3":
                        holder.avatar3.setImageFromObject(bundle.getString(key));
                        holder.avatar0.setVisibility(View.INVISIBLE);
                        holder.avatar1.setVisibility(View.VISIBLE);
                        holder.avatar2.setVisibility(View.VISIBLE);
                        holder.avatar3.setVisibility(View.VISIBLE);
                        holder.number.setVisibility(View.VISIBLE);
                        break;
                    case "number":
                        holder.number.setText(String.valueOf(bundle.getLong(key)));
                        break;
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
