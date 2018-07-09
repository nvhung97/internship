package com.example.cpu11398_local.cleanarchitecturedemo.presentation.di;

import com.example.cpu11398_local.cleanarchitecturedemo.presentation.view.ContentActivity;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.view.LoginActivity;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.view.RegisterActivity;
import javax.inject.Singleton;
import dagger.Component;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {
    void inject(LoginActivity loginActivity);
    void inject(RegisterActivity registerActivity);
    void inject(ContentActivity contentActivity);
}
