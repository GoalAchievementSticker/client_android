package com.example.java_sticker;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;


import com.example.java_sticker.Fragment.DetailFragment;
import com.example.java_sticker.Fragment.Exercise;
import com.example.java_sticker.Fragment.FragHome;
import com.example.java_sticker.Fragment.FragJoin;
import com.example.java_sticker.Fragment.FragMypage;
import com.example.java_sticker.Fragment.Hobby;
import com.example.java_sticker.Fragment.Routin;
import com.example.java_sticker.Fragment.Study;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class Group_main extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    Fragment fragment_home;
    Fragment fragment_group_join;
    Fragment fragment_mypage;
    Fragment fragment_study;
    Fragment fragment_hobby;
    Fragment fragment_routin;
    Fragment fragment_exercise;
    //Fragment DetailFragment;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);// ActionBar 타이틀(앱이름) 감추기


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        //네비게이션연결 프래그먼트
        fragment_home = new FragHome();
        fragment_group_join = new FragJoin();
        fragment_mypage = new FragMypage();

        //카테고리 프래그먼트
        fragment_study = new Study();
        fragment_exercise = new Exercise();
        fragment_hobby = new Hobby();
        fragment_routin = new Routin();

        //DetailFragment = new DetailFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.group_layout, fragment_home).commitAllowingStateLoss();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.group_layout, fragment_home).commitAllowingStateLoss();
                    return true;
                case R.id.join_group:
                    getSupportFragmentManager().beginTransaction().replace(R.id.group_layout, fragment_group_join).commitAllowingStateLoss();
                    return true;

                case R.id.mypage:
                    getSupportFragmentManager().beginTransaction().replace(R.id.group_layout, fragment_mypage).commitAllowingStateLoss();
                    return true;
            }
            return true;
        });

    }


    //카테고리 프래그먼트 전환
    //0:공부, 1:운동, 2:취미, 3:루틴
    public  void onFragmentChange(int index){
        if(index == 0 ){
            getSupportFragmentManager().beginTransaction().replace(R.id.group_layout, fragment_study).addToBackStack(null).commit();
        } else if(index == 1){
            getSupportFragmentManager().beginTransaction().replace(R.id.group_layout, fragment_exercise).addToBackStack(null).commit();
        }
        else if(index == 2){
            getSupportFragmentManager().beginTransaction().replace(R.id.group_layout, fragment_hobby).addToBackStack(null).commit();
        }else if(index == 3){
            getSupportFragmentManager().beginTransaction().replace(R.id.group_layout, fragment_routin).addToBackStack(null).commit();
        }

    }



}