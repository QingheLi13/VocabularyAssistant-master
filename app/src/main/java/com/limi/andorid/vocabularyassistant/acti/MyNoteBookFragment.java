package com.limi.andorid.vocabularyassistant.acti;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.limi.andorid.vocabularyassistant.R;
import com.special.ResideMenu.ResideMenu;


public class MyNoteBookFragment extends Fragment {
    private View parentView;
    private ResideMenu resideMenu;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private NotebookTabLayoutFragmentAdapter fragmentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (parentView != null) {
            ViewGroup viewGroup = (ViewGroup) parentView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(parentView);
            }
            return parentView;
        }
        parentView = inflater.inflate(R.layout.fragment_blank, container, false);

        viewPager = (ViewPager) parentView.findViewById(R.id.view_pager_vp);
        tabLayout = (TabLayout) parentView.findViewById(R.id.tab_layout_tl);
        initData();
        return parentView;
    }

    private void initData() {
        String[] tabTitles = {"Favourite", "Wrong Wrong"};
        Fragment[] fragments = {new FavouriteWordFragment(), new WrongWordFragment()};
        this.fragmentAdapter = new NotebookTabLayoutFragmentAdapter(this.getChildFragmentManager(), fragments, tabTitles);
        this.viewPager.setAdapter(this.fragmentAdapter);
        this.tabLayout.setupWithViewPager(this.viewPager);
    }


}
