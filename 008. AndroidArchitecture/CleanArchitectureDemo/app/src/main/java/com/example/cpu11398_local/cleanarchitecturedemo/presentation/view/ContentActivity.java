package com.example.cpu11398_local.cleanarchitecturedemo.presentation.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import com.example.cpu11398_local.cleanarchitecturedemo.R;
import com.example.cpu11398_local.cleanarchitecturedemo.databinding.ActivityContentBinding;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.di.AppComponent;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.di.AppModule;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.di.DaggerAppComponent;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.view_model.ContentViewModel;
import javax.inject.Inject;

public class ContentActivity extends BaseActivity {

    private static AppComponent appComponent;

    @Inject
    public ContentViewModel contentViewModel;

    private ActivityContentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    void onDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_content);
        contentViewModel = (ContentViewModel) getLastCustomNonConfigurationInstance();
        if (contentViewModel == null) {
            ContentActivity.getAppComponent(this).inject(this);
        }
        binding.setContentViewModel(contentViewModel);
    }

    @Override
    void onSubscribeViewModel() {

    }

    @Override
    void onUnSubscribeViewModel() {

    }

    @Override
    Object onSaveViewModel() {
        return contentViewModel;
    }

    @Override
    void onEndTaskViewModel() {
        contentViewModel.endTask();
    }

    public static AppComponent getAppComponent(Context context) {
        if (appComponent == null) {
            appComponent = DaggerAppComponent
                    .builder()
                    .appModule(new AppModule(context))
                    .build();
        }
        return appComponent;
    }
}
