package com.example.java_sticker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.HashMap;
import java.util.List;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class custom_p_goal_click extends AppCompatActivity {

    private TextView header_goal;
    private Intent intent;
    private Intent intent3;
    CustomAdapter adapter;
    GridItem gd;
    private ArrayList<GridItem> items = null;
    GridViewWithHeaderAndFooter gridView;
    //RecyclerView gridView;
    Dialog custom_dialog;
    String p_tittle;
    String key;
    String uid;
    int count;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("personalDialog");
    private ImageView sticker_img;
    DatabaseReference ds;
    String dss;
    int click;
    int point_1_index;
    //List<String> ds;
    String pos;
    String td;
    private List<String> goal_key = new ArrayList<>();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    private ValueEventListener postListener;
    LayoutInflater inflater;
    ViewGroup viewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_pgoal_click);

        // Create a storage reference from our app
        View myView = inflater.inflate(R.layout.custom_grid, viewGroup, false);

        sticker_img = myView.findViewById(R.id.sticker_img);
        items = new ArrayList<>();
        adapter = new CustomAdapter(this, items);
        gridView = (GridViewWithHeaderAndFooter) findViewById(R.id.gridView);
        //pDialog = new ArrayList<personalDialog>();
        //items = new ArrayList<>();
        //파이어베이스
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();
        intent = getIntent();
        p_tittle = intent.getStringExtra("tittle");
        key = intent.getStringExtra("key");
        count = intent.getIntExtra("count", 5);
        intent3 = getIntent();
        // pos = intent3.getIntExtra("pos", pos);

        //Log.d("TAG", pos);


        View header = getLayoutInflater().inflate(R.layout.header, null, false);
        header_goal = (TextView) header.findViewById(R.id.header_goal);
        gridView.addHeaderView(header);
        //adapter = new CustomAdapter(items);
        //gridView.setAdapter(adapter);
        //gridView.setAdapter(adapter);

        //ds = new ArrayList<>();
        ds = databaseReference.child(uid).child("goal_personal").child(key).child("도장판");
        td = ds.push().getKey();
        DatabaseReference ke = databaseReference.child(uid).child("goal_personal");

        //Log.d("TAG", ds.child(key).getKey());

        header_goal.setText(p_tittle);


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


        //그리드뷰 각 칸 클릭시, 데이터 수정
        gridView.setOnItemClickListener((adapterView, view, i, l) -> {
            //이 위치는 어댑터에서 가져온 pos, 즉 position값을 의미한다
            //i = pos;
            //     final GridItem gi=(GridItem) adapterView.getItemAtPosition(i);
            Log.d("TAG", String.valueOf(i));


            // ds.child(String.valueOf(i)).child("goal_id").setValue("GOALID");
            // ds.child(String.valueOf(i)).child("test").setValue("TeSt");


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


//                Map<String, String> os = new HashMap<>();
//                os.put("test", "무지개");
//                //키값 배열에서 pos라는 위치의 값을 가져와서 업데이트하는데 안에 뭘 넣지. .
//                //ds.child(goal_key.get(i)).updateChildren();
        });

        // ReadPersonalDialog2();


        //Log.d("TAG", String.valueOf(adapter));

        //adapter.notifyDataSetChanged();

        //adapter.notifyDataSetChanged();

        gridView.setAdapter(adapter);

    }

    ;

    private GridItem addGoal(int i) {
        // Handle any errors
        storageRef.child("plus.png").getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    // Got the download URL for 'plus.png'
                    gd = new GridItem(String.valueOf(i), uri.toString());
                    ds.child(String.valueOf(i)).setValue(gd);
                    Log.d("TAG", uri.toString());

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
//                    Glide.with(this)
//                            .placeholder(e.categoryDrawable) // while loading
//                            .fallback(e.categoryDrawable) // if load was null
//                            .error(R.drawable.error) // e.g. if path is not valid image
//                            .load(e.imagePath) // null or non-null image
//                            .into(imageView);

                    Glide.with(custom_p_goal_click.this)
                            .load("https://firebasestorage.googleapis.com/v0/b/goal-sticker.appspot.com/o/plus.png?alt=media&token=60672783-93d1-4bae-bc7e-9a1db789be61")
                            .into(sticker_img);

                    items.add(gridItem);
                    Log.d("TAG", "gridItem.getTest(): " + gridItem.getTest());
//                    Log.d("TAG", "gd.getTest(): "+gd.getTest());
                    Log.d("TAG", "sticker_img: " + sticker_img);
                    //키만 넣는 배열
                    //goal_key.add(key);

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