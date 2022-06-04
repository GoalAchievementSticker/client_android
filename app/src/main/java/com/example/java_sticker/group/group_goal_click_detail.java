package com.example.java_sticker.group;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.java_sticker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link group_goal_click_detail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class group_goal_click_detail extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Toolbar toolbar_group;
    TextView g_goal_c_total_per;
    RecyclerView g_goal_c_item_ry;

    //툴바그룹 목표
    TextView g_title_name;


    //FB
    String uid;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference profile_databaseReference = firebaseDatabase.getReference();
    DatabaseReference databaseReference = firebaseDatabase.getReference("GroupDialog");

    //(참여한 uid 접근)
    DatabaseReference uid_key_ds;
    //(참여한 uid의 각 골그룹 다이얼로그 값 가져오기)
    DatabaseReference uid_goal_group;

    //ProgressBar circleProgressBar;
    com.example.java_sticker.CustomProgress customProgress;

    TextView custom_g_goal_tittle;

    //dialog
    ArrayList<GroupDialog> gDialog;
    //해당 도장판의 참가한 유저의 배열을 저장하는 곳!
    List<String> uid_key;

    g_goal_c_detail_adapter gAdapter;



    //참여중 리사이클러뷰 아이템 클릭했던 값 받는 변수
    String title;
    String key;
    int count;

    private Intent intent;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public group_goal_click_detail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment group_goal_click_detail.
     */
    // TODO: Rename and change types and number of parameters
    public static group_goal_click_detail newInstance(String param1, String param2) {
        group_goal_click_detail fragment = new group_goal_click_detail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_group_goal_click_detail, container, false);

        toolbar_group = (Toolbar) view.findViewById(R.id.toolbar_group);
        g_goal_c_total_per = (TextView) view.findViewById(R.id.g_goal_c_total_per);
        g_goal_c_item_ry = (RecyclerView) view.findViewById(R.id.g_goal_c_item_ry);

        //파이어베이스
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();

        intent = getActivity().getIntent();


        //눌렀을때 넘어온 값 전달
        title = intent.getStringExtra("tittle");
        key = intent.getStringExtra("key");
        count = intent.getIntExtra("count", 5);


        //uid 접근
        uid_key_ds=databaseReference.child(uid).child("dialog_group").child(key);


        toolbar_group.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        //툴바에 목표 설정
        g_title_name = view.findViewById(R.id.userN);
        g_title_name.setText(title);

        //다이얼로그 선언
        gDialog = new ArrayList<>();
        uid_key = new ArrayList<>();

        //리사이클러뷰 선언
        customProgress = view.findViewById(R.id.customProgress_g_goal_c);
        custom_g_goal_tittle = view.findViewById(R.id.custom_g_goal_tittle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        g_goal_c_item_ry.setLayoutManager(linearLayoutManager);
        //리사이클러뷰 어댑터 연결
        gAdapter = new g_goal_c_detail_adapter(getContext(), gDialog);
        g_goal_c_item_ry.setAdapter(gAdapter);

        //uid_key접근가져옴
        ReadUidKeyDialog();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //리사이클러뷰 각 아이템 정보도 가져오기
                for(int j = 0; j<uid_key.size(); j++){
                    uid_goal_group = databaseReference.child(uid_key.get(j)).child("dialog_group").child(key);
                    ReadDialogGroup();
                }

            }
        },400);



        // Inflate the layout for this fragment
        return view;
    }


    //클릭한 리사이클러뷰 아이템의 참가한 유저의 uid를 가져오는 함수
    private void ReadUidKeyDialog() {
        uid_key_ds.addListenerForSingleValueEvent(new ValueEventListener() {
            //@SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uid_key.clear();
                for (DataSnapshot dataSnapshot : snapshot.child("uid").getChildren()) {
                    uid_key.add(dataSnapshot.getValue(String.class));
                    //Log.d("TAG", String.valueOf(uid_key));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        });

    }



    //각 uid_key에 해당하는 group_goal가져오기!!!
    private void ReadDialogGroup(){
        uid_goal_group.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String key = snapshot.getKey();
                GroupDialog read_g = snapshot.getValue(GroupDialog.class);
                assert  read_g !=null;
                read_g.key = key;
                gDialog.add(read_g);

                gAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}