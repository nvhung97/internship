package com.example.cpu11398_local.etalk.presentation.view.chat.group;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.request.target.Target;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.presentation.custom.AvatarImageView;
import com.example.cpu11398_local.etalk.presentation.model.Message;
import com.example.cpu11398_local.etalk.presentation.view.chat.media.MediaPhotoActivity;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModelCallback;
import com.example.cpu11398_local.etalk.utils.Event;
import com.example.cpu11398_local.etalk.utils.GlideApp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MessageGroupAdapter extends RecyclerView.Adapter<MessageGroupAdapter.MessageGroupViewHolder>{

    private final int ME_TEXT       = 0;
    private final int ME_IMAGE      = 1;
    private final int ME_FILE       = 4;
    private final int FRIEND_TEXT   = 7;
    private final int FRIEND_IMAGE  = 8;
    private final int FRIEND_FILE   = 11;

    private List<MessageGroupItem>  messages;
    private ViewModelCallback       callback;

    public abstract class MessageGroupViewHolder extends RecyclerView.ViewHolder {
        public MessageGroupViewHolder(View view) {
            super(view);
        }
        public abstract void bindView(MessageGroupItem item);
    }

    public class MessageTextMeViewHolder extends MessageGroupViewHolder {
        public ConstraintLayout      content;
        public TextView              data;
        public TextView              time;
        public AvatarImageView       avatar;
        public List<AvatarImageView> seens = new ArrayList<>();
        public TextView              more;
        public MessageTextMeViewHolder(View view) {
            super(view);
            content = view.findViewById(R.id.lyt_message_group_text_me_content);
            data    = view.findViewById(R.id.lyt_message_group_text_me_data);
            time    = view.findViewById(R.id.lyt_message_group_text_me_time);
            avatar  = view.findViewById(R.id.lyt_message_group_text_me_status);
            more    = view.findViewById(R.id.lyt_message_group_text_me_more);
            seens.add(view.findViewById(R.id.lyt_message_group_text_me_seen1));
            seens.add(view.findViewById(R.id.lyt_message_group_text_me_seen2));
            seens.add(view.findViewById(R.id.lyt_message_group_text_me_seen3));
            seens.add(view.findViewById(R.id.lyt_message_group_text_me_seen4));
            seens.add(view.findViewById(R.id.lyt_message_group_text_me_seen5));
            seens.add(view.findViewById(R.id.lyt_message_group_text_me_seen6));
            seens.add(view.findViewById(R.id.lyt_message_group_text_me_seen7));
            seens.add(view.findViewById(R.id.lyt_message_group_text_me_seen8));
            seens.add(view.findViewById(R.id.lyt_message_group_text_me_seen9));
            content.setOnClickListener(v ->
                    time.setVisibility(
                            time.getVisibility() == View.VISIBLE
                                    ? View.GONE
                                    : View.VISIBLE
                    )
            );
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void bindView(MessageGroupItem item) {
            data.setText(Html.fromHtml(item.getTextData(), Html.FROM_HTML_MODE_COMPACT));
            time.setText(item.getTime());
            time.setVisibility(item.getTimeVisible());
            avatar.setImageFromObject(item.getAvatar());
            avatar.setVisibility(item.getAvatarVisible());
            List<String> seenAvatars = new ArrayList<>(item.getSeen().values());
            for (int i = 0; i < 9; ++i) {
                if (i < seenAvatars.size()) {
                    seens.get(i).setImageFromObject(seenAvatars.get(i));
                    seens.get(i).setVisibility(View.VISIBLE);
                } else {
                    seens.get(i).setVisibility(View.GONE);
                }
            }
            if (seenAvatars.size() > 9) {
                more.setText("+" + (seenAvatars.size() - 9));
                more.setVisibility(View.VISIBLE);
            } else {
                more.setVisibility(View.GONE);
            }
        }
    }

    public class MessageImageMeViewHolder extends MessageGroupViewHolder {
        private Context              context;
        public ConstraintLayout      content;
        public ImageView             data;
        public TextView              time;
        public AvatarImageView       avatar;
        public List<AvatarImageView> seens = new ArrayList<>();
        public TextView              more;
        public MessageImageMeViewHolder(View view) {
            super(view);
            context = view.getContext();
            content = view.findViewById(R.id.lyt_message_group_image_me_content);
            data    = view.findViewById(R.id.lyt_message_group_image_me_data);
            time    = view.findViewById(R.id.lyt_message_group_image_me_time);
            avatar  = view.findViewById(R.id.lyt_message_group_image_me_status);
            more    = view.findViewById(R.id.lyt_message_group_image_me_more);
            seens.add(view.findViewById(R.id.lyt_message_group_image_me_seen1));
            seens.add(view.findViewById(R.id.lyt_message_group_image_me_seen2));
            seens.add(view.findViewById(R.id.lyt_message_group_image_me_seen3));
            seens.add(view.findViewById(R.id.lyt_message_group_image_me_seen4));
            seens.add(view.findViewById(R.id.lyt_message_group_image_me_seen5));
            seens.add(view.findViewById(R.id.lyt_message_group_image_me_seen6));
            seens.add(view.findViewById(R.id.lyt_message_group_image_me_seen7));
            seens.add(view.findViewById(R.id.lyt_message_group_image_me_seen8));
            seens.add(view.findViewById(R.id.lyt_message_group_image_me_seen9));
            data.setClipToOutline(true);
        }
        @Override
        public void bindView(MessageGroupItem item) {
            GlideApp
                    .with(context)
                    .load(item.getTextData())
                    .override(Target.SIZE_ORIGINAL)
                    .into(data);
            time.setText(item.getTime());
            time.setVisibility(item.getTimeVisible());
            avatar.setImageFromObject(item.getAvatar());
            avatar.setVisibility(item.getAvatarVisible());
            List<String> seenAvatars = new ArrayList<>(item.getSeen().values());
            for (int i = 0; i < 9; ++i) {
                if (i < seenAvatars.size()) {
                    seens.get(i).setImageFromObject(seenAvatars.get(i));
                    seens.get(i).setVisibility(View.VISIBLE);
                } else {
                    seens.get(i).setVisibility(View.GONE);
                }
            }
            if (seenAvatars.size() > 9) {
                more.setText("+" + (seenAvatars.size() - 9));
                more.setVisibility(View.VISIBLE);
            } else {
                more.setVisibility(View.GONE);
            }
            content.setOnClickListener(v -> {
                Intent intent = new Intent(context, MediaPhotoActivity.class);
                intent.putExtra("name", "");
                intent.putExtra("link", item.getTextData());
                context.startActivity(intent);
            });
        }
    }

    public class MessageFileMeViewHolder extends MessageGroupViewHolder {
        public ConstraintLayout      content;
        public TextView              data;
        public ImageButton           download;
        public ProgressBar           progressBar;
        public ImageButton           cancel;
        public TextView              time;
        public AvatarImageView       avatar;
        public List<AvatarImageView> seens = new ArrayList<>();
        public TextView              more;
        public MessageFileMeViewHolder(View view) {
            super(view);
            content     = view.findViewById(R.id.lyt_message_group_file_me_content);
            data        = view.findViewById(R.id.lyt_message_group_file_me_data);
            download    = view.findViewById(R.id.lyt_message_group_file_me_download);
            progressBar = view.findViewById(R.id.lyt_message_group_file_me_progress);
            cancel      = view.findViewById(R.id.lyt_message_group_file_me_cancel);
            time        = view.findViewById(R.id.lyt_message_group_file_me_time);
            avatar      = view.findViewById(R.id.lyt_message_group_file_me_status);
            more        = view.findViewById(R.id.lyt_message_group_file_me_more);
            seens.add(view.findViewById(R.id.lyt_message_group_file_me_seen1));
            seens.add(view.findViewById(R.id.lyt_message_group_file_me_seen2));
            seens.add(view.findViewById(R.id.lyt_message_group_file_me_seen3));
            seens.add(view.findViewById(R.id.lyt_message_group_file_me_seen4));
            seens.add(view.findViewById(R.id.lyt_message_group_file_me_seen5));
            seens.add(view.findViewById(R.id.lyt_message_group_file_me_seen6));
            seens.add(view.findViewById(R.id.lyt_message_group_file_me_seen7));
            seens.add(view.findViewById(R.id.lyt_message_group_file_me_seen8));
            seens.add(view.findViewById(R.id.lyt_message_group_file_me_seen9));
            content.setOnClickListener(v ->
                    time.setVisibility(
                            time.getVisibility() == View.VISIBLE
                                    ? View.GONE
                                    : View.VISIBLE
                    )
            );
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void bindView(MessageGroupItem item) {
            data.setText(item.getTextData().split("eTaLkFiLe")[1]);
            time.setText(item.getTime());
            time.setVisibility(item.getTimeVisible());
            avatar.setImageFromObject(item.getAvatar());
            avatar.setVisibility(item.getAvatarVisible());
            List<String> seenAvatars = new ArrayList<>(item.getSeen().values());
            for (int i = 0; i < 9; ++i) {
                if (i < seenAvatars.size()) {
                    seens.get(i).setImageFromObject(seenAvatars.get(i));
                    seens.get(i).setVisibility(View.VISIBLE);
                } else {
                    seens.get(i).setVisibility(View.GONE);
                }
            }
            if (seenAvatars.size() > 9) {
                more.setText("+" + (seenAvatars.size() - 9));
                more.setVisibility(View.VISIBLE);
            } else {
                more.setVisibility(View.GONE);
            }
            download.setVisibility(item.getDownloadVisible());
            cancel.setVisibility(item.getCancelVisible());
            progressBar.setVisibility(item.getProgressVisible());
            progressBar.setProgress(item.getProgressPercent(), true);
            download.setOnClickListener(v ->
                    callback.onHelp(Event.create(
                            Event.CHAT_ACTIVITY_DOWNLOAD,
                            messages.indexOf(item)
                    ))
            );
            cancel.setOnClickListener(v ->
                    callback.onHelp(Event.create(
                            Event.CHAT_ACTIVITY_CANCEL
                    ))
            );
        }
    }

    public class MessageTextFriendViewHolder extends MessageGroupViewHolder {
        public TextView              name;
        public ConstraintLayout      content;
        public TextView              data;
        public TextView              time;
        public AvatarImageView       avatar;
        public List<AvatarImageView> seens = new ArrayList<>();
        public TextView              more;
        public MessageTextFriendViewHolder(View view) {
            super(view);
            name    = view.findViewById(R.id.lyt_message_group_text_friend_name);
            content = view.findViewById(R.id.lyt_message_group_text_friend_content);
            data    = view.findViewById(R.id.lyt_message_group_text_friend_data);
            time    = view.findViewById(R.id.lyt_message_group_text_friend_time);
            avatar  = view.findViewById(R.id.lyt_message_group_text_friend_avatar);
            more    = view.findViewById(R.id.lyt_message_group_text_friend_more);
            seens.add(view.findViewById(R.id.lyt_message_group_text_friend_seen1));
            seens.add(view.findViewById(R.id.lyt_message_group_text_friend_seen2));
            seens.add(view.findViewById(R.id.lyt_message_group_text_friend_seen3));
            seens.add(view.findViewById(R.id.lyt_message_group_text_friend_seen4));
            seens.add(view.findViewById(R.id.lyt_message_group_text_friend_seen5));
            seens.add(view.findViewById(R.id.lyt_message_group_text_friend_seen6));
            seens.add(view.findViewById(R.id.lyt_message_group_text_friend_seen7));
            seens.add(view.findViewById(R.id.lyt_message_group_text_friend_seen8));
            seens.add(view.findViewById(R.id.lyt_message_group_text_friend_seen9));
            content.setOnClickListener(v ->
                    time.setVisibility(
                            time.getVisibility() == View.VISIBLE
                                    ? View.GONE
                                    : View.VISIBLE
                    )
            );
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void bindView(MessageGroupItem item) {
            name.setText(item.getName());
            name.setVisibility(item.getNameVisible());
            data.setText(Html.fromHtml(item.getTextData(), Html.FROM_HTML_MODE_COMPACT));
            time.setText(item.getTime());
            time.setVisibility(item.getTimeVisible());
            avatar.setImageFromObject(item.getAvatar());
            avatar.setVisibility(item.getAvatarVisible());
            List<String> seenAvatars = new ArrayList<>(item.getSeen().values());
            for (int i = 0; i < 9; ++i) {
                if (i < seenAvatars.size()) {
                    seens.get(i).setImageFromObject(seenAvatars.get(i));
                    seens.get(i).setVisibility(View.VISIBLE);
                } else {
                    seens.get(i).setVisibility(View.GONE);
                }
            }
            if (seenAvatars.size() > 9) {
                more.setText("+" + (seenAvatars.size() - 9));
                more.setVisibility(View.VISIBLE);
            } else {
                more.setVisibility(View.GONE);
            }
        }
    }

    public class MessageImageFriendViewHolder extends MessageGroupViewHolder {
        private Context              context;
        public TextView              name;
        public ConstraintLayout      content;
        public ImageView             data;
        public TextView              time;
        public AvatarImageView       avatar;
        public List<AvatarImageView> seens = new ArrayList<>();
        public TextView              more;
        public MessageImageFriendViewHolder(View view) {
            super(view);
            context = view.getContext();
            name    = view.findViewById(R.id.lyt_message_group_image_friend_name);
            content = view.findViewById(R.id.lyt_message_group_image_friend_content);
            data    = view.findViewById(R.id.lyt_message_group_image_friend_data);
            time    = view.findViewById(R.id.lyt_message_group_image_friend_time);
            avatar  = view.findViewById(R.id.lyt_message_group_image_friend_avatar);
            more    = view.findViewById(R.id.lyt_message_group_image_friend_more);
            seens.add(view.findViewById(R.id.lyt_message_group_image_friend_seen1));
            seens.add(view.findViewById(R.id.lyt_message_group_image_friend_seen2));
            seens.add(view.findViewById(R.id.lyt_message_group_image_friend_seen3));
            seens.add(view.findViewById(R.id.lyt_message_group_image_friend_seen4));
            seens.add(view.findViewById(R.id.lyt_message_group_image_friend_seen5));
            seens.add(view.findViewById(R.id.lyt_message_group_image_friend_seen6));
            seens.add(view.findViewById(R.id.lyt_message_group_image_friend_seen7));
            seens.add(view.findViewById(R.id.lyt_message_group_image_friend_seen8));
            seens.add(view.findViewById(R.id.lyt_message_group_image_friend_seen9));
            data.setClipToOutline(true);
        }
        @Override
        public void bindView(MessageGroupItem item) {
            name.setText(item.getName());
            name.setVisibility(item.getNameVisible());
            GlideApp
                    .with(context)
                    .load(item.getTextData())
                    .override(Target.SIZE_ORIGINAL)
                    .into(data);
            time.setText(item.getTime());
            time.setVisibility(item.getTimeVisible());
            avatar.setImageFromObject(item.getAvatar());
            avatar.setVisibility(item.getAvatarVisible());
            List<String> seenAvatars = new ArrayList<>(item.getSeen().values());
            for (int i = 0; i < 9; ++i) {
                if (i < seenAvatars.size()) {
                    seens.get(i).setImageFromObject(seenAvatars.get(i));
                    seens.get(i).setVisibility(View.VISIBLE);
                } else {
                    seens.get(i).setVisibility(View.GONE);
                }
            }
            if (seenAvatars.size() > 9) {
                more.setText("+" + (seenAvatars.size() - 9));
                more.setVisibility(View.VISIBLE);
            } else {
                more.setVisibility(View.GONE);
            }
            content.setOnClickListener(v -> {
                Intent intent = new Intent(context, MediaPhotoActivity.class);
                intent.putExtra("name", item.getName());
                intent.putExtra("link", item.getTextData());
                context.startActivity(intent);
            });
        }
    }

    public class MessageFileFriendViewHolder extends MessageGroupViewHolder {
        public TextView              name;
        public ConstraintLayout      content;
        public TextView              data;
        public ImageButton           download;
        public ProgressBar           progressBar;
        public ImageButton           cancel;
        public TextView              time;
        public AvatarImageView       avatar;
        public List<AvatarImageView> seens = new ArrayList<>();
        public TextView              more;
        public MessageFileFriendViewHolder(View view) {
            super(view);
            name        = view.findViewById(R.id.lyt_message_group_file_friend_name);
            content     = view.findViewById(R.id.lyt_message_group_file_friend_content);
            data        = view.findViewById(R.id.lyt_message_group_file_friend_data);
            download    = view.findViewById(R.id.lyt_message_group_file_friend_download);
            progressBar = view.findViewById(R.id.lyt_message_group_file_friend_progress);
            cancel      = view.findViewById(R.id.lyt_message_group_file_friend_cancel);
            time        = view.findViewById(R.id.lyt_message_group_file_friend_time);
            avatar      = view.findViewById(R.id.lyt_message_group_file_friend_avatar);
            more        = view.findViewById(R.id.lyt_message_group_file_friend_more);
            seens.add(view.findViewById(R.id.lyt_message_group_file_friend_seen1));
            seens.add(view.findViewById(R.id.lyt_message_group_file_friend_seen2));
            seens.add(view.findViewById(R.id.lyt_message_group_file_friend_seen3));
            seens.add(view.findViewById(R.id.lyt_message_group_file_friend_seen4));
            seens.add(view.findViewById(R.id.lyt_message_group_file_friend_seen5));
            seens.add(view.findViewById(R.id.lyt_message_group_file_friend_seen6));
            seens.add(view.findViewById(R.id.lyt_message_group_file_friend_seen7));
            seens.add(view.findViewById(R.id.lyt_message_group_file_friend_seen8));
            seens.add(view.findViewById(R.id.lyt_message_group_file_friend_seen9));
            content.setOnClickListener(v ->
                    time.setVisibility(
                            time.getVisibility() == View.VISIBLE
                                    ? View.GONE
                                    : View.VISIBLE
                    )
            );
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void bindView(MessageGroupItem item) {
            name.setText(item.getName());
            name.setVisibility(item.getNameVisible());
            data.setText(item.getTextData().split("eTaLkFiLe")[1]);
            time.setText(item.getTime());
            time.setVisibility(item.getTimeVisible());
            avatar.setImageFromObject(item.getAvatar());
            avatar.setVisibility(item.getAvatarVisible());
            List<String> seenAvatars = new ArrayList<>(item.getSeen().values());
            for (int i = 0; i < 9; ++i) {
                if (i < seenAvatars.size()) {
                    seens.get(i).setImageFromObject(seenAvatars.get(i));
                    seens.get(i).setVisibility(View.VISIBLE);
                } else {
                    seens.get(i).setVisibility(View.GONE);
                }
            }
            if (seenAvatars.size() > 9) {
                more.setText("+" + (seenAvatars.size() - 9));
                more.setVisibility(View.VISIBLE);
            } else {
                more.setVisibility(View.GONE);
            }
            download.setVisibility(item.getDownloadVisible());
            cancel.setVisibility(item.getCancelVisible());
            progressBar.setVisibility(item.getProgressVisible());
            progressBar.setProgress(item.getProgressPercent(), true);
            download.setOnClickListener(v ->
                callback.onHelp(Event.create(
                        Event.CHAT_ACTIVITY_DOWNLOAD,
                        messages.indexOf(item)
                ))
            );
            cancel.setOnClickListener(v ->
                callback.onHelp(Event.create(
                        Event.CHAT_ACTIVITY_CANCEL
                ))
            );
        }
    }

    public MessageGroupAdapter(List<MessageGroupItem> messages, ViewModelCallback callback) {
        this.messages = messages;
        this.callback = callback;
    }

    @Override
    public int getItemViewType(int position) {
        MessageGroupItem    item    = messages.get(position);
        Message             message = item.getMessage();
        if (item.isMe()) {
            if (message.getType() == Message.TEXT) {
                return ME_TEXT;
            }
            if (message.getType() == Message.IMAGE) {
                return ME_IMAGE;
            }
            if (message.getType() == Message.FILE) {
                return ME_FILE;
            }
        } else {
            if (message.getType() == Message.TEXT) {
                return FRIEND_TEXT;
            }
            if (message.getType() == Message.IMAGE) {
                return FRIEND_IMAGE;
            }
            if (message.getType() == Message.FILE) {
                return FRIEND_FILE;
            }
        }
        return 0;
    }

    @NonNull
    @Override
    public MessageGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ME_TEXT:
                return new MessageTextMeViewHolder(
                        LayoutInflater
                                .from(parent.getContext())
                                .inflate(R.layout.lyt_message_group_text_me, parent, false)
                );
            case ME_IMAGE:
                return new MessageImageMeViewHolder(
                        LayoutInflater
                                .from(parent.getContext())
                                .inflate(R.layout.lyt_message_group_image_me, parent, false)
                );
            case ME_FILE:
                return new MessageFileMeViewHolder(
                        LayoutInflater
                                .from(parent.getContext())
                                .inflate(R.layout.lyt_message_group_file_me, parent, false)
                );
            case FRIEND_TEXT:
                return new MessageTextFriendViewHolder(
                        LayoutInflater
                                .from(parent.getContext())
                                .inflate(R.layout.lyt_message_group_text_friend, parent, false)
                );
            case FRIEND_IMAGE:
                return new MessageImageFriendViewHolder(
                        LayoutInflater
                                .from(parent.getContext())
                                .inflate(R.layout.lyt_message_group_image_friend, parent, false)
                );
            case FRIEND_FILE:
                return new MessageFileFriendViewHolder(
                        LayoutInflater
                                .from(parent.getContext())
                                .inflate(R.layout.lyt_message_group_file_friend, parent, false)
                );
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageGroupViewHolder holder, int position) {
        holder.bindView(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @SuppressLint("CheckResult")
    public void onNewData(List<MessageGroupItem> messages, Callable<Void> func) {
        Single
                .just(DiffUtil.calculateDiff(
                        new MessageGroupDiffUtil(
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
