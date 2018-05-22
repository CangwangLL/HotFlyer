package com.tianxing.hotflyer.viewer.fragment.favourite;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.tianxing.hotflyer.viewer.R;
import com.tianxing.hotflyer.viewer.adapter.ViewPagerAdapter;
import com.tianxing.hotflyer.viewer.fragment.ExtendedAppBarFragment;

public class FavouriteTabsFragment extends ExtendedAppBarFragment {

    public TabLayout mTabLayout;

    public ViewPager mViewPager;

    public static ViewPagerAdapter mAdapter;

    public FavouriteTabsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        FavouriteFragment fragment = new FavouriteMovieFragment();
        mAdapter.addFragment(fragment, "作品");
        fragment = new FavouriteActressFragment();
        mAdapter.addFragment(fragment, "女优");

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        mTabLayout = view.findViewById(R.id.favourite_tabs);
        mViewPager = view.findViewById(R.id.favourite_view_pager);
        return view;
    }

    public static void update() {
        if (mAdapter != null) {
            for (int i = 0; i < mAdapter.getCount(); i++) {
                ((FavouriteFragment) mAdapter.getItem(i)).update();
            }
        }
    }
}
