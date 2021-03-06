package com.sola.v2ex_android.ui.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.sola.v2ex_android.ui.NewestTopicsFragment;
import com.sola.v2ex_android.ui.NodeFragment;

/**
 * Created by wei on 2016/10/18.
 */

public class MainPageAdapter extends FragmentPagerAdapter {

    private final String[] titles = { "主题", "节点"};

    public MainPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (0 == position){
            return NewestTopicsFragment.newInstance();
        }else {
            return NodeFragment.newInstance();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}
