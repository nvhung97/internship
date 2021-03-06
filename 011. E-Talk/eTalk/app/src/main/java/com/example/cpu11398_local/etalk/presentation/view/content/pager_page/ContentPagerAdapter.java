package com.example.cpu11398_local.etalk.presentation.view.content.pager_page;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.example.cpu11398_local.etalk.presentation.view.content.pager_page.contacts.ContactsFragment;
import com.example.cpu11398_local.etalk.presentation.view.content.pager_page.groups.GroupsFragment;
import com.example.cpu11398_local.etalk.presentation.view.content.pager_page.messages.MessagesFragment;
import com.example.cpu11398_local.etalk.presentation.view.content.pager_page.more.MoreFragment;
import com.example.cpu11398_local.etalk.presentation.view.content.pager_page.timeline.TimelineFragment;

public class ContentPagerAdapter extends FragmentStatePagerAdapter {

    public ContentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new MessagesFragment();
            case 1: return new ContactsFragment();
            case 2: return new GroupsFragment();
            case 3: return new TimelineFragment();
            case 4: return new MoreFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }
}
