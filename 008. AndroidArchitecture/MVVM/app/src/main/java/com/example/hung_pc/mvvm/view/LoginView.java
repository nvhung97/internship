package com.example.hung_pc.mvvm.view;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.example.hung_pc.mvvm.R;
import com.example.hung_pc.mvvm.databinding.ActivityLoginViewBinding;
import com.example.hung_pc.mvvm.utils.Utils;
import com.example.hung_pc.mvvm.view_model.ViewModel;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LoginView extends AppCompatActivity {

    private final String TAG = "LoginView";

    private ActivityLoginViewBinding binding;
    private ViewModel                viewModel;
    private Disposable               disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.showLog(TAG, "onCreate");

        initDataBinding();
    }

    private void initDataBinding(){
        Utils.showLog(TAG, "initDataBinding");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_view);
        viewModel = (ViewModel) getLastCustomNonConfigurationInstance();
        if (viewModel == null) {
            viewModel = new ViewModel();
        }
        binding.setViewModel(viewModel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.subscribeObserver(
                new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        onLoginCallback(aBoolean);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }
        );
    }

    private void onLoginCallback(boolean isSuccess){
        Utils.showLog(TAG, "onLoginCallback");
        if (isSuccess){
            Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        Utils.showLog(TAG, "onRetainCustomNonConfigurationInstance");
        return viewModel;
    }

    @Override
    protected void onStop() {
        super.onStop();
        disposable.dispose();
    }
}
