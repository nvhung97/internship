package com.example.cpu11398_local.etalk.presentation.view.welcome;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.data.local.ChatHolder;
import com.example.cpu11398_local.etalk.data.local.ETalkDB;
import com.example.cpu11398_local.etalk.presentation.di.AppComponent;
import com.example.cpu11398_local.etalk.presentation.di.AppModule;
import com.example.cpu11398_local.etalk.presentation.di.DaggerAppComponent;
import com.example.cpu11398_local.etalk.presentation.model.Message;
import com.example.cpu11398_local.etalk.presentation.view.BaseActivity;
import com.example.cpu11398_local.etalk.presentation.view.chat.person.MessagePersonItem;
import com.example.cpu11398_local.etalk.presentation.view.content.ContentActivity;
import com.example.cpu11398_local.etalk.presentation.view.login.LoginActivity;
import com.example.cpu11398_local.etalk.presentation.view.register.RegisterActivity;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.utils.Event;
import com.example.cpu11398_local.etalk.utils.Tool;

import javax.inject.Inject;
import javax.inject.Named;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WelcomeActivity extends BaseActivity {

    private final int REQUEST_LOGIN     = 0;
    private final int REQUEST_REGISTER  = 1;

    @Inject
    @Named("WelcomeViewModel")
    public ViewModel    viewModel;

    private Disposable  disposable;
    private Dialog      dialog;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        /*Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/1/", new TestModel());
        childUpdates.put("/50/", new Message("long", "addd", 0));
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("test")
                .updateChildren(childUpdates, (databaseError, databaseReference) -> {
                    if (databaseError == null) {
                        Log.e("Test", "OK");
                    } else  {
                        Log.i("Test", databaseError.getMessage());
                    }
                });*/
        /*FirebaseDatabase
                .getInstance()
                .getReference()
                .child("test")
                .child("1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TestModel testModel = dataSnapshot.getValue(TestModel.class);
                Log.e("TestModel", " " + testModel._getTime());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        ETalkDB eTalkDB = ETalkDB.getInstance(this);
        /*new Thread(() -> eTalkDB.eTalkDao().insertChatHolder(
                new ChatHolder(
                        "hungnv6",
                        new MessagePersonItem(
                                new Message(
                                        "long",
                                        "hello",
                                        Message.TEXT
                                )
                        )
                )
        )).start();*/
        /*eTalkDB
                .eTalkDao()
                .loadChatHolder("hungnv61536717419575")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(chatHolder -> {
                    Log.e("Test", " " +chatHolder.getMessages().size());
                });*/
    }

    /**
     * Start Login Activity when user click Login button.
     * @param v View is clicked.
     */
    @SuppressLint("CheckResult")
    public void showLoginView(View v) {
        //startActivity(new Intent(this, ProfileActivity.class));
        startActivityForResult(
                new Intent(WelcomeActivity.this, LoginActivity.class),
                REQUEST_LOGIN
        );
    }

    /**
     * Start Register Activity when user click Register button.
     * @param v View is clicked.
     */
    public void showRegisterView(View v) {
        startActivityForResult(
                new Intent(WelcomeActivity.this, RegisterActivity.class),
                REQUEST_REGISTER
        );
    }

    /**
     * Get result after {@link #showLoginView(View)} for login or {@link #showRegisterView(View)}
     * for register. If login successfully, start {@code ContentActivity}. If register successfully,
     * start {@code LoginActivity}. In other cases, do nothing.
     * @param requestCode code of action.
     * @param resultCode result of action.
     * @param data data after perform action.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_LOGIN:
                if (resultCode == RESULT_OK) {
                    startActivity(new Intent(this, ContentActivity.class));
                    finish();
                }
                break;
            case REQUEST_REGISTER:
                if (resultCode == RESULT_OK) {
                    startActivityForResult(
                            new Intent(this, LoginActivity.class),
                            REQUEST_LOGIN
                    );
                }
        }
    }

    /**
     * A Singleton {@code AppComponent} is used to inject object to other activity.
     */
    private static AppComponent appComponent;

    /**
     * create a new {@code AppComponent} if it does not exist.
     * @param context parameter is used create a new {@code AppComponent}.
     * @return {@code AppComponent}.
     */
    public static AppComponent getAppComponent(Context context) {
        if (appComponent == null) {
            appComponent = DaggerAppComponent
                    .builder()
                    .appModule(new AppModule(context))
                    .build();
        }
        return appComponent;
    }

    @Override
    public void onDataBinding() {
        WelcomeActivity.getAppComponent(this).inject(this);
    }

    @Override
    public void onSubscribeViewModel() {
        viewModel.subscribeObserver(new WelcomeObserver());
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

    private void onUserLoggedIn() {
        startActivity(new Intent(this, ContentActivity.class));
        finish();
    }

    private void onShowLoading() {
        dialog = Tool.createProcessingDialog(this);
        dialog.show();
    }

    private void onHideLoading() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private class WelcomeObserver implements Observer<Event> {
        @Override
        public void onSubscribe(Disposable d) {
            disposable = d;
        }

        @Override
        public void onNext(Event event) {
            Object[] data = event.getData();
            switch (event.getType()) {
                case Event.WELCOME_ACTIVITY_USER_LOGGED_IN:
                    onUserLoggedIn();
                    break;
                case Event.WELCOME_ACTIVITY_SHOW_LOADING:
                    onShowLoading();
                    break;
                case Event.WELCOME_ACTIVITY_HIDE_LOADING:
                    onHideLoading();
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
}
