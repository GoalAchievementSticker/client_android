package com.example.java_sticker.group;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.java_sticker.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

/*그룹도장판*/
public class custom_g_goal_click extends Fragment {
    private TextView header_goal;
    private Intent intent;
    Custom_gAdapter adapter;
    g_GridItem gd;
    private ArrayList<g_GridItem> items = null;
    GridViewWithHeaderAndFooter gridView;
    //RecyclerView gridView;
    int count;
    String tittle;
    int goal_count;
    String key;
    String w_uid;
    String uid_auth;
    int p;

    //파이어베이스
    String uid;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("GroupDialog");
    private ImageView sticker_img;
    DatabaseReference ds;
    DatabaseReference uid_key_ds;


    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    private ValueEventListener postListener;


    Toolbar toolbar;
    ImageView s1, s2, s3, s4, s5;
    View v;
    BottomSheetDialog bsd;
    private View view;



    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        view = inflater.inflate(R.layout.activity_custom_ggoal_click, container, false);
        //toolbar
        toolbar = view.findViewById(R.id.goal_toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });


        // Create a storage reference from our app
        sticker_img = view.findViewById(R.id.sticker_img);
        items = new ArrayList<>();
        adapter = new Custom_gAdapter(getContext(), items);
        gridView = (GridViewWithHeaderAndFooter) view.findViewById(R.id.g_gridView);

        //파이어베이스
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();

        try {
            GetBundle();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        View header = getLayoutInflater().inflate(R.layout.header, null, false);
        header_goal = (TextView) header.findViewById(R.id.header_goal);
        gridView.addHeaderView(header);
        header_goal.setText(tittle);

        //bottom sheet
        v = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        bsd = new BottomSheetDialog(getActivity());
        bsd.setContentView(v);


        adapter.notifyDataSetChanged();

        s1 = v.findViewById(R.id.s1);
        s2 = v.findViewById(R.id.s2);
        s3 = v.findViewById(R.id.s3);
        s4 = v.findViewById(R.id.s4);
        s5 = v.findViewById(R.id.s5);


//        //그리드뷰 각 칸 클릭시, 데이터 수정
        gridView.setOnItemClickListener((adapterView, view, i, l) -> {
            Log.d("TAG", String.valueOf(i));
            stickerClick(i);

        });

        //0으로초기화 방지
        ReadPersonalDialog();
        gridView.setAdapter(adapter);

            //클릭한 사람의 정보 받아서 가져오기
            ds = databaseReference.child(uid_auth).child("goal_group").child(key).child("도장판");
            //도장판 읽어오기!
            ds.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (int i = 0; i < count; i++) {
                            ReadGoal(i);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {


                }
            });

        return view;

    }


    private void stickerClick(int i) {
        //bottom sheet dialog 보이기기
        bsd.show();
        //height 만큼 보이게 됨
        bsd.getBehavior().setState(STATE_COLLAPSED);

        s1.setOnClickListener(view -> {
            Toast.makeText(getContext(), "s1클릭", Toast.LENGTH_SHORT).show();
            storageRef.child("check.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        // Got the download URL for 'plus.png'
//                        gd = new GridItem(String.valueOf(i), uri.toString());
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //도장을 클릭했다면 프로그래스바 숫자를 늘린다
                        goal_count();
                    }).addOnFailureListener(Throwable::printStackTrace);
        });
        s2.setOnClickListener(view -> {
            Toast.makeText(getContext(), "s2클릭", Toast.LENGTH_SHORT).show();
            storageRef.child("sprout.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //도장을 클릭했다면 프로그래스바 숫자를 늘린다
                        goal_count();
                    }).addOnFailureListener(Throwable::printStackTrace);
            //도장을 클릭했다면 프로그래스바 숫자를 늘린다


        });
        s3.setOnClickListener(view -> {
            Toast.makeText(getContext(), "s3클릭", Toast.LENGTH_SHORT).show();
            storageRef.child("y_star.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        // Got the download URL for 'plus.png'
//                        gd = new GridItem(String.valueOf(i), uri.toString());
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //도장을 클릭했다면 프로그래스바 숫자를 늘린다
                        goal_count();
                    }).addOnFailureListener(Throwable::printStackTrace);
        });
        s4.setOnClickListener(view -> {
            Toast.makeText(getContext(), "s4클릭", Toast.LENGTH_SHORT).show();
            storageRef.child("triangular.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        // Got the download URL for 'plus.png'
//                        gd = new GridItem(String.valueOf(i), uri.toString());
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();
                        //도장을 클릭했다면 프로그래스바 숫자를 늘린다
                        goal_count();
                    }).addOnFailureListener(Throwable::printStackTrace);
        });
        s5.setOnClickListener(view -> {
            Toast.makeText(getContext(), "s5클릭", Toast.LENGTH_SHORT).show();
            storageRef.child("new-moon.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        // Got the download URL for 'plus.png'
//                        gd = new GridItem(String.valueOf(i), uri.toString());
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();
                        //도장을 클릭했다면 프로그래스바 숫자를 늘린다
                        goal_count();
                    }).addOnFailureListener(Throwable::printStackTrace);
        });



    }


    //클릭한 리사이클러뷰 아이템 값 가져오기 반영.
    private void GetBundle() {
        Bundle bundle = this.getArguments();
        count = bundle.getInt("count");
        tittle = bundle.getString("tittle");
        goal_count = bundle.getInt("goal_count");
        uid_auth= bundle.getString("uid_auth");
        key  = bundle.getString("key");
        w_uid = bundle.getString("w_uid");

    }



    //도장판 함수 가져오기!
    private ArrayList<g_GridItem> ReadGoal(int i) {
       ds.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    g_GridItem gridItem = dataSnapshot.getValue(g_GridItem.class);
                    //test
                    assert gridItem != null;
                    gridItem.setGoal_id(String.valueOf(i));
                    items.add(gridItem);

                }
                adapter.notifyDataSetChanged();
                gridView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(), "불러오기 실패", Toast.LENGTH_SHORT).show();
            }

        });

       //items를 리턴해서 프래그먼트 리스트에 넣어준다!
       return items;
    }

    //프로그래스바 숫자 늘리기
    private void goal_count() {
        databaseReference.child(uid_auth).child("dialog_group").child(key).child("gGoal").setValue(++p);
        ReadPersonalDialog();
    }

    //다이얼로그 저장된 함수 가져오기
    private int ReadPersonalDialog() {
        databaseReference.child(uid_auth).child("dialog_group").child(key).child("gGoal").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                p = snapshot.getValue(Integer.class);
                Log.d("TAG", String.valueOf(p));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return p;

    }



}
