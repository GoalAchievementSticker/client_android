package com.example.java_sticker;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class custom_p_goal_click extends AppCompatActivity {

    private TextView header_goal;
    private Intent intent;
    CustomAdapter adapter;
    GridItem gd;
    private ArrayList<GridItem> items = null;
    GridViewWithHeaderAndFooter gridView;
    //RecyclerView gridView;
    String p_tittle;
    String key;
    String uid;
    int count;

    //파이어베이스
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("personalDialog");
    private ImageView sticker_img;
    DatabaseReference ds;

    private List<String> goal_key = new ArrayList<>();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    private ValueEventListener postListener;

    Toolbar toolbar;
    ImageView s1, s2, s3, s4, s5;
    View v;
    BottomSheetDialog bsd;

    @SuppressLint({"NonConstantResourceId", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_pgoal_click);

        //toolbar
        toolbar = (Toolbar) findViewById(R.id.goal_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");


        // Create a storage reference from our app
        sticker_img = findViewById(R.id.sticker_img);
        items = new ArrayList<>();
        adapter = new CustomAdapter(this, items);
        gridView = (GridViewWithHeaderAndFooter) findViewById(R.id.gridView);

        //파이어베이스
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();
        intent = getIntent();
        p_tittle = intent.getStringExtra("tittle");
        key = intent.getStringExtra("key");
        count = intent.getIntExtra("count", 5);


        View header = getLayoutInflater().inflate(R.layout.header, null, false);
        header_goal = (TextView) header.findViewById(R.id.header_goal);
        gridView.addHeaderView(header);


        ds = databaseReference.child(uid).child("goal_personal").child(key).child("도장판");


        header_goal.setText(p_tittle);

        //bottom sheet
        v = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        bsd = new BottomSheetDialog(this);
        bsd.setContentView(v);

        // 도장판이 존재한다면 읽어오기, 없다면 for문 만큼 생성
        ds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (int i = 0; i < count; i++)
                        ReadPersonalDialog2(i);
                } else {
                    for (int i = 0; i < count; i++) {
                        items.add(addGoal(i));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });


        adapter.notifyDataSetChanged();


//        ll = findViewById(R.id.select_sticker);
        s1 = v.findViewById(R.id.s1);
        s2 = v.findViewById(R.id.s2);
        s3 = v.findViewById(R.id.s3);
        s4 = v.findViewById(R.id.s4);
        s5 = v.findViewById(R.id.s5);


//        //그리드뷰 각 칸 클릭시, 데이터 수정
        gridView.setOnItemClickListener((adapterView, view, i, l) -> {
            Log.d("TAG", String.valueOf(i));

            stickerClick(i);

//
//            switch (v.getId()) {
//                //check
//                case R.id.s1:
//
//                    Toast.makeText(this, "s1클릭", Toast.LENGTH_SHORT).show();
////                storageRef.child("check.png").getDownloadUrl()
////                        .addOnSuccessListener(uri -> {
////                            // Got the download URL for 'plus.png'
//////                        gd = new GridItem(String.valueOf(i), uri.toString());
////                            ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
////                            Log.d("TAG", uri.toString());
////
////                        }).addOnFailureListener(Throwable::printStackTrace);
//
//                    break;
//                //sprout
//                case R.id.s2:
//                    Toast.makeText(this, "s2클릭", Toast.LENGTH_SHORT).show();
////                storageRef.child("sprout.png").getDownloadUrl()
////                        .addOnSuccessListener(uri -> {
////                            // Got the download URL for 'plus.png'
//////                        gd = new GridItem(String.valueOf(i), uri.toString());
////                            ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
////                            Log.d("TAG", uri.toString());
////
////                        }).addOnFailureListener(Throwable::printStackTrace);
//                    break;
//                //star
//                case R.id.s3:
//                    Toast.makeText(this, "s3클릭", Toast.LENGTH_SHORT).show();
////                storageRef.child("y_star.png").getDownloadUrl()
////                        .addOnSuccessListener(uri -> {
////                            // Got the download URL for 'plus.png'
//////                        gd = new GridItem(String.valueOf(i), uri.toString());
////                            ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
////                            Log.d("TAG", uri.toString());
////
////                        }).addOnFailureListener(Throwable::printStackTrace);
//                    break;
//                //triangular
//                case R.id.s4:
//                    Toast.makeText(this, "s4클릭", Toast.LENGTH_SHORT).show();
////                storageRef.child("triangular.png").getDownloadUrl()
////                        .addOnSuccessListener(uri -> {
////                            // Got the download URL for 'plus.png'
//////                        gd = new GridItem(String.valueOf(i), uri.toString());
////                            ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
////                            Log.d("TAG", uri.toString());
////
////                        }).addOnFailureListener(Throwable::printStackTrace);
//                    break;
//                //new-moon
//                case R.id.s5:
//                    Toast.makeText(this, "s5클릭", Toast.LENGTH_SHORT).show();
////                storageRef.child("new-moon.png").getDownloadUrl()
////                        .addOnSuccessListener(uri -> {
////                            // Got the download URL for 'plus.png'
//////                        gd = new GridItem(String.valueOf(i), uri.toString());
////                            ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
////                            Log.d("TAG", uri.toString());
////
////                        }).addOnFailureListener(Throwable::printStackTrace);
//                    break;
//            }


//            ds.child(String.valueOf(i)).child("goal_id").setValue("GOALID");


            Log.d("TAG", storageRef.child("heart.png").getDownloadUrl().toString());
            // Glide.with(view).load(storageRef).into(ds.child(String.valueOf(i)).child("test")); // Glide를 사용하여 이미지 로드
//                try{
//                    HashMap<String,Object> map=new HashMap<>();
//                    map.put(String.valueOf(i),String.valueOf(i));
//
//                    ds.child(td).child(goal_key.get(i)).updateChildren(map,(databaseError, databaseReference)->{
//
//                    } );
//                }catch(IndexOutOfBoundsException e){
//                    e.printStackTrace();}
//

        });


        gridView.setAdapter(adapter);

    }

    private void stickerClick(int i) {
        //bottom sheet dialog 보이기기
        bsd.show();
        //height 만큼 보이게 됨
        bsd.getBehavior().setState(STATE_COLLAPSED);

        s1.setOnClickListener(view -> {
            Toast.makeText(this, "s1클릭", Toast.LENGTH_SHORT).show();
            storageRef.child("check.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        // Got the download URL for 'plus.png'
//                        gd = new GridItem(String.valueOf(i), uri.toString());
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                    }).addOnFailureListener(Throwable::printStackTrace);
        });
        s2.setOnClickListener(view -> {
            Toast.makeText(this, "s2클릭", Toast.LENGTH_SHORT).show();
            storageRef.child("sprout.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        // Got the download URL for 'plus.png'
//                        gd = new GridItem(String.valueOf(i), uri.toString());
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                    }).addOnFailureListener(Throwable::printStackTrace);
        });
        s3.setOnClickListener(view -> {
            Toast.makeText(this, "s3클릭", Toast.LENGTH_SHORT).show();
            storageRef.child("y_star.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        // Got the download URL for 'plus.png'
//                        gd = new GridItem(String.valueOf(i), uri.toString());
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                    }).addOnFailureListener(Throwable::printStackTrace);
        });
        s4.setOnClickListener(view -> {
            Toast.makeText(this, "s4클릭", Toast.LENGTH_SHORT).show();
            storageRef.child("triangular.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        // Got the download URL for 'plus.png'
//                        gd = new GridItem(String.valueOf(i), uri.toString());
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();
                    }).addOnFailureListener(Throwable::printStackTrace);
        });
        s5.setOnClickListener(view -> {
            Toast.makeText(this, "s5클릭", Toast.LENGTH_SHORT).show();
            storageRef.child("new-moon.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        // Got the download URL for 'plus.png'
//                        gd = new GridItem(String.valueOf(i), uri.toString());
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();
                    }).addOnFailureListener(Throwable::printStackTrace);
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private GridItem addGoal(int i) {
        // Handle any errors
        storageRef.child("not.png").getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    // Got the download URL for 'plus.png'
                    gd = new GridItem(String.valueOf(i), uri.toString());
                    ds.child(String.valueOf(i)).setValue(gd);

                }).addOnFailureListener(Throwable::printStackTrace);

//        assert td != null;
        //Log.d("TAG", String.valueOf(storageRef.child("plus.png").getDownloadUrl()));
        return gd;
    }


    //다이얼로그 저장된 함수 가져오기
    private void ReadPersonalDialog2(int i) {

        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                goal_key.clear();
                // sticker_img.setImageResource(0);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    GridItem gridItem = dataSnapshot.getValue(GridItem.class);


                    //test
                    assert gridItem != null;
                    gridItem.goal_id = String.valueOf(i);

//                    Glide.with(custom_p_goal_click.this)
//                            .load(gridItem.getTest())
//                            .into(sticker_img);


                    items.add(gridItem);

                }
                adapter.notifyDataSetChanged();
                gridView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(custom_p_goal_click.this, "불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        };
        ds.addValueEventListener(postListener);
    }

}