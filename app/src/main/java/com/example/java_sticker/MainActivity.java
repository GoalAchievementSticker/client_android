package com.example.java_sticker;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private TextView header_goal;
    private CustomAdapter adapter;
    ArrayList<GridItem> items;
    //GridItem gitems;
    ArrayList<personalDialog> pDialog;
    Dialog custom_dialog;
    Custom_p_item_adapter pAdapter;
    RecyclerView p_goal_recycler;
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

        Button btn = (Button) findViewById(R.id.dialogButton);


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
        items = new ArrayList<>();
        adapter = new CustomAdapter(items);




        //파이어베이스
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();



        //다이얼로그 값 저장된게 있다면
        if(pDialog != null) {
            ReadPersonalDialog();
        }



        //삽입버튼으로 값을 넣을때
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                custom_dialog = new Dialog(MainActivity.this);
                custom_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                custom_dialog.setContentView(R.layout.custom_dialog);
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



            //생성된 레코드 파이어베이스 저장
            DatabaseReference keyRef = databaseReference.child(uid).child("dialog_personal").child(key);
            keyRef.setValue(personalDialog);


            //헤시맵으로 키값과 함께 레코드 생성
            Map<String, List<GridItem>> result = new HashMap<>();
            //result.put("key", i);
            //result.put("GridItem", gridItem);

            //String read = key+"A";
            DatabaseReference goalRef = databaseReference.child(uid).child("goal_personal").child(key).child("도장판");
            //String t = goalRef.push().getKey();
            for (int i = 0; i < vi; i++) {
                items.add(new GridItem( i,"test"));
            }
            result.put("key",items);
            goalRef.setValue(result);

//            for(int i =0; i< items.size(); i++){
//                GridItem gridItem = items.get(i);
//                goalRef.child(t).setValue(result);
//            }
            //result.put("key",items);
            //items.add(result);
            //adapter.notifyDataSetChanged();

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