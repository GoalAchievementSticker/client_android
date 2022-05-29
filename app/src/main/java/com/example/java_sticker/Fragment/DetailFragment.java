package com.example.java_sticker.Fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;


import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.java_sticker.R;
import com.example.java_sticker.group.GroupDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class DetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private View view;
    TextView goal;
    TextView count;
    TextView auth;
    TextView limit;
    TextView cate;

    int _count;
    int _limit;
    int _limit_count;
    String _goal;
    String _auth;
    String _cate;
    String _key;
    Button add_button;

    String uid;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference categoryReference = firebaseDatabase.getReference("Category");
    DatabaseReference databaseReference = firebaseDatabase.getReference("GroupDialog");

    //uid 담을 리스트
    List<String> uid_key;

    ArrayList<GroupDialog> gDialog;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail, container, false);

        uid_key = new ArrayList<>();
        gDialog = new ArrayList<>();

        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();


        add_button = (Button) view.findViewById(R.id.add_button);
        goal = (TextView) view.findViewById(R.id.goal);
        count = (TextView) view.findViewById(R.id.count);
        auth = (TextView) view.findViewById(R.id.auth);
        limit = (TextView) view.findViewById(R.id.limit);
        cate = (TextView) view.findViewById(R.id.cate);


        //ReadGroupDialog(_cate, _key);

        try {
            GetBundle();
            ReadCategoryDialog(_cate, _key);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Log.d("TAG", uid_key.get(0));
//                    ReadGroupDialog(uid_key.get(0),_key);
//                }
//            },200);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        //참가하기 버튼을 눌렀을때 작동!!!
        //참가하기 버튼 기능
        //1. 클릭했을때 uid_key랑 비교해서 값이 있다면 이미 참가중이라고 알림주고 끝내기(완료)
        //2.  클릭했을때 uid_key랑 비교해서 값이 없다면 category -> uid에 반영, 작성한 유저 uid추가, 참가한 유저의 db에 각각 반영(아래 코드 참고)(완료)
        //3. 제한 인원 값과 uid_key사이즈 + 1 이 같다면 해당 리사이클러뷰(카테고리에 있는것만) 삭제!!(나중에 해결)
        //4. 클릭했을때 limit_count 증가 반영!!! -> category의 limit_count db 반영(완료), 작성한 유저 limit_count 반영(완료), 참가한 유저 db의 limit_count반영 (해결!)
        add_button.setOnClickListener(view -> new Handler().postDelayed(() -> {
            boolean status = false;
            for (int i = 0; i < uid_key.size(); i++) {
                status = uid_key.get(i).equals(uid); //자신의 uid랑 동일한 uid_key 설정
                //이미 있다면 true
                if (status) {
                    //이미참여한사람임으로 버튼 참가 못하게 막기
                    Toast.makeText(view.getContext(), "이미 참가한 도장판 입니다", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            //위에 for문을 돌고 참가한 유저가 아니라면 uid에 추가해준다.
            //카테고리 + GroupDialog(최초작성한 유저 uid에 들어감) + 참가한 유저 groupDialog 새로 추가해줌+uid반영..
            if (!status) {
                //카테고리 uid 접근, 카테고리 limit_count 접근
                DatabaseReference add_category_uid = categoryReference.child(_cate).child(_key).child("uid");
                DatabaseReference add_category_limit_count = categoryReference.child(_cate).child(_key).child("limit_count");
                //작성자 uid 접근
                DatabaseReference add_GroupDialog_uid = databaseReference.child(uid_key.get(0)).child("dialog_group").child(_key).child("uid");
                DatabaseReference add_GroupDialog_limit_count = databaseReference.child(uid_key.get(0)).child("dialog_group").child(_key).child("limit_count");
                //참가한 유저 GroupDialog 값 접근. .
                DatabaseReference add_GroupDialog_button_click_user = databaseReference.child(uid).child("dialog_group").child(_key);


                //카테고리 uid추가(완료)
                add_category_uid.push().setValue(uid);
                //카테고리 limit_count(증가 반영)
                add_category_limit_count.setValue(_limit_count + 1);
                //작성자 GroupDialog에 uid 추가(완료)
                add_GroupDialog_uid.push().setValue(uid);
                //작성자 GroupDialog에 limit_count(증가 반영)
                add_GroupDialog_limit_count.setValue(_limit_count + 1);
                //참가한 유저 GroupDialog 추가 db단위로 추가안하면 배열값으로 들어감.
                //여기 limit_count 값 그냥 기존값 불러와서 +1하면됨(해결)
                GroupDialog groupDialog = new GroupDialog(_count, _goal, _limit, _auth, _key, 0, _cate, _limit_count + 1);
                add_GroupDialog_button_click_user.setValue(groupDialog);

                //for문 돌려서 이미 있는 uid_key안의 uid추가
                for (int i = 0; i < uid_key.size(); i++) {
                    add_GroupDialog_button_click_user.child("uid").push().setValue(uid_key.get(i));
                }
                //내 자신도 추가해야함!!
                add_GroupDialog_button_click_user.child("uid").push().setValue(uid);
            }
            Log.d("TAG", String.valueOf(status));

            //만약 참가한 사람이 limit만큼 찼다면 카테고리 값을 삭제해준다.
//                        if(uid_key.size()+1 == _limit){
//                            DatabaseReference remove_category = categoryReference.child(_cate).child(_key);
//                            remove_category.removeValue();
//                        }


        }, 200));


        return view;
    }

    private void GetBundle() {
        Bundle bundle = this.getArguments();

        _count = bundle.getInt("count");
        _limit = bundle.getInt("limit");
        _limit_count = bundle.getInt("limit_count");
        _goal = bundle.getString("goal");
        _auth = bundle.getString("auth");
        _cate = bundle.getString("cate");
        _key = bundle.getString("key");

        Log.d("getBundle", _count + "/ " + _limit + "/ " + _goal + "/ " + _auth + "/ " + _cate);
        goal.setText(_goal);
        count.setText(_count + "개");
        auth.setText(_auth);
        limit.setText(String.valueOf(_limit));
        cate.setText(_cate);

    }

    //다이얼로그 저장된 함수 가져오기
    private void ReadGroupDialog(String write_uid, String write_key) {
        databaseReference.child(write_uid).child("dialog_group").child(write_key).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gDialog.clear();
                String key = snapshot.getKey();
                GroupDialog read_g = snapshot.getValue(GroupDialog.class);
                assert read_g != null;
                read_g.key = key;

                gDialog.add(read_g);
                Log.d("TAG", String.valueOf(gDialog));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(), "불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //클릭한 카테고리 값 가져오기
    private void ReadCategoryDialog(String cate2, String key2) {
        uid_key.clear();
        categoryReference.child(cate2).child(key2).addValueEventListener(new ValueEventListener() {
            //@SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.child("uid").getChildren()) {
                    //참가한 uid 를 uid-key에 넣기
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
}