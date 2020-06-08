package com.justice.noteapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {



    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return MainActivity.fragments.get(position);
    }

    @Override
    public int getCount() {
        return MainActivity.fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
          return MainActivity.fragmentsName.get(position);

    }
}
