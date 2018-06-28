package com.example.cpu11398_local.mvvm.view_model;

import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.widget.EditText;

import com.example.cpu11398_local.mvvm.R;
import com.example.cpu11398_local.mvvm.utils.Utils;

public class ViewModel {

    private final String TAG = "ViewModel";

    public static ObservableInt usernameHintColor;
    public static ObservableInt passwordHintColor;

    public ViewModel() {
        Utils.showLog(TAG, "ViewModel");
        usernameHintColor = new ObservableInt();
        passwordHintColor = new ObservableInt();
    }

    public void performLogin() {
        Utils.showLog(TAG, "performLogin");
        usernameHintColor.set(R.color.colorRed);
        passwordHintColor.set(R.color.colorRed);
    }
}
