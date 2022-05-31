package com.example.java_sticker.gGoalInput;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.java_sticker.R;

import java.util.Objects;

public class Second extends Fragment {
    private View view;
    Bundle bundle = new Bundle();
    Fragment Third = new Third();

    //viewpager
  //  private ViewPager viewPager;
    private View vp;

//    SharedPreferences sharedpreferences;
//    public static final String MyPREFERENCES = "MyPrefs" ;


    public Second(){}
    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        assert inflater != null;
        view = inflater.inflate(R.layout.custom_g_input2, container, false);

        EditText authentication = view.findViewById(R.id.auth);
        Button preBtn = view.findViewById(R.id.preBtn);
        Button nxtBtn = view.findViewById(R.id.nxtBtn);

        gGoalInputActivity frag = ((gGoalInputActivity) this.getActivity());
        assert frag != null;
       // viewPager = frag.findViewById(R.id.input_viewPager);

        //Intent i = getIntent(); //getIntent()로 받을 준비

//        int cnt = i.getIntExtra("count", 0);
//        String goal = i.getStringExtra("goal");
//        int limit = i.getIntExtra("limit", 0);

        //sharedpreferences =  getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        preBtn.setOnClickListener(view -> {
            assert getFragmentManager() != null;
            getFragmentManager().popBackStack();
        });
        nxtBtn.setOnClickListener(view -> {
            //Bundle bundle_g = this.getArguments();
            //입력한값 형 변환
            String auth = authentication.getText().toString();

            if (auth.matches(""))
                Toast.makeText(getContext(), "인증방식을 입력해주세요.", Toast.LENGTH_SHORT).show();
            else {
                //First에서 받은 정보  get
                int count = 0;

                Bundle bundle_g = this.getArguments();
                assert bundle_g != null;
                count = bundle_g.getInt("count");
                String goal = bundle_g.getString("goal");
                int limit = bundle_g.getInt("limit");

                //Third로 데이터 넘기기
                bundle.putInt("count", count);
                bundle.putString("goal", goal);
                bundle.putInt("limit", limit);


                bundle.putString("auth", auth);

                Log.d("여기 second", String.valueOf(count));
                Log.d("여기 second", String.valueOf(goal));
                Log.d("여기 second", String.valueOf(limit));
                Log.d("여기 second", auth);
                Third.setArguments(bundle);

//                Bundle Second = new Bundle();
//                Second.putString("auth",auth);
//                getParentFragmentManager().setFragmentResult("SecondKey", Second);


//                SharedPreferences.Editor editor = sharedpreferences.edit();
//                editor.putString("auth", auth);
//                editor.commit();

              //  viewPager.setCurrentItem(getItem(), true);

                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.input_framelayout, Third);
                //프래그먼트 트랜잭션을 백스택에 push
                transaction.addToBackStack(null);
                //프래그먼트 상태전환 최적화
                transaction.setReorderingAllowed(true);
                transaction.commit();
            }

        });
        return view;
    }
//
//    private int getItem() {
//        //return viewPager.getCurrentItem() + 1;
//    }
}
