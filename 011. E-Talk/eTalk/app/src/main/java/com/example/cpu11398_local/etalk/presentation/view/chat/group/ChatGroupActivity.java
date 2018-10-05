package com.example.cpu11398_local.etalk.presentation.view.chat.group;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.presentation.view.BaseActivity;
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

public class ChatGroupActivity extends BaseActivity {

    private final int REQUEST_CAMERA_CODE  = 0;
    private final int REQUEST_GALLERY_CODE = 1;

    @Inject
    @Named("ChatGroupViewModel")
    public ViewModel viewModel;

    private ActivityChatGroupBinding binding;
    private Disposable disposable;
    private ViewModelCallback helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Tool.setStatusBarHeight(
                this,
                findViewById(R.id.chat_activity_status_bar)
        );
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
            }
        });
        binding.chatActivityTxtFriendName.setText(getIntent().getExtras().getString("name"));
        binding.chatActivityTxtFriendStatus.setText(
                getIntent().getExtras().getInt("number")
                        + " "
                        + getString(R.string.app_members)
        );
        addControlKeyboardView(binding.chatActivityLytMessage);
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
                                getIntent().getExtras().getString("key")
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
                            REQUEST_CAMERA_CODE,
                            REQUEST_GALLERY_CODE
                    ).show();
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
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_CAMERA_CODE) {
                Log.e("Test", " " + data.getExtras().get("data"));
                /*Bitmap bitmapAvatar = (Bitmap)data.getExtras().get("data");
                avatarCopy.copy(bitmapAvatar);*/
            }
            else if (requestCode == REQUEST_GALLERY_CODE) {
                /*try {
                    InputStream is = getContentResolver().openInputStream(data.getData());
                    OutputStream os = new FileOutputStream(new File(getFilesDir(), "test." + MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(data.getData()))));
                    byte[] buff = new byte[1024];
                    int len;
                    while((len = is.read(buff)) > 0){
                        os.write(buff,0, len);
                    }
                    is.close();
                    os.close();
                } catch (FileNotFoundException e) {
                    Log.e("=========", "====================");
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                /*Bitmap bitmapAvatar = Tool.getImageWithUri(this, data.getData());
                avatarCopy.copy(bitmapAvatar);*/
            }
        }
    }
}
