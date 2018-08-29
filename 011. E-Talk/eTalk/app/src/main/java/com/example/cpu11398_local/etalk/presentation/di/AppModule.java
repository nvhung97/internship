package com.example.cpu11398_local.etalk.presentation.di;

import android.content.Context;
import com.example.cpu11398_local.etalk.data.cache.SharedPreferencesDB;
import com.example.cpu11398_local.etalk.data.network.FirebaseDB;
import com.example.cpu11398_local.etalk.data.repository.UserRepository;
import com.example.cpu11398_local.etalk.data.repository.data_source.CacheSource;
import com.example.cpu11398_local.etalk.data.repository.data_source.NetworkSource;
import com.example.cpu11398_local.etalk.data.repository.implement.UserRepositoryImpl;
import com.example.cpu11398_local.etalk.domain.executor.TaskExecutor;
import com.example.cpu11398_local.etalk.domain.interactor.GetUserInfoUsecase;
import com.example.cpu11398_local.etalk.domain.interactor.LoginUsecase;
import com.example.cpu11398_local.etalk.domain.interactor.LogoutUsecase;
import com.example.cpu11398_local.etalk.domain.interactor.RegisterUsecase;
import com.example.cpu11398_local.etalk.domain.interactor.UpdateUserInfoUsecase;
import com.example.cpu11398_local.etalk.domain.interactor.Usecase;
import com.example.cpu11398_local.etalk.domain.interactor.WelcomeUsecase;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModelCallback;
import com.example.cpu11398_local.etalk.presentation.view_model.chat.ChatViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.content.ContentViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.content.MoreViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.login.LoginViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.profile.ProfileViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.register.RegisterViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.ViewModel;
import com.example.cpu11398_local.etalk.presentation.view_model.welcome.WelcomeViewModel;
import com.example.cpu11398_local.etalk.utils.NetworkChangeReceiver;
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

    private Context             context;
    private ViewModelCallback   viewModelCallback;

    public AppModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    public Context provideContext() {
        return context;
    }

    @Provides
    public ViewModelCallback provideViewModelCallback() {
        return viewModelCallback;
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

    @Provides
    public NetworkChangeReceiver provideNetworkChangeReceiver() {
        return new NetworkChangeReceiver();
    }


    /**********************************************************************************************
     *                                    DATA SOURCE
     **********************************************************************************************/

    @Provides
    public NetworkSource provideNetworkSource() {
        return new FirebaseDB();
    }

    @Provides
    public CacheSource provideCacheSource(Context context) {
        return new SharedPreferencesDB(context);
    }


    /**********************************************************************************************
     *                                     REPOSITORY
     **********************************************************************************************/

    @Provides
    @Singleton
    public UserRepository provideUserRepository(NetworkSource networkSource,
                                                CacheSource cacheSource) {
        return new UserRepositoryImpl(networkSource, cacheSource);
    }


    /**********************************************************************************************
     *                                       USECASE
     **********************************************************************************************/

    @Provides
    @Named("WelcomeUsecase")
    public Usecase provideWelcomeUsecase(Executor executor,
                                       Scheduler scheduler,
                                       CompositeDisposable compositeDisposable,
                                       UserRepository userRepository) {
        return new WelcomeUsecase(
                executor,
                scheduler,
                compositeDisposable,
                userRepository
        );
    }

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

    @Provides
    @Named("RegisterUsecase")
    public Usecase provideRegisterUsecase(Executor executor,
                                          Scheduler scheduler,
                                          CompositeDisposable compositeDisposable,
                                          UserRepository userRepository) {
        return new RegisterUsecase(
                executor,
                scheduler,
                compositeDisposable,
                userRepository
        );
    }

    @Provides
    @Named("GetUserInfoUsecase")
    public Usecase provideGetUserInfoUsecase(Executor executor,
                                             Scheduler scheduler,
                                             CompositeDisposable compositeDisposable,
                                             UserRepository userRepository) {
        return new GetUserInfoUsecase(
                executor,
                scheduler,
                compositeDisposable,
                userRepository
        );
    }

    @Provides
    @Named("UpdateUserInfoUsecase")
    public Usecase provideUpdateUserInfoUsecase(Executor executor,
                                                Scheduler scheduler,
                                                CompositeDisposable compositeDisposable,
                                                UserRepository userRepository) {
        return new UpdateUserInfoUsecase(
                executor,
                scheduler,
                compositeDisposable,
                userRepository
        );
    }

    @Provides
    @Named("LogoutUsecase")
    public Usecase provideLogoutUsecase(Executor executor,
                                        Scheduler scheduler,
                                        CompositeDisposable compositeDisposable,
                                        UserRepository userRepository) {
        return new LogoutUsecase(
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
    @Named("WelcomeViewModel")
    public ViewModel provideWelcomeViewModel(Context context,
                                             @Named("WelcomeUsecase") Usecase usecase) {
        return new WelcomeViewModel(context, usecase);
    }

    @Provides
    @Named("LoginViewModel")
    public ViewModel provideLoginViewModel(Context context,
                                           @Named("LoginUsecase") Usecase usecase,
                                           NetworkChangeReceiver receiver) {
        return new LoginViewModel(context, usecase, receiver);
    }

    @Provides
    @Named("RegisterViewModel")
    public ViewModel provideRegisterViewModel(Context context,
                                              @Named("RegisterUsecase") Usecase usecase,
                                              NetworkChangeReceiver receiver) {
        return new RegisterViewModel(context, usecase, receiver);
    }

    @Provides
    @Named("ContentViewModel")
    public ViewModel provideContentViewModel(Context context,
                                             @Named("LogoutUsecase") Usecase usecase,
                                             NetworkChangeReceiver receiver) {
        ContentViewModel contentViewModel = new ContentViewModel(context, usecase, receiver);
        viewModelCallback = contentViewModel;
        return contentViewModel;
    }

    @Provides
    @Named("MoreViewModel")
    public ViewModel provideMoreViewModel(Context context,
                                          ViewModelCallback viewModelCallback,
                                          @Named("GetUserInfoUsecase") Usecase usecase) {
        return new MoreViewModel(context, viewModelCallback, usecase);
    }

    @Provides
    @Named("ProfileViewModel")
    public ViewModel provideProfileViewModel(Context context,
                                             @Named("GetUserInfoUsecase") Usecase usecase1,
                                             @Named("UpdateUserInfoUsecase") Usecase usecase2,
                                             NetworkChangeReceiver receiver) {
        return new ProfileViewModel(context, usecase1, usecase2, receiver);
    }

    @Provides
    @Named("ChatViewModel")
    public ViewModel provideChatViewModel(Context context) {
        return new ChatViewModel(context);
    }
}