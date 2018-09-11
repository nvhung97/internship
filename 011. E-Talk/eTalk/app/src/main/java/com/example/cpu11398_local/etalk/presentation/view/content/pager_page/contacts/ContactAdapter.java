package com.example.cpu11398_local.etalk.presentation.view.content.pager_page.contacts;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.presentation.custom.AvatarImageView;
import com.example.cpu11398_local.etalk.presentation.custom.StatusView;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.model.User;
import com.example.cpu11398_local.etalk.presentation.view_model.content.ContactViewModel;
import java.util.List;
import java.util.Map;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private User                            currentUser;
    private List<Conversation>              conversations;
    private Map<String, User>               friends;
    private ContactViewModel.ActionCallback actionCallback;

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        public FrameLayout      row;
        public AvatarImageView  avatar;
        public StatusView       status;
        public TextView         name;
        public ImageButton      voiceCall;
        public ImageButton      videoCall;
        public ContactViewHolder(View itemView) {
            super(itemView);
            row         = itemView.findViewById(R.id.lyt_row_contact);
            avatar      = itemView.findViewById(R.id.lyt_row_contact_avatar);
            status      = itemView.findViewById(R.id.lyt_row_contact_status);
            name        = itemView.findViewById(R.id.lyt_row_contact_name);
            voiceCall   = itemView.findViewById(R.id.lyt_row_contact_voice_call);
            videoCall   = itemView.findViewById(R.id.lyt_row_contact_video_call);
        }
    }

    public ContactAdapter(User currentUser,
                          List<Conversation> conversations,
                          Map<String, User> friends,
                          ContactViewModel.ActionCallback actionCallback) {
        this.currentUser    = currentUser;
        this.conversations  = conversations;
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
        User friend = null;
        for (String key : conversations.get(position).getMembers().keySet()) {
            if (!key.equals(currentUser.getUsername())) {
                friend = friends.get(key);
                break;
            }
        }
        if (friend != null) {
            holder.avatar.setImageFromObject(friend.getAvatar());
            holder.status.setStatus(friend.getActive());
            holder.name.setText(friend.getName());
            holder.row.setOnClickListener(v -> actionCallback.chatWith(conversations.get(position)));
            holder.voiceCall.setOnClickListener(v -> actionCallback.voiceCallWith(conversations.get(position)));
            holder.videoCall.setOnClickListener(v -> actionCallback.videoCallWith(conversations.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    private boolean allowUpdate = true;

    public void onNewData(User currentUser,
                          List<Conversation> conversations,
                          Map<String, User> friends) {
        if (allowUpdate) {
            allowUpdate = false;
            Single
                    .just(DiffUtil.calculateDiff(
                            new ContactDiffUtil(
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
                        allowUpdate = true;
                    });
        }
    }
}
