package com.example.cpu11398_local.etalk.presentation.di;

import com.example.cpu11398_local.etalk.presentation.view.chat.ChatActivity;
import com.example.cpu11398_local.etalk.presentation.view.content.ContentActivity;
import com.example.cpu11398_local.etalk.presentation.view.login.LoginActivity;
import com.example.cpu11398_local.etalk.presentation.view.register.RegisterActivity;
import dagger.Component;

@Component(modules = {AppModule.class})
//@Singleton
public interface AppComponent {
    void inject(LoginActivity loginActivity);
    void inject(RegisterActivity registerActivity);
    void inject(ContentActivity contentActivity);
    void inject(ChatActivity chatActivity);
}
