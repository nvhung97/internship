package com.example.cpu11398_local.etalk.presentation.view.group;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.presentation.custom.AvatarImageView;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.presentation.view_model.group.CreateGroupViewModel;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private List<User> friends;
    private CreateGroupViewModel.SelectedCallback callback;

    public class FriendViewHolder extends RecyclerView.ViewHolder {
        public AvatarImageView  avatar;
        public TextView         name;
        public CheckBox         select;
        public FriendViewHolder(View itemView) {
            super(itemView);
            avatar  = itemView.findViewById(R.id.lyt_row_create_group_avatar);
            name    = itemView.findViewById(R.id.lyt_row_create_group_name);
            select  = itemView.findViewById(R.id.lyt_row_create_group_checkbox);
        }
    }

    public FriendAdapter(List<User> friends, CreateGroupViewModel.SelectedCallback callback) {
        this.friends  = friends;
        this.callback = callback;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FriendViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.lyt_row_create_group, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        User friend = friends.get(position);
        holder.avatar.setImageFromObject(friend.getAvatar());
        holder.name.setText(friend.getName());
        holder.select.setChecked(callback.isSelected(friend.getUsername()));
        holder.select.setOnClickListener(v -> {
            if (holder.select.isChecked()) {
                callback.select(friend.getUsername());
            } else {
                callback.remove(friend.getUsername());
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }
}
