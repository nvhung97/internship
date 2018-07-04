package com.example.cpu11398_local.dagger2demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import com.example.cpu11398_local.dagger2demo.data.UserManager;
import com.example.cpu11398_local.dagger2demo.utils.Utils;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangeActivity extends AppCompatActivity{

    private final String TAG = "ChangeActivity";

    @Inject
    public UserManager userManager;

    @BindView(R.id.edt_change_username) EditText edt_change_username;
    @BindView(R.id.edt_change_password) EditText edt_change_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        ButterKnife.bind(this);
        Utils.showLog(TAG, "onCreate");

        MainActivity.getAppComponent().inject(this);
    }

    @OnClick(R.id.btn_change)
    public void change(){
        Utils.showLog(TAG, "change");
        userManager.change(
                edt_change_username.getText().toString(),
                edt_change_password.getText().toString()
        );
    }
}
