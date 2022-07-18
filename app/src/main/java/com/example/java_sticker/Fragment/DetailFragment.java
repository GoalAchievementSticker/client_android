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
import androidx.fragment.app.FragmentManager;

import com.example.java_sticker.R;
import com.example.java_sticker.group.GroupDialog;
import com.example.java_sticker.group.g_GridItem;
import com.example.java_sticker.personal.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;


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
    String w_uid;
    Button add_button;


    String uid;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference categoryReference = firebaseDatabase.getReference("Category");
    DatabaseReference databaseReference = firebaseDatabase.getReference("GroupDialog");
    DatabaseReference profile_name = firebaseDatabase.getReference();

    String name;

    List<String> uid_key;

    ArrayList<GroupDialog> gDialog;
    ArrayList<g_GridItem> items;

    String not_uri;

    //그리드뷰 데이터 저장
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    DatabaseReference uid_fixed = null;
    DatabaseReference uid_boolen;
    g_GridItem gd;

    boolean status = false;

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

        //리사이클러뷰 클릭했을때 나오는 도장판 연결
        items = new ArrayList<g_GridItem>();

        gd = new g_GridItem();


        add_button = (Button) view.findViewById(R.id.add_button);
        goal = (TextView) view.findViewById(R.id.goal);
        count = (TextView) view.findViewById(R.id.count);
        auth = (TextView) view.findViewById(R.id.auth);
        limit = (TextView) view.findViewById(R.id.limit);
        cate = (TextView) view.findViewById(R.id.cate);

        storageRef.child("not2.png").getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    // Got the download URL for 'plus.png'
                    not_uri = uri.toString();

                }).addOnFailureListener(Throwable::printStackTrace);


        //ReadGroupDialog(_cate, _key);

        try {
            GetBundle();
            ReadCategoryDialog(_cate, _key);
            Read_name();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        //참가하기 버튼을 눌렀을때 작동!!!
        //참가하기 버튼 기능
        //1. 클릭했을때 uid_key랑 비교해서 값이 있다면 이미 참가중이라고 알림주고 끝내기(완료)
        //2.  클릭했을때 uid_key랑 비교해서 값이 없다면 category -> uid에 반영, 작성한 유저 uid추가, 참가한 유저의 db에 각각 반영(아래 코드 참고)(완료)
        //3. 제한 인원 값과 uid_key사이즈 + 1 이 같다면 해당 리사이클러뷰(카테고리에 있는것만) 삭제!!(해결)
        //4. 클릭했을때 limit_count증가 반영!!! -> category의 limit_count db 반영(완료), 작성한 유저 limit_count 반영(완료), 참가한 유저 db의 limit_count반영 (해결!)
        //5. 참가한 인원 == 제한 인원이면 도장판 생성
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < uid_key.size(); i++) {
                            status = uid_key.get(i).equals(uid);
                            //이미 있다면 true
                            if (status) {
                                //이미참여한사람임으로 버튼 참가 못하게 막기
                                Toast.makeText(view.getContext(), "이미 참가한 도장판 입니다", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }

                        //Log.d("uid_key", "w_uid: " + databaseReference.child(uid_key.get(0))w_uid + "입니다");
                        for (int i = 0; i < uid_key.size(); i++)
                            Log.d("uid_key", "uid_key.get(i): " + databaseReference.child(uid_key.get(i)) + "입니다\n");

                        //위에 for문을 돌고 참가한 유저가 아니라면 uid에 추가해준다.
                        //카테고리 + GroupDialog(최초작성한 유저 uid에 들어감) + 참가한 유저 groupDialog 새로 추가해줌+uid반영..
                        if (!status) {
                            //카테고리 uid 접근, 카테고리 limit_count 접근
                            DatabaseReference add_category_uid = categoryReference.child(_cate).child(_key).child("uid");
                            DatabaseReference add_category_limit_count = categoryReference.child(_cate).child(_key).child("limit_count");


                            //uid_key가 정확히 무엇인지
                            //uid_key(i)로그 찍으니까 자기 자신의 uid만 찍힘...
                            //java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
                            //작성자 uid 접근

                            DatabaseReference add_GroupDialog_uid = databaseReference.child(uid_key.get(0))
                                    .child("dialog_group")
                                    .child(_key)
                                    .child("uid");

                            //작성자 limit_count 접근
                            DatabaseReference add_GroupDialog_limit_count = databaseReference.child(uid_key.get(0))
                                    .child("dialog_group")
                                    .child(_key)
                                    .child("limit_count");


                            //작성자 uid추가(완료)
                            add_GroupDialog_uid.child(String.valueOf(uid)).setValue(uid);
                            //작성자 limit_count(증가 반영)
                            add_GroupDialog_limit_count.setValue(_limit_count + 1);


                            //참가한 유저 GroupDialog 값 접근. .
                            DatabaseReference add_GroupDialog_button_click_user = databaseReference.child(uid).child("dialog_group").child(_key);
                            //카테고리 uid추가(완료)
                            add_category_uid.child(uid).setValue(uid);
                            //카테고리 limit_count(증가 반영)
                            add_category_limit_count.setValue(_limit_count + 1);


                            //참가한 유저 GroupDialog 추가 db단위로 추가안하면 배열값으로 들어감.
                            //여기 limit_count 값 그냥 기존값 불러와서 +1하면됨(해결)
                            GroupDialog groupDialog = new GroupDialog(_count, _goal, _limit, _auth, _key, 0, _cate, _limit_count + 1, w_uid, name, uid, false);
                            add_GroupDialog_button_click_user.setValue(groupDialog);

                            //for문 돌려서 이미 있는 uid_key안의 uid추가
                            for (int i = 0; i < uid_key.size(); i++) {
                                add_GroupDialog_button_click_user.child("uid").child(uid_key.get(i)).setValue(uid_key.get(i));
                            }
                            //내 자신도 추가해야함!!
                            add_GroupDialog_button_click_user.child("uid").child(uid).setValue(uid);

                            Log.d("TAG", String.valueOf(uid_key) + "첫번째");
                            if (uid_key.size() + 1 == _limit) {
                                uid_key.add(uid);
                                int uid_size = uid_key.size();
                                Log.d("TAG", String.valueOf(uid_key) + "두번째");
                                //만든 유저 도장판 uid에 참가 uid 도장판 추가
                                for (int t = 0; t < uid_size; t++) {
                                    //uid 참가한 유저 배열 위치 순서대로 접근해서 저장해주기
                                    uid_fixed = databaseReference.child(uid_key.get(t)).child("goal_group").child(_key).child("도장판");
                                    uid_boolen = databaseReference.child(uid_key.get(t)).child("dialog_group").child(_key).child("close");
                                    uid_boolen.setValue(true);
                                    for (int j = 0; j < _count; j++) {
                                        addGoal(j);
                                    }


                                }

                                DatabaseReference remove_category = categoryReference.child(_cate).child(_key);
                                remove_category.removeValue();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction().remove(DetailFragment.this).commit();
                                fragmentManager.popBackStack();


                            }


                            Toast.makeText(view.getContext(), "참가됐습니다!", Toast.LENGTH_SHORT).show();

                        }

                    }
                }, 1000);


                //만약 참가한 사람이 limit만큼 찼다면 카테고리 값을 삭제해준다.
                //성공
                //채워지면 도장판도 생성해줘야함!! -> 작업시작
                //도장판 생성, uid기준 들어있는 사람들 group dialog - 유저 uid기준 -  goal_group - 리사이크럴뷰 아이디 기준 - 등록된 uid기준 - 각 도장판 배열 저장


            }
        });


        return view;
    }


    //클릭한 리사이클러뷰 아이템 값 가져오기 반영.
    private void GetBundle() {
        Bundle bundle = this.getArguments();

        _count = bundle.getInt("count");
        _limit = bundle.getInt("limit");
        _limit_count = bundle.getInt("limit_count");
        _goal = bundle.getString("goal");
        _auth = bundle.getString("auth");
        _cate = bundle.getString("cate");
        _key = bundle.getString("key");
        w_uid = bundle.getString("w_uid");

        Log.d("getBundle", _count + "/ " + _limit + "/ " + _goal + "/ " + _auth + "/ " + _cate);
        goal.setText(_goal);
        count.setText(String.valueOf(_count) + "개");
        auth.setText(_auth);
        limit.setText(String.valueOf(_limit));
        cate.setText(_cate);


    }


    //도장판칸 생성
    private void addGoal(int j) {
        gd = new g_GridItem(String.valueOf(j), not_uri);
        uid_fixed.child(String.valueOf(j)).setValue(gd);
        Log.d("TAG", "여기 돌고있나요? ");


    }


    //클릭한 카테고리 값 가져오기
    private void ReadCategoryDialog(String cate2, String key2) {
        uid_key.clear();
        categoryReference.child(cate2).child(key2).addListenerForSingleValueEvent(new ValueEventListener() {
            //@SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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

    //유저 이름 저장!
    private void Read_name() {
        profile_name.child("user").child(uid).child("userName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}