package com.justice.noteapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {



    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return MainFragment.fragments.get(position);
    }

    @Override
    public int getCount() {
        return MainFragment.fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
          return MainFragment.fragmentsName.get(position);

    }
}
