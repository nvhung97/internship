package com.example.cpu11398_local.cleanarchitecturedemo.presentation.di;

import android.content.Context;
import com.example.cpu11398_local.cleanarchitecturedemo.data.local.mapper.PaperMapper;
import com.example.cpu11398_local.cleanarchitecturedemo.data.local.service.PaperDB;
import com.example.cpu11398_local.cleanarchitecturedemo.data.repository.Repository;
import com.example.cpu11398_local.cleanarchitecturedemo.data.repository.data_source.LocalSource;
import com.example.cpu11398_local.cleanarchitecturedemo.domain.executor.TaskExecutor;
import com.example.cpu11398_local.cleanarchitecturedemo.domain.interactor.UseCaseLogin;
import com.example.cpu11398_local.cleanarchitecturedemo.domain.interactor.UseCaseRegister;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.view_model.LoginViewModel;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.view_model.RegisterViewModel;

import java.util.concurrent.Executor;

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
    public PaperMapper providePaperMapper() {
        return new PaperMapper();
    }

    @Provides
    public Context provideContext() {
        return context;
    }

    @Provides
    public PaperDB providePaperDB(Context context) {
        return new PaperDB(context);
    }

    @Provides
    public LocalSource provideLocalSource(PaperDB paperDB) {
        return new LocalSource(paperDB);
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
    public Repository provideRepository(LocalSource localSource, PaperMapper paperMapper) {
        return new Repository(localSource, paperMapper);
    }

    @Provides
    @Singleton
    public UseCaseRegister provideUseCaseRegister(Executor executor,
                                                  Scheduler scheduler,
                                                  CompositeDisposable compositeDisposable,
                                                  Repository repository) {
        return new UseCaseRegister(
                executor,
                scheduler,
                compositeDisposable,
                repository
        );
    }

    @Provides
    public RegisterViewModel provideRegisterViewModel(UseCaseRegister useCaseRegister) {
        return new RegisterViewModel(useCaseRegister);
    }

    @Provides
    @Singleton
    public UseCaseLogin provideUseCaseLogin(Executor executor,
                                            Scheduler scheduler,
                                            CompositeDisposable compositeDisposable,
                                            Repository repository) {
        return new UseCaseLogin(
                executor,
                scheduler,
                compositeDisposable,
                repository
        );
    }

    @Provides
    public LoginViewModel provideLoginViewModel(UseCaseLogin useCaseLogin) {
        return new LoginViewModel(useCaseLogin);
    }
}
