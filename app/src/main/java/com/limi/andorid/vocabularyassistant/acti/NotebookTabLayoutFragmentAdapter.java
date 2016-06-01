package com.limi.andorid.vocabularyassistant.acti;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by limi on 16/5/4.
 */
public class NotebookTabLayoutFragmentAdapter extends FragmentPagerAdapter {
    private String[] tabTitles;
    private Fragment[] fragments;

    public NotebookTabLayoutFragmentAdapter(FragmentManager fm, Fragment[] fragments, String[] tabTitles) {
        super(fm);
        this.fragments = fragments;
        this.tabTitles = tabTitles;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        return this.fragments[position];
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return this.fragments.length;
    }

    /**
     * This method may be called by the ViewPager to obtain a title string
     * to describe the specified page. This method may return null
     * indicating no title for this page. The default implementation returns
     * null.
     *
     * @param position The position of the title requested
     * @return A title for the requested page
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return this.tabTitles[position];
    }

}
