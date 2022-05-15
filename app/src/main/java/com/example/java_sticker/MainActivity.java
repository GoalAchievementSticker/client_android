package com.example.java_sticker;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/*리사이클러뷰 메인*/
public class MainActivity extends AppCompatActivity {


    private TextView header_goal;
    private CustomAdapter adapter;
    ArrayList<GridItem> items;
    ArrayList<personalDialog> pDialog;
    Dialog custom_dialog;
    String p_tittle;
    int p_count;
    Custom_p_item_adapter pAdapter;
    RecyclerView p_goal_recycler;
    ChildEventListener mchildEventListener;
    ProgressBar circleProgressBar;
    TextView custom_p_goal_tittle;
    String uid;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("personalDialog");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //다이얼로그 선언
        pDialog = new ArrayList<>();
        custom_dialog = new Dialog(MainActivity.this);
        custom_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        custom_dialog.setContentView(R.layout.custom_dialog);
        Button btn = (Button) findViewById(R.id.dialogButton); //소환버튼


        //리사이클러뷰 선언
        p_goal_recycler = (RecyclerView) findViewById(R.id.recyclerview_p_goal);
        circleProgressBar = (ProgressBar) findViewById(R.id.custom_p_goal_progressbar);
        custom_p_goal_tittle = (TextView) findViewById(R.id.custom_p_goal_tittle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        p_goal_recycler.setLayoutManager(linearLayoutManager);

        //리사이클러뷰 어댑터 연결
        pAdapter = new Custom_p_item_adapter(this,pDialog);
        p_goal_recycler.setAdapter(pAdapter);
        //리사이클러뷰 클릭했을때 나오는 도장판 연결
        adapter = new CustomAdapter();
        items = new ArrayList<GridItem>();



        //파이어베이스
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();



        //다이얼로그 값 저장된게 있다면
        if(pDialog != null) {
            ReadPersonalDialog();
        }



        //삽입버튼으로 값을 넣을때
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });


    }


    public void showDialog() {
        //다이얼로그를 보여준다
        custom_dialog.show();


        //다이얼로그에 있는 텍스트들 연결
        EditText sticker_count = (EditText) custom_dialog.findViewById(R.id.sticker_count);
        EditText sticker_goal = (EditText) custom_dialog.findViewById(R.id.sticker_goal);

        //예스, 노 버튼 연결
        Button noBtn = custom_dialog.findViewById(R.id.noBtn);
        Button yesBtn = custom_dialog.findViewById(R.id.yesBtn);

        //노버튼 클릭시 다이얼로그 닫기
        noBtn.setOnClickListener(view -> custom_dialog.dismiss());

        //예습 버튼 클릭시 다이얼로그 동작
        yesBtn.setOnClickListener(view -> {

            //다이얼로그에 입력한값 형 변환
            int vi = Integer.parseInt(sticker_count.getText().toString());
            String goal = sticker_goal.getText().toString();
            //파이어베이스 저장
            //고유키와 함께 저장히기 위한 장치
            String key = databaseReference.push().getKey();
            //list에 추가
            personalDialog personalDialog = new personalDialog(vi, goal, key);
            pDialog.add(personalDialog);

            pAdapter.notifyDataSetChanged();


            //헤시맵으로 키값과 함께 레코드 생성
//            HashMap result = new HashMap<>();
//            result.put("sticker_count", vi);
//            result.put("sticker_goal", goal);
//            result.put("key", key);

            //생성된 레코드 파이어베이스 저장
            DatabaseReference keyRef = databaseReference.child(uid).child("dialog_personal").child(key);
            keyRef.setValue(personalDialog);

            //databaseReference.addValueEventListener(postListener);
            //다이얼로그 값 저장
           // writePersonalDialog(vi, goal,key);



            for (int i = 0; i < vi; i++) {
                items.add(new GridItem(i, R.drawable.heart));
            }
            adapter.items = items;
            adapter.notifyDataSetChanged();
            DatabaseReference goalRef = databaseReference.child(uid).child("dialog_personal").child(key).child("도장판");
            goalRef.setValue(items);



            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ReadPersonalDialog();
                }
            },400);


            custom_dialog.dismiss();




        });




    }

    //다이얼로그 저장된 함수 가져오기
   private void ReadPersonalDialog() {
//        mchildEventListener = databaseReference.child(uid).child("dialog_personal").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                personalDialog personalDialog = snapshot.getValue(personalDialog.class);
//                pDialog.add(personalDialog);
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
       databaseReference.child(uid).child("dialog_personal").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               pDialog.clear();
               for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                   String key = dataSnapshot.getKey();
                   personalDialog read_p = dataSnapshot.getValue(personalDialog.class);
                   //String tittle = read_p.getpTittle();
                   //Toast.makeText(MainActivity.this, tittle,Toast.LENGTH_SHORT).show();
                   read_p.key = key;

                   pDialog.add(read_p);

               }
               pAdapter.notifyDataSetChanged();


           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

               Toast.makeText(MainActivity.this, "불러오기 실패", Toast.LENGTH_SHORT).show();
           }
       });

   }


}