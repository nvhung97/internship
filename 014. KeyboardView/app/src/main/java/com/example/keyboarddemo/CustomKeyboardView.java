package com.example.keyboarddemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
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

    private boolean isSpacePressed   = false;
    private boolean isShiftHold      = false;
    private long    timeShiftPressed = 0;


    public CustomKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        keyboardQwerty  = new Keyboard(context, R.xml.qwerty);
        keyboardNumeric = new Keyboard(context, R.xml.numeric);
        keyboardSymbol  = new Keyboard(context, R.xml.symbol);
        setKeyboard(keyboardQwerty);
        setOnKeyboardActionListener(this);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isSpacePressed) {
            drawKeyBackground(getKey(KEY_SPACE), R.drawable.key_space_hit, canvas);
        } else {
            drawKeyBackground(getKey(KEY_SPACE), R.drawable.key_space, canvas);
        }
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
                        updateKeyIcon(KEY_SHIFT, R.drawable.key_shift);
                        isShiftHold = false;
                    } else if (System.currentTimeMillis() - timeShiftPressed <= DOUBLE_PRESS_TIME) {
                        updateKeyIcon(KEY_SHIFT, R.drawable.key_shift_hold);
                        isShiftHold = true;
                    } else {
                        setShifted(false);
                        invalidateAllKeys();
                        updateKeyIcon(KEY_SHIFT, R.drawable.key_shift);
                    }
                } else {
                    timeShiftPressed = System.currentTimeMillis();
                    setShifted(true);
                    invalidateAllKeys();
                    updateKeyIcon(KEY_SHIFT, R.drawable.key_shift_hit);
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
                updateKeyIcon(KEY_DONE, R.drawable.key_done_hit);
                break;
            case KEY_LANGUAGE:
                //TODO
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
                if (isShifted() && primaryCode >= 'a' && primaryCode <= 'z') {
                    inputConnection.commitText(String.valueOf((char)(primaryCode - 32)), 1);
                } else {
                    inputConnection.commitText(String.valueOf((char)primaryCode), 1);
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
                updateKeyIcon(KEY_DONE, R.drawable.key_done);
                break;
            case KEY_LANGUAGE:
                //TODO
                break;
            default:
                if (isShifted() && !isShiftHold) {
                    setShifted(false);
                    invalidateAllKeys();
                    updateKeyIcon(KEY_SHIFT, R.drawable.key_shift);
                }
        }
    }

    private InputConnection inputConnection;

    public void with(EditText editText) {
        inputConnection = editText.onCreateInputConnection(new EditorInfo());
    }

    private void drawKeyBackground(Key key, int drawableId, Canvas canvas) {
        Drawable drawable = getContext().getDrawable(drawableId);
        drawable.setState(key.getCurrentDrawableState());
        drawable.setBounds(
                key.x + key.height / 4,
                key.y + key.height / 4,
                key.x + key.width - key.height / 4,
                key.y + key.height * 3 / 4
        );
        drawable.draw(canvas);
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

    private void updateKeyIcon(int keyCode, int drawableId) {
        getKey(keyCode).icon = getContext().getDrawable(drawableId);
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeUp() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }
}
