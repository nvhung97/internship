package com.example.cpu11398_local.mvvm.view;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.cpu11398_local.mvvm.databinding.ActivityLoginViewBinding;
import com.example.cpu11398_local.mvvm.R;
import com.example.cpu11398_local.mvvm.view_model.ViewModel;

public class LoginView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDataBinding();
    }

    private void initDataBinding() {
        ActivityLoginViewBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login_view);
        ViewModel viewModel = new ViewModel();
        binding.setViewmodel(viewModel);
    }
}
