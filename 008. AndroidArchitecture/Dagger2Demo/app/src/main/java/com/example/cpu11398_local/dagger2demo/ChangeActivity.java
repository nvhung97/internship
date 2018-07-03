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

public class ChangeActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = "ChangeActivity";

    @Inject
    public UserManager userManager;

    @BindView(R.id.edt_change_username)
    EditText edt_change_username;
    @BindView(R.id.edt_change_password)
    EditText edt_change_password;
    @BindView(R.id.btn_change)
    Button btn_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        ButterKnife.bind(this);
        Utils.showLog(TAG, "onCreate");

        btn_change.setOnClickListener(this);

        MainActivity.getAppComponent().inject(this);
    }

    @Override
    public void onClick(View v) {
        Utils.showLog(TAG, "onClick");
        userManager.change(
                edt_change_username.getText().toString(),
                edt_change_password.getText().toString()
        );
    }
}
