package com.example.cpu11398_local.etalk.presentation.custom;

import android.content.res.Resources;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.databinding.BindingAdapter;
import android.widget.TextView;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.utils.Event;

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
        viewPager.setOffscreenPageLimit(4);
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
        viewPager.setOffscreenPageLimit(4);
    }

    /**
     * Add setter to bind text and text color for {@code TextView} according to given {@code Event}.
     * @param textView TextView need to set hint state.
     * @param event used to set hint state for the TextView.
     */
    @BindingAdapter("hintWithEvent")
    public static void setHintWithEvent(TextView textView, Event event) {
        Resources resources = textView.getContext().getResources();
        switch (textView.getId()) {
            case R.id.login_activity_txt_input_hint:
                switch (event.getType()) {
                    case Event.NONE:
                        textView.setText(resources.getString(R.string.login_activity_txt_input_hint));
                        textView.setTextColor(resources.getColor(R.color.colorGray));
                        break;
                    case Event.LOGIN_ACTIVITY_LOGIN_FAILED:
                        textView.setText(resources.getString(R.string.login_activity_login_failed));
                        textView.setTextColor(resources.getColor(R.color.colorRed));
                        break;
                    case Event.LOGIN_ACTIVITY_TIMEOUT:
                        textView.setText(resources.getString(R.string.app_request_timeout));
                        textView.setTextColor(resources.getColor(R.color.colorRed));
                        break;
                }
                break;
            case R.id.register_activity_txt_input_hint:
                switch (event.getType()) {
                    case Event.NONE:
                        textView.setText(resources.getString(R.string.register_activity_txt_input_hint));
                        textView.setTextColor(resources.getColor(R.color.colorGray));
                        break;
                    case Event.REGISTER_ACTIVITY_REGISTER_FAILED:
                        textView.setText(resources.getString(R.string.register_activity_register_failed));
                        textView.setTextColor(resources.getColor(R.color.colorRed));
                        break;
                    case Event.REGISTER_ACTIVITY_TIMEOUT:
                        textView.setText(resources.getString(R.string.app_request_timeout));
                        textView.setTextColor(resources.getColor(R.color.colorRed));
                        break;
                }
                break;
            case R.id.add_friend_activity_txt_input_hint:
                switch (event.getType()) {
                    case Event.NONE:
                        textView.setText(resources.getString(R.string.add_friend_activity_txt_input_hint));
                        textView.setTextColor(resources.getColor(R.color.colorGray));
                        break;
                    case Event.ADD_FRIEND_ACTIVITY_TIMEOUT:
                        textView.setText(resources.getString(R.string.app_request_timeout));
                        textView.setTextColor(resources.getColor(R.color.colorRed));
                        break;
                }
                break;
        }
    }

    /**
     * Add setter for {@code src_from_url} in {@code AvatarImageView}.
     * @param avatarImageView view need to set image.
     * @param object used to load image to {@code AvatarImageView}.
     */
    @BindingAdapter("src_from_object")
    public static void setImageFromObject(AvatarImageView avatarImageView, Object object) {
        avatarImageView.setImageFromObject(object);
    }

    /**
     * Set animation for a view with attribute {@code anim} in {@code View}.
     * @param view view need to set animation.
     * @param animation animation from resource.
     */
    @BindingAdapter("anim")
    public static void setAnimation(View view, Animation animation) {
        view.startAnimation(animation);
    }
}
