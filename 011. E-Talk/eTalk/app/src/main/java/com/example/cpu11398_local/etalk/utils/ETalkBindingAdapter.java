package com.example.cpu11398_local.etalk.utils;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.databinding.BindingAdapter;

/**
 * Created by Hung-pc on 8/14/2018.
 * Class included methods that used in xml file to bind attribute or
 * something else of view.
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

    /**
     * Add setter to bind adapter of {@code ViewPager}.
     * @param viewPager view need to set adapter.
     * @param pagerAdapter adapter is used to add to {@code viewPager}.
     */
    @BindingAdapter("setPagerAdapter")
    public static void setPagerAdapter(ViewPager viewPager, PagerAdapter pagerAdapter) {
        viewPager.setAdapter(pagerAdapter);
    }

    /**
     * Add setter to bind listener for page change of {@code ViewPager}.
     * @param viewPager view need to set listener.
     * @param listener listener is used to add to {@code viewPager}.
     */
    @BindingAdapter("setOnPageChangeListener")
    public static void setOnPageChangeListener(ViewPager viewPager, ViewPager.OnPageChangeListener listener) {
        viewPager.addOnPageChangeListener(listener);
    }

    /**
     * Add setter to bind current item selected for {@code ViewPager}.
     * @param viewPager view need to set page item.
     * @param item position of item.
     */
    @BindingAdapter("setCurrentItem")
    public static void setCurrentItem(ViewPager viewPager, int item) {
        viewPager.setCurrentItem(item);
    }
}
