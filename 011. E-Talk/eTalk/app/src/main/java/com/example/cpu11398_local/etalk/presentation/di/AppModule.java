package com.example.cpu11398_local.etalk.presentation.di;

import android.content.Context;
import com.example.cpu11398_local.etalk.data.netword.FirebaseDB;
import com.example.cpu11398_local.etalk.data.repository.UserRepository;
import com.example.cpu11398_local.etalk.data.repository.data_source.NetworkSource;
import com.example.cpu11398_local.etalk.data.repository.implement.UserRepositoryImpl;
import com.example.cpu11398_local.etalk.domain.executor.TaskExecutor;
import com.example.cpu11398_local.etalk.domain.interactor.LoginUsecase;
import com.example.cpu11398_local.etalk.domain.interactor.Usecase;
import com.example.cpu11398_local.etalk.presentation.view_model.chat.ChatViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.content.ContentViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.login.LoginViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.register.RegisterViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import java.util.concurrent.Executor;
import javax.inject.Named;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

@Module
public class AppModule {

    private Context context;

    public AppModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    public Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    public Executor provideExecutor() {
        return new TaskExecutor();
    }

    @Provides
    public Scheduler provideScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    public CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }


    /**********************************************************************************************
     *                                    DATA SOURCE
     **********************************************************************************************/

    @Provides
    public NetworkSource provideNetworkSource() {
        return new FirebaseDB();
    }


    /**********************************************************************************************
     *                                     REPOSITORY
     **********************************************************************************************/

    @Provides
    @Singleton
    public UserRepository provideUserRepository(NetworkSource networkSource) {
        return new UserRepositoryImpl(networkSource);
    }


    /**********************************************************************************************
     *                                       USECASE
     **********************************************************************************************/

    @Provides
    @Named("LoginUsecase")
    public Usecase provideLoginUsecase(Executor executor,
                                       Scheduler scheduler,
                                       CompositeDisposable compositeDisposable,
                                       UserRepository userRepository) {
        return new LoginUsecase(
                executor,
                scheduler,
                compositeDisposable,
                userRepository
        );
    }


    /**********************************************************************************************
     *                                     VIEW-MODEL
     **********************************************************************************************/

    @Provides
    @Named("ContentViewModel")
    public ViewModel provideContentViewModel(Context context) {
        return new ContentViewModel(context);
    }

    @Provides
    @Named("ChatViewModel")
    public ViewModel provideChatViewModel(Context context) {
        return new ChatViewModel(context);
    }

    @Provides
    @Named("LoginViewModel")
    public ViewModel provideLoginViewModel(Context context, @Named("LoginUsecase") Usecase usecase) {
        return new LoginViewModel(context, usecase);
    }

    @Provides
    @Named("RegisterViewModel")
    public ViewModel provideRegisterViewModel(Context context) {
        return new RegisterViewModel(context);
    }
}
