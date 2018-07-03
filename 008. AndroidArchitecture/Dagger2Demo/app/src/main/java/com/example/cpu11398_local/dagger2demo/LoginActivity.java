package com.example.cpu11398_local.dagger2demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.cpu11398_local.dagger2demo.data.UserManager;
import com.example.cpu11398_local.dagger2demo.utils.Utils;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = "LoginActivity";

    @Inject
    public UserManager userManager;

    @BindView(R.id.edt_login_username)
    EditText edt_login_username;
    @BindView(R.id.edt_login_password)
    EditText edt_login_password;
    @BindView(R.id.btn_login)
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Utils.showLog(TAG, "onCreate");

        btn_login.setOnClickListener(this);

        MainActivity.getAppComponent().inject(this);
    }

    @Override
    public void onClick(View v) {
        Utils.showLog(TAG, "onClick");
        userManager.login(
                edt_login_username.getText().toString(),
                edt_login_password.getText().toString()
        );
    }
}
