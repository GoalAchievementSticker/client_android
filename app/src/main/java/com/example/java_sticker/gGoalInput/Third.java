package com.example.java_sticker.gGoalInput;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.java_sticker.Fragment.FragInputCheck;
import com.example.java_sticker.Fragment.w_FragJoin;
import com.example.java_sticker.R;

public class Third extends Fragment implements View.OnClickListener {

    ToggleButton exercise;
    ToggleButton study;
    ToggleButton hobby;
    ToggleButton routine;
    Bundle bundle = new Bundle();
    Fragment FragInputCheck = new FragInputCheck();
    Fragment FragJoin = new w_FragJoin();

    private View view;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        assert inflater != null;
        view = inflater.inflate(R.layout.custom_g_input3, container, false);


        exercise = view.findViewById(R.id.exercise);
        study = view.findViewById(R.id.study);
        hobby = view.findViewById(R.id.hobby);
        routine = view.findViewById(R.id.routine);


        Button preBtn = view.findViewById(R.id.preBtn);
        Button OKBtn = view.findViewById(R.id.OKBtn);


        exercise.setOnClickListener(this);
        hobby.setOnClickListener(this);
        routine.setOnClickListener(this);
        study.setOnClickListener(this);


        preBtn.setOnClickListener(view -> {
                    assert getFragmentManager() != null;
                    getFragmentManager().popBackStack();
                }
        );
        OKBtn.setOnClickListener(view -> ToFragjoin());

        return view;
    }

    private void ToFragjoin() {
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


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        //입력한값 형 변환
        String ex = exercise.getText().toString();
        String st = study.getText().toString();
        String hob = hobby.getText().toString();
        String rout = routine.getText().toString();

        switch (v.getId()) {
            case R.id.exercise:
                if (exercise.isChecked()) {
                    exercise.setChecked(true);
                    Toast.makeText(getContext(), "운동 카테고리 클릭", Toast.LENGTH_SHORT).show();
                    bundle.putString("cate", ex);

                    study.setChecked(false);
                    routine.setChecked(false);
                    hobby.setChecked(false);
                } else {
                    exercise.setChecked(false);
                }

                break;

            case R.id.study:
                if (study.isChecked()) {
                    study.setChecked(true);
                    Toast.makeText(getContext(), "공부 카테고리 클릭", Toast.LENGTH_SHORT).show();
                    bundle.putString("cate", st);

                    routine.setChecked(false);
                    hobby.setChecked(false);
                    exercise.setChecked(false);
                } else {
                    study.setChecked(false);
                }

                break;
            case R.id.routine:
                if (routine.isChecked()) {
                    routine.setChecked(true);
                    Toast.makeText(getContext(), "루틴 카테고리 클릭", Toast.LENGTH_SHORT).show();
                    bundle.putString("cate", rout);

                    hobby.setChecked(false);
                    exercise.setChecked(false);
                    study.setChecked(false);
                } else {
                    routine.setChecked(false);
                }

                break;
            case R.id.hobby:
                if (hobby.isChecked()) {
                    hobby.setChecked(true);
                    Toast.makeText(getContext(), "취미 카테고리 클릭", Toast.LENGTH_SHORT).show();
                    bundle.putString("cate", hob);

                    exercise.setChecked(false);
                    study.setChecked(false);
                    routine.setChecked(false);
                } else {
                    hobby.setChecked(false);
                }

                break;

            default:
                break;
        }
    }
}

