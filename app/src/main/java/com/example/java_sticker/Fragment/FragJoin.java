package com.example.java_sticker.Fragment;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.java_sticker.R;
import com.example.java_sticker.gGoalInput.First;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class FragJoin extends Fragment {
    //Tablayout
    private ArrayList<String> tabNames = new ArrayList<>();
    TabLayout tabLayout;
    private View view;

    FloatingActionButton fab_g;
    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        view = inflater.inflate(R.layout.fragjoin, container, false);
        //FAT
        fab_g = view.findViewById(R.id.fab_g);
        //FAB 클릭 시
        fab_g.setOnClickListener(view -> toggleFab());
        setTabLayout();
        loadTabName();
        return view;
    }

    private void toggleFab() {
        ObjectAnimator.ofFloat(fab_g, View.ROTATION, 0f, 45f).start();
        Fragment First = new First();
        assert getFragmentManager() != null;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.group_layout, First);
        //프래그먼트 트랜잭션을 백스택에 push
        transaction.addToBackStack(null);
        //프래그먼트 상태전환 최적화
        transaction.setReorderingAllowed(true);
        transaction.commit();
    }

    private void setViewPager() {
//        assert getFragmentManager() != null;
//        FragJoinAdapter adapter = new FragJoinAdapter(getChildFragmentManager());
//        ViewPager viewPager =view.findViewById(R.id.viewPager);
        //      viewPager.setAdapter(adapter);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout = view.findViewById(R.id.tab_layout);
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewPager);

        tabLayout = view.findViewById(R.id.tab_layout);
        // attach tablayout with viewpager
        tabLayout.setupWithViewPager(mViewPager);

        FragJoinAdapter adapter = new FragJoinAdapter(getChildFragmentManager());

        // add your fragments
        adapter.addFrag(new w_FragJoin(), "대기 중");
        adapter.addFrag(new j_FragJoin(), "참여 중");


        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        // set adapter on viewpager
        mViewPager.setAdapter(adapter);
        mViewPager.setSaveEnabled(false);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void setTabLayout() {
        tabNames.forEach(name -> tabLayout.addTab(tabLayout.newTab().setText(name)));
    }

    private void loadTabName() {
        tabNames.add("대기 중");
        tabNames.add("참여 중");
    }

}
