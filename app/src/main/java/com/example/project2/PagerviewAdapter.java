package com.example.project2;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerviewAdapter extends FragmentPagerAdapter
{
    int num;

    public PagerviewAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                postdisplay tab1=new postdisplay();
                return tab1;

            case 1:
                Photosdisplay tab2=new Photosdisplay();
                return tab2;

            case 2:
                videosdisplay tab3=new videosdisplay();
                return tab3;

            case 3:
                savepostdisplay tab4=new savepostdisplay();
                return tab4;

            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return 4;
    }
}
