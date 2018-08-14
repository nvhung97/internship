package com.example.cpu11398_local.etalk.utils;

import android.view.View;
import android.widget.LinearLayout;
import android.databinding.BindingAdapter;

/**
 * Created by Hung-pc on 8/14/2018.
 * Class included method that
 */

public class ETalkBindingAdapter {

    /**
     * Add setter for attribute {@code layout_weight} with float parameter in {@code view}.
     * @param view  view need to set layout weight.
     * @param weight layout weight.
     */
    @BindingAdapter("android:layout_weight")
    public static void setLayoutWeight(View view, float weight) {
        view.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                weight
        ));
    }
}
