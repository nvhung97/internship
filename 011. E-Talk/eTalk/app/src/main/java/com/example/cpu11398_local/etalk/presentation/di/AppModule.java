package com.example.cpu11398_local.etalk.presentation.di;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.example.cpu11398_local.etalk.presentation.view.content.pager_adapter.ContentPagerAdapter;
import com.example.cpu11398_local.etalk.presentation.view_model.ChatViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.ContentViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.LoginViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.RegisterViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import javax.inject.Named;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Context         context;
    private FragmentManager fragmentManager;

    public AppModule(Context context) {
        this.context         = context.getApplicationContext();
        this.fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
    }

    @Provides
    public Context provideContext() {
        return context;
    }

    @Provides
    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    @Provides
    @Named("ContentViewModel")
    public ViewModel provideContentViewModel(Context context) {
        return new ContentViewModel(context);
    }

    @Provides
    @Named("ContentPagerAdapter")
    public FragmentStatePagerAdapter provideContentPagerAdapter(FragmentManager fragmentManager) {
        return new ContentPagerAdapter(fragmentManager);
    }

    @Provides
    @Named("ChatViewModel")
    public ViewModel provideChatViewModel(Context context) {
        return new ChatViewModel(context);
    }

    @Provides
    @Named("LoginViewModel")
    public ViewModel provideLoginViewModel(Context context) {
        return new LoginViewModel(context);
    }

    @Provides
    @Named("RegisterViewModel")
    public ViewModel provideRegisterViewModel(Context context) {
        return new RegisterViewModel(context);
    }
}
