package com.example.java_sticker.group;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.java_sticker.Fragment.j_FragJoin;
import com.example.java_sticker.R;

import java.util.ArrayList;
import java.util.List;

public class custom_g_goal_click_main extends AppCompatActivity {

    Fragment group_goal_click_detail;
   // custom_g_goal_click custom_g_goal_click = new custom_g_goal_click();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_g_goal_click_main);

        group_goal_click_detail = new group_goal_click_detail();

        getSupportFragmentManager().beginTransaction().replace(R.id.group_goal_click_layout, group_goal_click_detail).commitAllowingStateLoss();


    }


}
