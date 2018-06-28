package com.example.cpu11398_local.mvp.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.cpu11398_local.mvp.R;
import com.example.cpu11398_local.mvp.presenter.ILoginPresenter;
import com.example.cpu11398_local.mvp.presenter.LoginPresenter;
import com.example.cpu11398_local.mvp.utils.Utils;

public class LoginView extends AppCompatActivity implements ILoginView, View.OnClickListener{

    private final String TAG = "LoginView";

    private EditText edt_username;
    private EditText edt_password;
    private Button   btn_login;

    private ILoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);

        Utils.showLog(TAG, "onCreate");

        initViewObject();

        initLoginPresenter();
    }

    private void initViewObject() {
        Utils.showLog(TAG, "initViewObject");
        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        btn_login    = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(this);
    }

    private void initLoginPresenter() {
        Utils.showLog(TAG, "initLoginPresenter");
        loginPresenter = (ILoginPresenter)getLastCustomNonConfigurationInstance();
        if (loginPresenter == null) {
            loginPresenter = new LoginPresenter(this);
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        Utils.showLog(TAG, "onRetainCustomNonConfigurationInstance");
        return loginPresenter;
    }

    @Override
    public void onUsernameEmpty() {
        Utils.showLog(TAG, "onUsernameEmpty");
        edt_username.setHintTextColor(getResources().getColor(R.color.colorRed));
    }

    @Override
    public void onPasswordEmpty() {
        Utils.showLog(TAG, "onPasswordEmpty");
        edt_password.setHintTextColor(getResources().getColor(R.color.colorRed));
    }

    @Override
    public void onLoginSuccess() {
        Utils.showLog(TAG, "onLoginSuccess");
        Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginFailed() {
        Utils.showLog(TAG, "onLoginFailed");
        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        Utils.showLog(TAG, "onClick");
        switch (v.getId()) {
            case R.id.btn_login:
                loginPresenter.performLogin(
                        edt_username.getText().toString(),
                        edt_password.getText().toString()
                );
                break;
        }
    }
}
