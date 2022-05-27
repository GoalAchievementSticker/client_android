package com.example.java_sticker.gGoalInput;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.java_sticker.R;

public class Second extends Fragment {
    private View view;
    Bundle bundle = new Bundle();
    Fragment Third = new Third();

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        assert inflater != null;
        view = inflater.inflate(R.layout.custom_g_input2, container, false);

        EditText authentication = view.findViewById(R.id.auth);
        Button preBtn = view.findViewById(R.id.preBtn);
        Button nxtBtn = view.findViewById(R.id.nxtBtn);

        //Intent i = getIntent(); //getIntent()로 받을 준비

//        int cnt = i.getIntExtra("count", 0);
//        String goal = i.getStringExtra("goal");
//        int limit = i.getIntExtra("limit", 0);


        preBtn.setOnClickListener(view -> {
            assert getFragmentManager() != null;
            getFragmentManager().popBackStack();
        });
        nxtBtn.setOnClickListener(view -> {
            Bundle bundle_g = this.getArguments();
            //입력한값 형 변환
            String auth = authentication.getText().toString();

            //First에서 받은 정보  get
            int count = bundle_g.getInt("count");
            String goal = bundle_g.getString("goal");
            int limit = bundle_g.getInt("limit");


            //Third로 데이터 넘기기
            bundle.putInt("count", count);
            bundle.putString("goal", goal);
            bundle.putInt("limit", limit);

            bundle.putString("auth", auth);
            Third.setArguments(bundle);
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.group_layout, Third);
            //프래그먼트 트랜잭션을 백스택에 push
            transaction.addToBackStack(null);
            //프래그먼트 상태전환 최적화
            transaction.setReorderingAllowed(true);
            transaction.commit();
            //  Intent intent = new Intent(this, Third.class);


            //First에서 받은 데이터
//            intent.putExtra("count",cnt);
//            intent.putExtra("goal",goal);
//            intent.putExtra("limit",limit);

            //Second 정보
//            intent.putExtra("auth", auth);


            //       startActivity(intent);


        });
        return view;
    }

}
