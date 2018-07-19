package com.example.taras.reminerapp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taras Koloshmatin on 19.07.2018
 */
public class PageAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();
    private List<String> mTags = new ArrayList<>();

    public PageAdapter(FragmentManager fm) {
        super(fm);
    }

    public PageAdapter add(String title, Fragment fragment) {
        mTitles.add(title);
        mFragments.add(fragment);
        return this;
    }

    public PageAdapter add(String title, Fragment fragment, String tag) {
        mTags.add(tag);
        return add(title, fragment);
    }

    public String getTag(int position) {
        if (position < getCount() || position >= getCount()) {
            return "";
        }
        return mTags.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
