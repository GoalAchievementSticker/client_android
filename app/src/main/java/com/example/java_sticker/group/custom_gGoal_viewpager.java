package com.example.java_sticker.group;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.java_sticker.Fragment.j_FragJoin;
import com.example.java_sticker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class custom_gGoal_viewpager extends AppCompatActivity {


    private ViewPager mPager;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    j_FragJoin j_fragjoin = new j_FragJoin();
    custom_g_goal_click custom_g_goal_click = new custom_g_goal_click();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_ggoal_viewpager);

        Bundle bundle = new Bundle();

        mPager = findViewById(R.id.ggoal_viewpager);
        mFragmentList.add(0, custom_g_goal_click);
        mFragmentList.add(1, j_fragjoin);
        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), mFragmentList);
        mPager.setAdapter(pagerAdapter);
        mPager.setCurrentItem(0);

    }




    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            //유저가 현재 첫 페이지 보고 있으면, 시스템이 백 버튼을 컨트롤한다.
            //이건 finish()를 콜하고 백스택을 없앤다.
            super.onBackPressed();
        } else {
            // 그렇지 않다면, 이전의 페이지로 돌아간다.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    /**
     * 도장판에 들아갈 프래그먼트를 보여줄 간단한 PagerAdapter
     */
    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList = new ArrayList<>();

        public ScreenSlidePagerAdapter(FragmentManager fm, List<Fragment> mFragmentList) {
            super(fm);
            this.mFragmentList = mFragmentList;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public List<Fragment> addFrag(int position, Fragment fragment) {
            mFragmentList.add(position, fragment);
            return mFragmentList;
        }

//        @Override
//        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
//            return false;
//        }
    }
}
