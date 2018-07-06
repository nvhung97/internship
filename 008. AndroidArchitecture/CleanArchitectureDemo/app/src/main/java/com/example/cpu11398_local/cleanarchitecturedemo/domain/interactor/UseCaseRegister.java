package com.example.cpu11398_local.cleanarchitecturedemo.domain.interactor;

import com.example.cpu11398_local.cleanarchitecturedemo.data.repository.Repository;
import com.example.cpu11398_local.cleanarchitecturedemo.presentation.model.User;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Hung-pc on 7/6/2018.
 */

public class UseCaseRegister{

    private Repository repository;
    private PublishSubject<Boolean> publishSubject = PublishSubject.create();

    public UseCaseRegister(Repository repository) {
        this.repository = repository;
    }

    public void execute(Observer<Boolean> observer, User newUser) {
        repository.putData(newUser).subscribe(observer);
    }
}
