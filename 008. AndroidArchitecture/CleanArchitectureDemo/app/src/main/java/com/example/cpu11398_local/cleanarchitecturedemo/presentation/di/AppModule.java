package com.example.cpu11398_local.cleanarchitecturedemo.presentation.di;

import android.content.Context;
import com.example.cpu11398_local.cleanarchitecturedemo.data.cache.SharedPreferencesManager;
import com.example.cpu11398_local.cleanarchitecturedemo.data.local.paper.PaperDB;
import com.example.cpu11398_local.cleanarchitecturedemo.data.repository.data_source.CacheSource;
import com.example.cpu11398_local.cleanarchitecturedemo.data.repository.data_source.LocalSource;
import com.example.cpu11398_local.cleanarchitecturedemo.data.repository.user_repository.UserRepository;
import com.example.cpu11398_local.cleanarchitecturedemo.data.repository.user_repository.UserRepositoryImpl;
import com.example.cpu11398_local.cleanarchitecturedemo.domain.executor.TaskExecutor;
import com.example.cpu11398_local.cleanarchitecturedemo.domain.interactor.UseCase;
import com.example.cpu11398_local.cleanarchitecturedemo.domain.interactor.UseCaseGetUserInfo;
import com.example.cpu11398_local.cleanarchitecturedemo.domain.interactor.UseCaseLogin;
import com.example.cpu11398_local.cleanarchitecturedemo.domain.interactor.UseCaseRegister;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.model.User;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.view_model.ContentViewModel;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.view_model.LoginViewModel;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.view_model.RegisterViewModel;
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
    public LocalSource provideLocalSource(Context context) {
        return new PaperDB(context);
    }

    @Provides
    public CacheSource provideCacheSource(Context context) {
        return new SharedPreferencesManager(context);
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
    @Singleton
    public UserRepository provideUserRepository(LocalSource localSource, CacheSource cacheSource) {
        return new UserRepositoryImpl(localSource, cacheSource);
    }

    @Provides @Named("UseCaseRegister")
    public UseCase<Void, User> provideUseCaseRegister(Executor executor,
                                                      Scheduler scheduler,
                                                      CompositeDisposable compositeDisposable,
                                                      UserRepository userRepository) {
        return new UseCaseRegister(
                executor,
                scheduler,
                compositeDisposable,
                userRepository
        );
    }

    @Provides
    public RegisterViewModel provideRegisterViewModel(@Named("UseCaseRegister") UseCase<Void, User> useCaseRegister) {
        return new RegisterViewModel(useCaseRegister);
    }

    @Provides @Named("UseCaseLogin")
    public UseCase<Boolean, User> provideUseCaseLogin(Executor executor,
                                                      Scheduler scheduler,
                                                      CompositeDisposable compositeDisposable,
                                                      UserRepository userRepository) {
        return new UseCaseLogin(
                executor,
                scheduler,
                compositeDisposable,
                userRepository
        );
    }

    @Provides
    public LoginViewModel provideLoginViewModel(@Named("UseCaseLogin") UseCase<Boolean, User> useCaseLogin) {
        return new LoginViewModel(useCaseLogin);
    }

    @Provides @Named("UseCaseGetUserInfo")
    public UseCase<User, Void> UseCaseGetUserInfo(Executor executor,
                                                  Scheduler scheduler,
                                                  CompositeDisposable compositeDisposable,
                                                  UserRepository userRepository) {
        return new UseCaseGetUserInfo(
                executor,
                scheduler,
                compositeDisposable,
                userRepository
        );
    }

    @Provides
    public ContentViewModel provideContentViewModel(@Named("UseCaseGetUserInfo") UseCase<User, Void> useCaseGetUserInfo) {
        return new ContentViewModel(useCaseGetUserInfo);
    }
}
