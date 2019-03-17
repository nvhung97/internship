package com.example.keyboarddemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import android.widget.PopupWindow;
import java.lang.reflect.Field;
import java.util.List;

public class CustomKeyboardView extends KeyboardView implements KeyboardView.OnKeyboardActionListener {

    private final int DOUBLE_PRESS_TIME = 250;

    private final int KEY_SPACE     = 32;
    private final int KEY_SHIFT     = -1;
    private final int KEY_NUMERIC   = -2;
    private final int KEY_DONE      = -4;
    private final int KEY_DELETE    = -5;
    private final int KEY_LANGUAGE  = -101;
    private final int KEY_SYMBOL    = -102;
    private final int KEY_QWERTY    = -103;

    private Keyboard keyboardQwerty;
    private Keyboard keyboardNumeric;
    private Keyboard keyboardSymbol;

    private boolean isPopupOnScreen   = false;
    private boolean isLanguagePressed = false;
    private boolean isSpacePressed    = false;
    private boolean isShiftHold       = false;
    private long    timeShiftPressed  = 0;
    private int     keyHeight         = 0;

    private InputConnection inputConnection;
    private MotionEvent     motionEvent;

    public CustomKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        keyHeight       = getResources().getDimensionPixelSize(R.dimen.key_height);
        keyboardQwerty  = new Keyboard(context, R.xml.qwerty);
        keyboardNumeric = new Keyboard(context, R.xml.numeric);
        keyboardSymbol  = new Keyboard(context, R.xml.symbol);
        setKeyboard(keyboardQwerty);
        setOnKeyboardActionListener(this);
    }

    /**
     * Connect the keyboard with given {@code editText}.
     * @param editText receive text from this keyboard.
     */
    public void with(EditText editText) {
        inputConnection = editText.onCreateInputConnection(new EditorInfo());
    }

    @Override
    public void onPress(int primaryCode) {
        setPreviewEnabled(false);
        switch (primaryCode) {
            case KEY_SPACE:
                isSpacePressed = true;
                invalidate();
                break;
            case KEY_DELETE:
                // Do nothing here
                break;
            case KEY_SHIFT:
                if (isShifted()) {
                    if (isShiftHold) {
                        setShifted(false);
                        invalidateAllKeys();
                        updateKeyIcon(KEY_SHIFT, R.drawable.ic_shift);
                        isShiftHold = false;
                    } else if (System.currentTimeMillis() - timeShiftPressed <= DOUBLE_PRESS_TIME) {
                        updateKeyIcon(KEY_SHIFT, R.drawable.ic_shift_hold);
                        isShiftHold = true;
                    } else {
                        setShifted(false);
                        invalidateAllKeys();
                        updateKeyIcon(KEY_SHIFT, R.drawable.ic_shift);
                    }
                } else {
                    timeShiftPressed = System.currentTimeMillis();
                    setShifted(true);
                    invalidateAllKeys();
                    updateKeyIcon(KEY_SHIFT, R.drawable.ic_shift_hit);
                }
                break;
            case KEY_NUMERIC:
                // Do nothing here
                break;
            case KEY_QWERTY:
                // Do nothing here
                break;
            case KEY_SYMBOL:
                // Do nothing here
                break;
            case KEY_DONE:
                updateKeyIcon(KEY_DONE, R.drawable.ic_done_hit);
                break;
            case KEY_LANGUAGE:
                isLanguagePressed = true;
                invalidate();
                break;
            default:
                setPreviewEnabled(true);
        }
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        switch (primaryCode) {
            case KEY_SPACE:
                inputConnection.commitText(" ", 1);
                break;
            case KEY_DELETE:
                CharSequence selectedText = inputConnection.getSelectedText(0);
                if (TextUtils.isEmpty(selectedText)) {
                    inputConnection.deleteSurroundingText(1, 0);
                } else {
                    inputConnection.commitText("", 1);
                }
                break;
            case KEY_SHIFT:
                // Do nothing here
                break;
            case KEY_NUMERIC:
                // Do nothing here
                break;
            case KEY_QWERTY:
                // Do nothing here
                break;
            case KEY_SYMBOL:
                // Do nothing here
                break;
            case KEY_DONE:
                //TODO
                break;
            case KEY_LANGUAGE:
                //TODO
                break;
            default:
                if (!isShifted() || !Character.isLetter(primaryCode) || !Character.isLowerCase(primaryCode)) {
                    inputConnection.commitText(String.valueOf((char)primaryCode), 1);
                } else {
                    inputConnection.commitText(String.valueOf((char)Character.toUpperCase(primaryCode)), 1);
                }
        }
    }

    @Override
    public void onRelease(int primaryCode) {
        switch (primaryCode) {
            case KEY_SPACE:
                isSpacePressed = false;
                invalidate();
                break;
            case KEY_DELETE:
                // Do nothing here
                break;
            case KEY_SHIFT:
                // Do nothing here
                break;
            case KEY_NUMERIC:
                setKeyboard(keyboardNumeric);
                break;
            case KEY_QWERTY:
                setKeyboard(keyboardQwerty);
                break;
            case KEY_SYMBOL:
                setKeyboard(keyboardSymbol);
                break;
            case KEY_DONE:
                updateKeyIcon(KEY_DONE, R.drawable.ic_done);
                break;
            case KEY_LANGUAGE:
                isLanguagePressed = false;
                invalidate();
                break;
            default:
                if (isShifted() && !isShiftHold) {
                    setShifted(false);
                    invalidateAllKeys();
                    updateKeyIcon(KEY_SHIFT, R.drawable.ic_shift);
                }
        }
    }

    @Override
    protected boolean onLongPress(Key popupKey) {
        if (isPopupOnScreen = super.onLongPress(popupKey)) {
            onTouchEvent(motionEvent);
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(final MotionEvent me) {
        if (isPopupOnScreen) {
            try {
                int action = MotionEvent.ACTION_DOWN;
                if (me.getAction() == MotionEvent.ACTION_UP) {
                    action = MotionEvent.ACTION_UP;
                    isPopupOnScreen = false;
                }
                int[] popupLocation = new int[2];
                PopupWindow popupWindow = getPopupWindow();
                popupWindow.getContentView().getLocationOnScreen(popupLocation);
                popupWindow.getContentView().onTouchEvent(MotionEvent.obtain(
                        SystemClock.uptimeMillis(),
                        SystemClock.uptimeMillis(),
                        action,
                        me.getRawX() - popupLocation[0],
                        me.getRawY() - popupLocation[1] - keyHeight,
                        0
                ));
            } catch (Exception e) {
                new Handler().postDelayed(() -> onTouchEvent(me), 10);
            }
        }
        return super.onTouchEvent(motionEvent = me);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (isSpacePressed) {
            drawKeyBackground(KEY_SPACE, R.drawable.bg_space_hit, canvas);
        } else {
            drawKeyBackground(KEY_SPACE, R.drawable.bg_space, canvas);
        }
        if (isLanguagePressed) {
            drawKeyBackground(KEY_LANGUAGE, R.drawable.bg_key_hit, canvas);
        }
        super.onDraw(canvas);
    }

    private void drawKeyBackground(int keyCode, int drawableId, Canvas canvas) {
        Key key = getKey(keyCode);
        Drawable drawable = getContext().getDrawable(drawableId);
        drawable.setState(key.getCurrentDrawableState());
        if (keyCode == KEY_SPACE) {
            drawable.setBounds(
                    key.x + key.height / 4,
                    key.y + key.height / 4,
                    key.x + key.width - key.height / 4,
                    key.y + key.height * 3 / 4
            );
        } else {
            drawable.setBounds(
                    key.x,
                    key.y,
                    key.x + key.width,
                    key.y + key.height
            );
        }
        drawable.draw(canvas);
    }

    private void updateKeyIcon(int keyCode, int drawableId) {
        getKey(keyCode).icon = getContext().getDrawable(drawableId);
    }

    private Key getKey(int code) {
        List<Key> keys = getKeyboard().getKeys();
        for (Key key : keys) {
            if (key.codes[0] == code) {
                return key;
            }
        }
        return null;
    }

    /**
     * Get PopupWidow from super class to dismiss is when user hit any key.
     * @return current popupWindow.
     */
    private PopupWindow getPopupWindow() {
        try {
            Field field = getClass()
                    .getSuperclass()
                    .getDeclaredField("mPopupKeyboard");
            field.setAccessible(true);
            return (PopupWindow)field.get(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onText(CharSequence text) {
        // Do nothing here
    }

    @Override
    public void swipeLeft() {
        // Do nothing here
    }

    @Override
    public void swipeUp() {
        // Do nothing here
    }

    @Override
    public void swipeRight() {
        // Do nothing here
    }

    @Override
    public void swipeDown() {
        // Do nothing here
    }
}
