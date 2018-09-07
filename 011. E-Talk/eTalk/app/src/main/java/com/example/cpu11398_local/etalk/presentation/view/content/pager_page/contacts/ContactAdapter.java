package com.example.cpu11398_local.etalk.presentation.view.content.pager_page.contacts;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.presentation.custom.AvatarImageView;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.presentation.view_model.content.ContactViewModel;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<User> friends;
    private ContactViewModel.ActionCallback actionCallback;

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        public FrameLayout      row;
        public AvatarImageView  avatar;
        public TextView         name;
        public ImageButton      voiceCall;
        public ImageButton      videoCall;
        public ContactViewHolder(View itemView) {
            super(itemView);
            row         = itemView.findViewById(R.id.lyt_row_contact);
            avatar      = itemView.findViewById(R.id.lyt_row_contact_avatar);
            name        = itemView.findViewById(R.id.lyt_row_contact_name);
            voiceCall   = itemView.findViewById(R.id.lyt_row_contact_voice_call);
            videoCall   = itemView.findViewById(R.id.lyt_row_contact_video_call);
        }
    }

    public ContactAdapter(List<User> friends, ContactViewModel.ActionCallback actionCallback) {
        this.friends        = friends;
        this.actionCallback = actionCallback;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.lyt_row_contact, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        User friend = friends.get(position);
        holder.avatar.setImageFromObject(friend.getAvatar());
        holder.name.setText(friend.getName());
        holder.row.setOnClickListener(v -> actionCallback.chatWith(friend));
        holder.voiceCall.setOnClickListener(v -> actionCallback.voiceCallWith(friend));
        holder.videoCall.setOnClickListener(v -> actionCallback.videoCallWith(friend));
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }


}
