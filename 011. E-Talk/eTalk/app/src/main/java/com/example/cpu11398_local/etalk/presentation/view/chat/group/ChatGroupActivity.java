package com.example.cpu11398_local.etalk.presentation.view.chat.group;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.presentation.model.Conversation;
import com.example.cpu11398_local.etalk.presentation.view.BaseActivity;
import com.example.cpu11398_local.etalk.presentation.view.chat.media.EmojiAdapter;
import com.example.cpu11398_local.etalk.presentation.view.chat.media.MapActivity;
import com.example.cpu11398_local.etalk.presentation.view.welcome.WelcomeActivity;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModelCallback;
import com.example.cpu11398_local.etalk.presentation.view_model.chat.ChatGroupViewModel;
import com.example.cpu11398_local.etalk.databinding.ActivityChatGroupBinding;
import com.example.cpu11398_local.etalk.utils.Event;
import com.example.cpu11398_local.etalk.utils.Tool;
import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ChatGroupActivity extends BaseActivity{

    private final int REQUEST_TAKE_PHOTO_CODE       = 0;
    private final int REQUEST_RECORD_CODE           = 1;
    private final int REQUEST_CHOOSE_PHOTOS_CODE    = 2;
    private final int REQUEST_CHOOSE_VIDEOS_CODE    = 3;
    private final int REQUEST_CHOOSE_FILE           = 4;
    private final int REQUEST_MAP                   = 5;

    @Inject
    @Named("ChatGroupViewModel")
    public ViewModel viewModel;

    private ActivityChatGroupBinding binding;
    private Disposable disposable;
    private ViewModelCallback helper;
    private int keyboardHeight = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tool.setStatusBarHeight(
                this,
                findViewById(R.id.chat_activity_status_bar)
        );

        binding.chatActivityLstEmoticon.setAdapter(new EmojiAdapter(this));
        binding.chatActivityLstEmoticon.setOnItemClickListener(
                (AdapterView.OnItemClickListener) (parent, v, position, id) -> {
                    binding.chatActivityEdtMessage.append(((TextView)v).getText());
                }
        );
        binding.chatActivityBtnAudioRecord.setOnTouchListener((View.OnTouchListener) (v, event) -> {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    Toast.makeText(this, "Recording", Toast.LENGTH_SHORT).show();
                    break;
                case MotionEvent.ACTION_UP:
                    Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        });
    }

    @Override
    public void onDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_group);
        viewModel = (ViewModel) getLastCustomNonConfigurationInstance();
        if (viewModel == null) {
            WelcomeActivity.getAppComponent(this).inject(this);
        }
        binding.setViewModel((ChatGroupViewModel)viewModel);
        binding.chatActivityLstMessage.setLayoutManager(new LinearLayoutManager(this));
        binding.chatActivityLstMessage.addItemDecoration(new ChatGroupDivider(
                (int)getResources().getDimension(R.dimen.divider_chat_space_same),
                (int)getResources().getDimension(R.dimen.divider_chat_space_diff)
        ));
        binding.chatActivityLstMessage.addOnLayoutChangeListener((View.OnLayoutChangeListener) (v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) {
                binding.chatActivityLstMessage.postDelayed(
                        (Runnable) () -> binding.chatActivityLstMessage.scrollToPosition(
                                binding.chatActivityLstMessage.getAdapter().getItemCount() - 1
                        ),
                        100
                );
                keyboardHeight = oldBottom - bottom;
            }
        });
        binding.chatActivityTxtFriendName.setText(getIntent().getExtras().getString("name"));
        if (getIntent().getExtras().getLong("type") == Conversation.PERSON) {
            Long time = getIntent().getExtras().getLong("number");
            if (System.currentTimeMillis() - time < 10000) {
                binding.chatActivityTxtFriendStatus.setText(
                        getString(R.string.app_online)
                );
            } else {
                binding.chatActivityTxtFriendStatus.setText(
                        getString(R.string.app_offline)
                );
            }
        } else {
            binding.chatActivityTxtFriendStatus.setText(
                    getIntent().getExtras().getInt("number")
                            + " "
                            + getString(R.string.app_members)
            );
        }
        addControlKeyboardView(binding.chatActivityLytMessage);
        binding.chatActivityEdtMessage.setOnFocusChangeListener((View.OnFocusChangeListener) (v, hasFocus) -> {
            if (hasFocus) {
                if (binding.chatActivityLstEmoticon.getVisibility() == View.VISIBLE) {
                    binding.chatActivityLstEmoticon.setVisibility(View.GONE);
                }
                if (binding.chatActivityLytAudio.getVisibility() == View.VISIBLE) {
                    binding.chatActivityLytAudio.setVisibility(View.GONE);
                }
            }
        });
        binding.chatActivityEdtMessage.setOnClickListener(v -> {
            if (binding.chatActivityLstEmoticon.getVisibility() == View.VISIBLE) {
                binding.chatActivityLstEmoticon.setVisibility(View.GONE);
            }
            if (binding.chatActivityLytAudio.getVisibility() == View.VISIBLE) {
                binding.chatActivityLytAudio.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            int     locationY        = (int)ev.getY();
            int[]   viewLocation     = new int[2];
            binding.chatActivityLstMessage.getLocationOnScreen(viewLocation);
            if (locationY <= (viewLocation[1] + binding.chatActivityLstMessage.getHeight())) {
                if (binding.chatActivityLstEmoticon.getVisibility() == View.VISIBLE) {
                    binding.chatActivityLstEmoticon.setVisibility(View.GONE);
                }
                if (binding.chatActivityLytAudio.getVisibility() == View.VISIBLE) {
                    binding.chatActivityLytAudio.setVisibility(View.GONE);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onSubscribeViewModel() {
        viewModel.subscribeObserver(new ChatObserver());
    }

    @Override
    public void onUnSubscribeViewModel() {
        if (!disposable.isDisposed()){
            disposable.dispose();
        }
    }

    @Override
    public Object onSaveViewModel() {
        return viewModel;
    }

    @Override
    public void onEndTaskViewModel() {
        viewModel.endTask();
    }

    public void onShowPopupMenu(View view, PopupMenu.OnMenuItemClickListener listener) {
        PopupMenu popupMenu = new PopupMenu(this, view, Gravity.RIGHT);
        popupMenu.inflate(R.menu.menu_chat_more);
        popupMenu.setOnMenuItemClickListener(listener);
        Tool.forcePopupMenuShowIcon(popupMenu);
        popupMenu.show();
    }

    private class ChatObserver implements Observer<Event> {
        @Override
        public void onSubscribe(Disposable d) {
            disposable = d;
        }

        @Override
        public void onNext(Event event) {
            Object[] data = event.getData();
            switch (event.getType()) {
                case Event.CHAT_ACTIVITY_BACK:
                    onBackPressed();
                    break;
                case Event.CHAT_ACTIVITY_HELPER:
                    if (helper == null) {
                        helper = (ViewModelCallback) data[0];
                        helper.onHelp(Event.create(
                                Event.CHAT_ACTIVITY_VALUE,
                                getIntent().getExtras().getString("user"),
                                getIntent().getExtras().getString("key"),
                                ChatGroupActivity.this
                        ));
                    }
                    break;
                case Event.CHAT_ACTIVITY_GOTO_LAST:
                    binding.chatActivityLstMessage.scrollToPosition(
                            binding.chatActivityLstMessage.getAdapter().getItemCount() - 1
                    );
                    break;
                case Event.CHAT_ACTIVITY_GET_MEDIA:
                    Tool.createMediaOptionDialog(
                            ChatGroupActivity.this,
                            REQUEST_TAKE_PHOTO_CODE,
                            REQUEST_RECORD_CODE,
                            REQUEST_CHOOSE_PHOTOS_CODE,
                            REQUEST_CHOOSE_VIDEOS_CODE
                    ).show();
                    break;
                case Event.CHAT_ACTIVITY_ATTACH:
                    Intent intent = new Intent();
                    intent.setType("*/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            REQUEST_CHOOSE_FILE
                    );
                    break;
                case Event.CHAT_ACTIVITY_SHOW_POPUP_MENU:
                    onShowPopupMenu(
                            (View)data[0],
                            (PopupMenu.OnMenuItemClickListener)data[1]
                    );
                    break;
                case Event.CHAT_ACTIVITY_SHOW_MAP:
                    if (ActivityCompat.checkSelfPermission(ChatGroupActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(ChatGroupActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                                ChatGroupActivity.this,
                                new String[]{
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                },
                                0
                        );
                    } else {
                        startActivityForResult(new Intent(ChatGroupActivity.this, MapActivity.class), REQUEST_MAP);
                    }
                    break;
                case Event.CHAT_ACTIVITY_EMOTICON:
                    if (binding.chatActivityLstEmoticon.getVisibility() == View.GONE) {
                        if (keyboardHeight != 0) {
                            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)binding.chatActivityLstEmoticon.getLayoutParams();
                            if (keyboardHeight != layoutParams.height) {
                                layoutParams.height = keyboardHeight;
                            }
                        }
                        Tool.hideSoftKeyboard(ChatGroupActivity.this);
                        new Handler().postDelayed(
                                () -> binding.chatActivityLstEmoticon.setVisibility(View.VISIBLE),
                                100
                        );
                    } else {
                        binding.chatActivityLstEmoticon.setVisibility(View.GONE);
                        Tool.showSoftKeyboard(
                                binding.chatActivityEdtMessage,
                                ChatGroupActivity.this
                        );
                    }
                    break;
                case Event.CHAT_ACTIVITY_AUDIO:
                    if (binding.chatActivityLytAudio.getVisibility() == View.GONE) {
                        if (keyboardHeight != 0) {
                            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)binding.chatActivityLytAudio.getLayoutParams();
                            if (keyboardHeight != layoutParams.height) {
                                layoutParams.height = keyboardHeight;
                            }
                        }
                        Tool.hideSoftKeyboard(ChatGroupActivity.this);
                        new Handler().postDelayed(
                                () -> binding.chatActivityLytAudio.setVisibility(View.VISIBLE),
                                100
                        );
                    } else {
                        binding.chatActivityLytAudio.setVisibility(View.GONE);
                        Tool.showSoftKeyboard(
                                binding.chatActivityEdtMessage,
                                ChatGroupActivity.this
                        );
                    }
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO_CODE:
                    helper.onHelp(Event.create(
                            Event.CHAT_ACTIVITY_SEND_IMAGE_URI,
                            Tool.imageCaptureUri
                    ));
                    break;
                case REQUEST_RECORD_CODE:
                    break;
                case REQUEST_CHOOSE_PHOTOS_CODE:
                    helper.onHelp(Event.create(
                            Event.CHAT_ACTIVITY_SEND_IMAGE_URI,
                            data.getData()
                    ));
                    break;
                case REQUEST_CHOOSE_VIDEOS_CODE:
                    break;
                case REQUEST_CHOOSE_FILE:
                    helper.onHelp(Event.create(
                            Event.CHAT_ACTIVITY_SEND_FILE,
                            data.getData()
                    ));
                    break;
                case REQUEST_MAP:
                    helper.onHelp(Event.create(
                            Event.CHAT_ACTIVITY_SEND_LOCATION,
                            data.getExtras().getDouble("lat"),
                            data.getExtras().getDouble("lng")
                    ));
                    break;
            }
        }
    }
}
