package com.example.cpu11398_local.etalk.presentation.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    /**
     * Determine activity is recreating or not.
     * Value is assigned to {@code false} in {@link #onCreate(Bundle)} and
     * reassigned to {@code true} in {@link #onSaveInstanceState(Bundle)} if
     * activity recreating.
     */
    private boolean isReCreating;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onDataBinding();
        isReCreating = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        onSubscribeViewModel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        onUnSubscribeViewModel();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return onSaveViewModel();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        isReCreating = true;
    }

    @Override
    protected void onDestroy() {
        if (!isReCreating) {
            onEndTaskViewModel();
        }
        super.onDestroy();
    }

    /**
     * Allow subclass of this class can detect when it is recreating.
     * @return
     */
    protected boolean isReCreating() {
        return isReCreating;
    }

    /**
     * Called in {@link #onCreate(Bundle)} to bind data between view and viewModel.
     */
    public abstract void onDataBinding();

    /**
     * Called in {@link #onStart()} to subscribe observer to viewModel.
     */
    public abstract void onSubscribeViewModel();

    /**
     * Called in {@link #onStop()} to unsubscribe observer in viewModel.
     */
    public abstract void onUnSubscribeViewModel();

    /**
     * Called in {@link #onRetainCustomNonConfigurationInstance()} to save viewModel.
     * @return Object is viewModel to save.
     */
    public abstract Object onSaveViewModel();

    /**
     * Called in {@link #onDestroy()} to inform viewModel stop usecase if activity really finish.
     */
    public abstract void onEndTaskViewModel();
}
