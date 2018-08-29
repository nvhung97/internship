package com.example.cpu11398_local.etalk.presentation.view;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import com.example.cpu11398_local.etalk.utils.Tool;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class BaseActivity extends AppCompatActivity {

    /**
     * Determine activity is recreating or not.
     * Value is assigned to {@code false} in {@link #onCreate(Bundle)} and
     * reassigned to {@code true} in {@link #onSaveInstanceState(Bundle)} if
     * activity recreating.
     */
    private boolean isReCreating;

    /**
     * Container contain all {@code View} in activity need to consider for hide keyboard
     * when windows focus outside them.
     */
    private ArrayList<View> lstView         = new ArrayList<>();
    private int[]           viewLocation    = new int[2];
    private Rect            viewRect        = new Rect();

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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            int     locationX        = (int)ev.getX();
            int     locationY        = (int)ev.getY();
            for (View view : lstView) {
                if (view.isEnabled()) {
                    view.getLocationOnScreen(viewLocation);
                    viewRect.set(
                            viewLocation[0],
                            viewLocation[1],
                            viewLocation[0] + view.getWidth(),
                            viewLocation[1] + view.getHeight()
                    );
                    if (viewRect.contains(locationX, locationY)) {
                        return super.dispatchTouchEvent(ev);
                    }
                }
            }
            Tool.hideSoftKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * Allow subclass of this class can detect when it is recreating.
     * @return
     */
    protected boolean isReCreating() {
        return isReCreating;
    }

    /**
     * Allow subclass provides {@code View}(s) to consider for hide keyboard when
     * windows focus outside them.
     * @param views
     */
    protected void addControlKeyboardView(View... views) {
        lstView.addAll(Arrays.asList(views));
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
