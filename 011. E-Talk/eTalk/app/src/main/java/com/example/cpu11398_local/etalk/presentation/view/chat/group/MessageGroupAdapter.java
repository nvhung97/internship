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
import com.example.cpu11398_local.etalk.presentation.custom.ClockView;
import com.example.cpu11398_local.etalk.presentation.custom.RoundedMapView;
import com.example.cpu11398_local.etalk.presentation.model.Message;
import com.example.cpu11398_local.etalk.presentation.view.chat.media.MapActivity;
import com.example.cpu11398_local.etalk.presentation.view.chat.media.MediaPhotoActivity;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModelCallback;
import com.example.cpu11398_local.etalk.utils.Event;
import com.example.cpu11398_local.etalk.utils.GlideApp;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MessageGroupAdapter extends RecyclerView.Adapter<MessageGroupAdapter.MessageGroupViewHolder>{

    private final int ME_TEXT       = 0;
    private final int ME_IMAGE      = 1;
    private final int ME_SOUND      = 2;
    private final int ME_FILE       = 4;
    private final int ME_MAP        = 5;
    private final int FRIEND_TEXT   = 7;
    private final int FRIEND_IMAGE  = 8;
    private final int FRIEND_SOUND  = 9;
    private final int FRIEND_FILE   = 11;
    private final int FRIEND_MAP    = 12;

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

    public class MessageSoundMeViewHolder extends MessageGroupViewHolder {
        private Context              context;
        public ConstraintLayout      content;
        public ClockView             data;
        public ImageButton           play;
        public ImageButton           stop;
        public ImageView             equalizer;
        public TextView              time;
        public AvatarImageView       avatar;
        public List<AvatarImageView> seens = new ArrayList<>();
        public TextView              more;
        public MessageSoundMeViewHolder(View view) {
            super(view);
            context     = view.getContext();
            content     = view.findViewById(R.id.lyt_message_group_sound_me_content);
            data        = view.findViewById(R.id.lyt_message_group_sound_me_data);
            play        = view.findViewById(R.id.lyt_message_group_sound_me_play);
            stop        = view.findViewById(R.id.lyt_message_group_sound_me_stop);
            equalizer   = view.findViewById(R.id.lyt_message_group_sound_me_equalizer);
            time        = view.findViewById(R.id.lyt_message_group_sound_me_time);
            avatar      = view.findViewById(R.id.lyt_message_group_sound_me_status);
            more        = view.findViewById(R.id.lyt_message_group_sound_me_more);
            seens.add(view.findViewById(R.id.lyt_message_group_sound_me_seen1));
            seens.add(view.findViewById(R.id.lyt_message_group_sound_me_seen2));
            seens.add(view.findViewById(R.id.lyt_message_group_sound_me_seen3));
            seens.add(view.findViewById(R.id.lyt_message_group_sound_me_seen4));
            seens.add(view.findViewById(R.id.lyt_message_group_sound_me_seen5));
            seens.add(view.findViewById(R.id.lyt_message_group_sound_me_seen6));
            seens.add(view.findViewById(R.id.lyt_message_group_sound_me_seen7));
            seens.add(view.findViewById(R.id.lyt_message_group_sound_me_seen8));
            seens.add(view.findViewById(R.id.lyt_message_group_sound_me_seen9));
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
            if (item.getProgressVisible() == View.VISIBLE) {
                data.setCountTime((long)item.getProgressPercent());
                GlideApp
                        .with(context)
                        .load(R.drawable.equalizer_animation)
                        .override(Target.SIZE_ORIGINAL)
                        .into(equalizer);
            } else {
                data.setCountTime(Long.parseLong(item.getTextData().split("eTaLkAuDiO")[1]));
                GlideApp
                        .with(context)
                        .load(R.drawable.equalizer_no_animation)
                        .override(Target.SIZE_ORIGINAL)
                        .into(equalizer);
            }
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
            play.setVisibility(item.getStartVisible());
            stop.setVisibility(item.getStopVisible());
            play.setOnClickListener(v ->
                    callback.onHelp(Event.create(
                            Event.CHAT_ACTIVITY_START_PLAY,
                            messages.indexOf(item)
                    ))
            );
            stop.setOnClickListener(v ->
                    callback.onHelp(Event.create(
                            Event.CHAT_ACTIVITY_STOP_PLAY
                    ))
            );
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
            download.setVisibility(item.getStartVisible());
            cancel.setVisibility(item.getStopVisible());
            progressBar.setVisibility(item.getProgressVisible());
            progressBar.setProgress(item.getProgressPercent(), true);
            download.setOnClickListener(v ->
                    callback.onHelp(Event.create(
                            Event.CHAT_ACTIVITY_START_DOWNLOAD,
                            messages.indexOf(item)
                    ))
            );
            cancel.setOnClickListener(v ->
                    callback.onHelp(Event.create(
                            Event.CHAT_ACTIVITY_STOP_DOWNLOAD
                    ))
            );
        }
    }

    public class MessageMapMeViewHolder extends MessageGroupViewHolder implements OnMapReadyCallback {
        private Context              context;
        public ConstraintLayout      content;
        public RoundedMapView        data;
        public View                  mask;
        public TextView              time;
        public AvatarImageView       avatar;
        public List<AvatarImageView> seens = new ArrayList<>();
        public TextView              more;
        public GoogleMap             googleMap;
        public LatLng                latLng;
        public MessageMapMeViewHolder(View view) {
            super(view);
            context = view.getContext();
            content = view.findViewById(R.id.lyt_message_group_map_me_content);
            data    = view.findViewById(R.id.lyt_message_group_map_me_data);
            mask    = view.findViewById(R.id.lyt_message_group_map_me_mask);
            time    = view.findViewById(R.id.lyt_message_group_map_me_time);
            avatar  = view.findViewById(R.id.lyt_message_group_map_me_status);
            more    = view.findViewById(R.id.lyt_message_group_map_me_more);
            seens.add(view.findViewById(R.id.lyt_message_group_map_me_seen1));
            seens.add(view.findViewById(R.id.lyt_message_group_map_me_seen2));
            seens.add(view.findViewById(R.id.lyt_message_group_map_me_seen3));
            seens.add(view.findViewById(R.id.lyt_message_group_map_me_seen4));
            seens.add(view.findViewById(R.id.lyt_message_group_map_me_seen5));
            seens.add(view.findViewById(R.id.lyt_message_group_map_me_seen6));
            seens.add(view.findViewById(R.id.lyt_message_group_map_me_seen7));
            seens.add(view.findViewById(R.id.lyt_message_group_map_me_seen8));
            seens.add(view.findViewById(R.id.lyt_message_group_map_me_seen9));
            data.onCreate(null);
            data.getMapAsync(this);
            data.onResume();
            data.onStart();
            mask.setOnClickListener(v -> {
                Intent intent = new Intent(context, MapActivity.class);
                intent.putExtra("lat", latLng.latitude);
                intent.putExtra("lng", latLng.longitude);
                context.startActivity(intent);
            });
        }
        @Override
        public void bindView(MessageGroupItem item) {
            String[] location = item.getTextData().split(",");
            latLng = new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1]));
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
            if (googleMap != null) {
                updateMap();
            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            this.googleMap = googleMap;
            updateMap();
        }

        private void updateMap() {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
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

    public class MessageSoundFriendViewHolder extends MessageGroupViewHolder {
        public TextView              name;
        public Context               context;
        public ConstraintLayout      content;
        public ClockView             data;
        public ImageButton           play;
        public ImageButton           stop;
        public ImageView             equalizer;
        public TextView              time;
        public AvatarImageView       avatar;
        public List<AvatarImageView> seens = new ArrayList<>();
        public TextView              more;
        public MessageSoundFriendViewHolder(View view) {
            super(view);
            context     = view.getContext();
            name        = view.findViewById(R.id.lyt_message_group_sound_friend_name);
            content     = view.findViewById(R.id.lyt_message_group_sound_friend_content);
            data        = view.findViewById(R.id.lyt_message_group_sound_friend_data);
            play        = view.findViewById(R.id.lyt_message_group_sound_friend_play);
            stop        = view.findViewById(R.id.lyt_message_group_sound_friend_stop);
            equalizer   = view.findViewById(R.id.lyt_message_group_sound_friend_equalizer);
            time        = view.findViewById(R.id.lyt_message_group_sound_friend_time);
            avatar      = view.findViewById(R.id.lyt_message_group_sound_friend_avatar);
            more        = view.findViewById(R.id.lyt_message_group_sound_friend_more);
            seens.add(view.findViewById(R.id.lyt_message_group_sound_friend_seen1));
            seens.add(view.findViewById(R.id.lyt_message_group_sound_friend_seen2));
            seens.add(view.findViewById(R.id.lyt_message_group_sound_friend_seen3));
            seens.add(view.findViewById(R.id.lyt_message_group_sound_friend_seen4));
            seens.add(view.findViewById(R.id.lyt_message_group_sound_friend_seen5));
            seens.add(view.findViewById(R.id.lyt_message_group_sound_friend_seen6));
            seens.add(view.findViewById(R.id.lyt_message_group_sound_friend_seen7));
            seens.add(view.findViewById(R.id.lyt_message_group_sound_friend_seen8));
            seens.add(view.findViewById(R.id.lyt_message_group_sound_friend_seen9));
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
            if (item.getProgressVisible() == View.VISIBLE) {
                data.setCountTime((long)item.getProgressPercent());
                GlideApp
                        .with(context)
                        .load(R.drawable.equalizer_animation)
                        .override(Target.SIZE_ORIGINAL)
                        .into(equalizer);
            } else {
                data.setCountTime(Long.parseLong(item.getTextData().split("eTaLkAuDiO")[1]));
                GlideApp
                        .with(context)
                        .load(R.drawable.equalizer_no_animation)
                        .override(Target.SIZE_ORIGINAL)
                        .into(equalizer);
            }
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
            play.setVisibility(item.getStartVisible());
            stop.setVisibility(item.getStopVisible());
            play.setOnClickListener(v ->
                    callback.onHelp(Event.create(
                            Event.CHAT_ACTIVITY_START_PLAY,
                            messages.indexOf(item)
                    ))
            );
            stop.setOnClickListener(v ->
                    callback.onHelp(Event.create(
                            Event.CHAT_ACTIVITY_STOP_PLAY
                    ))
            );
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
            download.setVisibility(item.getStartVisible());
            cancel.setVisibility(item.getStopVisible());
            progressBar.setVisibility(item.getProgressVisible());
            progressBar.setProgress(item.getProgressPercent(), true);
            download.setOnClickListener(v ->
                callback.onHelp(Event.create(
                        Event.CHAT_ACTIVITY_START_DOWNLOAD,
                        messages.indexOf(item)
                ))
            );
            cancel.setOnClickListener(v ->
                callback.onHelp(Event.create(
                        Event.CHAT_ACTIVITY_STOP_DOWNLOAD
                ))
            );
        }
    }

    public class MessageMapFriendViewHolder extends MessageGroupViewHolder  implements OnMapReadyCallback {
        private Context              context;
        public TextView              name;
        public ConstraintLayout      content;
        public RoundedMapView        data;
        public AvatarImageView       avatarMarker;
        public View                  mask;
        public TextView              time;
        public AvatarImageView       avatar;
        public List<AvatarImageView> seens = new ArrayList<>();
        public TextView              more;
        public GoogleMap             googleMap;
        public LatLng                latLng;
        public MessageMapFriendViewHolder(View view) {
            super(view);
            context = view.getContext();
            name    = view.findViewById(R.id.lyt_message_group_map_friend_name);
            content = view.findViewById(R.id.lyt_message_group_map_friend_content);
            data    = view.findViewById(R.id.lyt_message_group_map_friend_data);
            mask    = view.findViewById(R.id.lyt_message_group_map_friend_mask);
            time    = view.findViewById(R.id.lyt_message_group_map_friend_time);
            avatar  = view.findViewById(R.id.lyt_message_group_map_friend_avatar);
            more    = view.findViewById(R.id.lyt_message_group_map_friend_more);
            avatarMarker = view.findViewById(R.id.lyt_message_group_map_friend_marker_avatar);
            seens.add(view.findViewById(R.id.lyt_message_group_map_friend_seen1));
            seens.add(view.findViewById(R.id.lyt_message_group_map_friend_seen2));
            seens.add(view.findViewById(R.id.lyt_message_group_map_friend_seen3));
            seens.add(view.findViewById(R.id.lyt_message_group_map_friend_seen4));
            seens.add(view.findViewById(R.id.lyt_message_group_map_friend_seen5));
            seens.add(view.findViewById(R.id.lyt_message_group_map_friend_seen6));
            seens.add(view.findViewById(R.id.lyt_message_group_map_friend_seen7));
            seens.add(view.findViewById(R.id.lyt_message_group_map_friend_seen8));
            seens.add(view.findViewById(R.id.lyt_message_group_map_friend_seen9));
            data.onCreate(null);
            data.getMapAsync(this);
            data.onResume();
            data.onStart();
            mask.setOnClickListener(v -> {
                Intent intent = new Intent(context, MapActivity.class);
                intent.putExtra("lat", latLng.latitude);
                intent.putExtra("lng", latLng.longitude);
                intent.putExtra("avatar", avatarMarker.getDrawingCache());
                context.startActivity(intent);
            });
        }
        @Override
        public void bindView(MessageGroupItem item) {
            String[] location = item.getTextData().split(",");
            latLng = new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1]));
            name.setText(item.getName());
            name.setVisibility(item.getNameVisible());
            time.setText(item.getTime());
            time.setVisibility(item.getTimeVisible());
            avatar.setImageFromObject(item.getAvatar());
            avatar.setVisibility(item.getAvatarVisible());
            avatarMarker.setImageFromObject(item.getAvatar());
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
            if (googleMap != null) {
                updateMap();
            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            this.googleMap = googleMap;
            updateMap();
        }

        private void updateMap() {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
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
            if (message.getType() == Message.MAP) {
                return ME_MAP;
            }
            if (message.getType() == Message.SOUND) {
                return ME_SOUND;
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
            if (message.getType() == Message.MAP) {
                return FRIEND_MAP;
            }
            if (message.getType() == Message.SOUND) {
                return FRIEND_SOUND;
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
            case ME_MAP:
                return new MessageMapMeViewHolder(
                        LayoutInflater
                                .from(parent.getContext())
                                .inflate(R.layout.lyt_message_group_map_me, parent, false)
                );
            case ME_SOUND:
                return new MessageSoundMeViewHolder(
                        LayoutInflater
                                .from(parent.getContext())
                                .inflate(R.layout.lyt_message_group_sound_me, parent, false)
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
            case FRIEND_MAP:
                return new MessageMapFriendViewHolder(
                        LayoutInflater
                                .from(parent.getContext())
                                .inflate(R.layout.lyt_message_group_map_friend, parent, false)
                );
            case FRIEND_SOUND:
                return new MessageSoundFriendViewHolder(
                        LayoutInflater
                                .from(parent.getContext())
                                .inflate(R.layout.lyt_message_group_sound_friend, parent, false)
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
                    boolean isMoreItem = this.messages.size() != messages.size();
                    this.messages = messages;
                    diffResult.dispatchUpdatesTo(this);
                    if (isMoreItem) {
                        func.call();
                    }
                });
    }
}
