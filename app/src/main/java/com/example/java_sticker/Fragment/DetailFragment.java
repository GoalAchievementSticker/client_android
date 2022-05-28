package com.example.java_sticker.Fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;


import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.java_sticker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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

    String _count;
    int _limit;
    String _goal;
    String _auth;
    String _cate;
    String _key;

    String uid;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference categoryReference = firebaseDatabase.getReference("Category");

    List<String> uid_key;

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
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();


        goal = view.findViewById(R.id.goal);
        count = view.findViewById(R.id.count);
        auth = view.findViewById(R.id.auth);
        limit = view.findViewById(R.id.limit);
        cate = view.findViewById(R.id.cate);


        //ReadGroupDialog(_cate, _key);

        try {
            GetBundle();
            ReadGroupDialog(_cate, _key);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(uid_key.get(0) == uid){
                    //이미 있는 사람이면 참가못함
                    Log.d("TAG", "참가못함");
                }else{
                    //아니니까 참가 할수있음
                    Log.d("TAG", "참가해라");
                }
            }
        },500);
        //Log.d("TAG", String.valueOf(uid_key));



//        if(uid_key.get(0) == uid){
//
//        }

        // Inflate the layout for this fragment
        return view;
    }

    private void GetBundle() {
        Bundle bundle = this.getArguments();

        _count = bundle.getString("count");
        _limit = bundle.getInt("limit");
        _goal = bundle.getString("goal");
        _auth = bundle.getString("auth");
        _cate = bundle.getString("cate");
        _key  = bundle.getString("key");

        Log.d("getBundle",_count+"/ "+_limit+"/ "+_goal+"/ "+_auth+"/ "+_cate);
        goal.setText(_goal);
        count.setText(_count);
        auth.setText(_auth);
        limit.setText(String.valueOf(_limit));
        cate.setText(_cate);

    }


    //클릭한 카테고리 값 가져오기
    private void ReadGroupDialog(String cate2, String key2) {
        uid_key.clear();
        categoryReference.child(cate2).child(key2).addValueEventListener(new ValueEventListener() {
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
}