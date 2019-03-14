package com.example.keyboarddemo;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private Keyboard     keyboard;
    private KeyboardView keyboardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        keyboard     = new Keyboard(this, R.xml.qwerty);
        keyboardView = findViewById(R.id.keyboard);

        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {
            @Override
            public void onPress(int primaryCode) {
                switch (primaryCode) {
                    case -1:    // shift
                    case -5:    // delete
                    case -2:    // ?123
                    case -101:  // language
                    case 32:    // space
                    case 10:    // enter
                        /*keyboardView.setPreviewEnabled(false);
                        for (Key key : keyboard.getKeys()) {
                            if (key.codes[0] == primaryCode) {
                                key.icon = getDrawable(R.drawable.key_enter);
                                break;
                            }
                        }*/
                        break;
                    default:
                        keyboardView.setPreviewEnabled(true);
                }
                Log.e("Test", "onPress " + primaryCode);
            }

            @Override
            public void onRelease(int primaryCode) {
                Log.e("Test", "onRelease " + primaryCode);
            }

            @Override
            public void onKey(int primaryCode, int[] keyCodes) {
                Log.e("Test", "onKey " + primaryCode);
            }

            @Override
            public void onText(CharSequence text) {
                Log.e("Test", "onText " + text);
            }

            @Override
            public void swipeLeft() {
                Log.e("Test", "swipeLeft");
            }

            @Override
            public void swipeRight() {
                Log.e("Test", "swipeRight");
            }

            @Override
            public void swipeDown() {
                Log.e("Test", "swipeDown");
            }

            @Override
            public void swipeUp() {
                Log.e("Test", "swipeUp");
            }
        });
    }
}
