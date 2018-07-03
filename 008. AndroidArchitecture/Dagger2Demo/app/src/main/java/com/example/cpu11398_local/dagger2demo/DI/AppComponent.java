package com.example.cpu11398_local.dagger2demo.DI;

import com.example.cpu11398_local.dagger2demo.ChangeActivity;
import com.example.cpu11398_local.dagger2demo.LoginActivity;
import javax.inject.Singleton;
import dagger.Component;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {

    void inject(LoginActivity mainActivity);
    void inject(ChangeActivity mainActivity);
}
