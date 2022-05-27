package com.example.java_sticker.gGoalInput;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.java_sticker.Fragment.FragInputCheck;
import com.example.java_sticker.Fragment.FragJoin;
import com.example.java_sticker.R;

public class Third extends Fragment {

    Button excercise;
    Button study;
    Button hobby;
    Button routine;
    Bundle bundle = new Bundle();
    Fragment FragInputCheck = new FragInputCheck();
    Fragment FragJoin = new FragJoin();

    private View view;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        assert inflater != null;
        view = inflater.inflate(R.layout.custom_g_input3, container, false);


        excercise = view.findViewById(R.id.exercise);
        study = view.findViewById(R.id.study);
        hobby = view.findViewById(R.id.hobby);
        routine = view.findViewById(R.id.routine);


        Button preBtn = view.findViewById(R.id.preBtn);
        Button OKBtn = view.findViewById(R.id.OKBtn);

        //카테고리 함수
        SelectCate();

        preBtn.setOnClickListener(view -> {
            assert getFragmentManager() != null;
            getFragmentManager().popBackStack();
                }
        );
        OKBtn.setOnClickListener(view -> {
                    //fragment에서 데이터 받기
                    Bundle bundle_g = this.getArguments();
                    //First
                    int cnt = bundle_g.getInt("count");
                    String goal = bundle_g.getString("goal");
                    int limit = bundle_g.getInt("limit");

                    //Second
                    String auth = bundle_g.getString("auth");

                    //First에서 받은 데이터
                    bundle.putInt("count", cnt);
                    bundle.putString("goal", goal);
                    bundle.putInt("limit", limit);

                    //Second 정보
                    bundle.putString("auth", auth);


                    FragJoin.setArguments(bundle);

                    assert getFragmentManager() != null;
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.group_layout, FragJoin);
                    //프래그먼트 트랜잭션을 백스택에 push
                    transaction.addToBackStack(null);
                    //프래그먼트 상태전환 최적화
                    transaction.setReorderingAllowed(true);
                    transaction.commit();

                }


        );
        return view;
    }

    private void Bundle() {

//        Intent i = getIntent();
//        //First
//        int cnt = i.getIntExtra("count", 0);
//        String goal = i.getStringExtra("goal");
//        int limit = i.getIntExtra("limit", 0);
//
//        //Second
//        String auth = i.getStringExtra("auth");
//
//        //First에서 받은 데이터
//        bundle.putInt("count", cnt);
//        bundle.putString("goal", goal);
//        bundle.putInt("limit", limit);
//
//        //Second 정보
//        bundle.putString("auth", auth);
//
//
//        FragJoin.setArguments(bundle);
//
//
//        try {
//            FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
//            transaction.add(R.id.group_layout, FragJoin);
//            //프래그먼트 트랜잭션을 백스택에 push
//            transaction.addToBackStack(null);
//            //프래그먼트 상태전환 최적화
//            transaction.setReorderingAllowed(true);
//            transaction.commit();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        }

    }

    private void SelectCate() {
        //입력한값 형 변환
        String ex = excercise.getText().toString();
        String st = study.getText().toString();
        String hob = hobby.getText().toString();
        String rout = routine.getText().toString();

        excercise.setOnClickListener(view -> {
            Toast.makeText(getContext(), "운동 카테고리", Toast.LENGTH_SHORT).show();
            bundle.putString("cate", ex);
        });
        study.setOnClickListener(view -> {
            Toast.makeText(getContext(), "공부 카테고리", Toast.LENGTH_SHORT).show();
            bundle.putString("cate", st);
        });
        hobby.setOnClickListener(view -> {
            Toast.makeText(getContext(), "취미 카테고리", Toast.LENGTH_SHORT).show();
            bundle.putString("cate", hob);
        });
        routine.setOnClickListener(view -> {
            Toast.makeText(getContext(), "루틴 카테고리", Toast.LENGTH_SHORT).show();
            bundle.putString("cate", rout);
        });


    }
}

