package com.example.project2.Adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewpagerAdapter extends FragmentPagerAdapter
{
    private final List<Fragment> fragmentList=new ArrayList<>();
    private final List<String> fragmentTitlelist=new ArrayList<>();

    public ViewpagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragment(Fragment fragment,String title){
        fragmentList.add(fragment);
        fragmentTitlelist.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return  fragmentTitlelist.get(position);
    }
}
