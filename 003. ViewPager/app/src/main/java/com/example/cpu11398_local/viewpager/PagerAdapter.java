package com.example.cpu11398_local.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private final String TAG = "ViewPager_PagerAdapter";

    public PagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        Log.w(TAG, "PagerAdapter()");
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.w(TAG, "instantiateItem(" + container + ", " + position + ")");
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {
        Log.w(TAG, "getItem(" + position + ")");
        Fragment frag = null;
        switch (position){
            case 0:
                frag=new Fragment_Android();
                break;
            case 1:
                frag=new Fragment_IOS();
                break;
            case 2:
                frag=new Fragment_PHP();
                break;
        }
        return frag;
    }
    @Override
    public int getCount() {
        Log.w(TAG, "getCount()");
        return 3;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        Log.w(TAG, "getPageTitle(" + position + ")");
        String title = "";
        switch (position){
            case 0:
                title="Android";
                break;
            case 1:
                title="IOS";
                break;
            case 2:
                title="PHP";
                break;
        }

        return title;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        Log.w(TAG, "isViewFromObject(" + view + ", " + object + ")");
        return super.isViewFromObject(view, object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.w(TAG, "destroyItem(" + container + ", " + position + ", " + object + ")");
        super.destroyItem(container, position, object);
    }
}
