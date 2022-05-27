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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.java_sticker.GridItem;
import com.example.java_sticker.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class First extends Fragment {

    //FB
    String uid;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference profile_databaseReference = firebaseDatabase.getReference();
    DatabaseReference databaseReference = firebaseDatabase.getReference("GroupDialog");

    //그리드뷰 데이터 저장
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    DatabaseReference ds;
    GridItem gd;
    private View view;
    Bundle bundle = new Bundle();
    Fragment Second=new Second();

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)  {
        assert inflater != null;
        view = inflater.inflate(R.layout.custom_g_input1, container, false);


        EditText goal = view.findViewById(R.id.sticker_goal);
        EditText count = view.findViewById(R.id.sticker_count);
        EditText limit = view.findViewById(R.id.limitNOP);
        Button noBtn = view.findViewById(R.id.noBtn);
        Button nxtBtn = view.findViewById(R.id.nxtBtn);

        noBtn.setOnClickListener(view -> {
            assert getFragmentManager() != null;
            getFragmentManager().popBackStack();
        });
        nxtBtn.setOnClickListener(view -> {

            //입력한값 형 변환
            int vi = Integer.parseInt(count.getText().toString());
            String g = goal.getText().toString();
            int l = Integer.parseInt(limit.getText().toString());

            bundle.putInt("count",vi);
            bundle.putInt("limit",l);
            bundle.putString("goal",g);
            Second.setArguments(bundle);

            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.group_layout, Second);
            //프래그먼트 트랜잭션을 백스택에 push
            transaction.addToBackStack(null);
            //프래그먼트 상태전환 최적화
            transaction.setReorderingAllowed(true);
            transaction.commit();
//            Intent intent = new Intent(this, Second.class);
//            intent.putExtra("goal", g);
//            intent.putExtra("count", vi);
//            intent.putExtra("limit", l);
//
//            startActivity(intent);


        });
        return view;
    }
}
