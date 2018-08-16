package com.example.cpu11398_local.etalk.domain.interactor;

public interface Usecase {

    /**
     * Execute an usecase with an observer and a data.
     * @param observer listen event from usecase.
     * @param params data to execute usecase.
     */
    void execute(Object observer, Object... params);

    /**
     * Stop executing usecase when don't want to listen event from it
     * in future.
     */
    void endTask();
}
