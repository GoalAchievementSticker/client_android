package com.example.java_sticker.group;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.java_sticker.Fragment.DetailFragment;
import com.example.java_sticker.R;
import com.example.java_sticker.personal.MainActivity;
import com.example.java_sticker.personal.personalDialog;
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

public class close_add_goal extends AppCompatActivity {
    private Intent intent;

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

    Button close_button;

    Dialog custom_close_dialog;

    String uid;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference categoryReference = firebaseDatabase.getReference("Category");
    DatabaseReference databaseReference = firebaseDatabase.getReference("GroupDialog");
    DatabaseReference ds;

    List<String> uid_key;

    ArrayList<GroupDialog> gDialog;
    ArrayList<g_GridItem> items;

    String not_uri;

    //그리드뷰 데이터 저장
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    DatabaseReference uid_fixed;
    DatabaseReference uid_boolen;
    g_GridItem gd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_add_goal);

        close_button = (Button) findViewById(R.id.add_button);
        goal = (TextView) findViewById(R.id.goal);
        count = (TextView) findViewById(R.id.count);
        auth = (TextView) findViewById(R.id.auth);
        limit = (TextView) findViewById(R.id.limit);
        cate = (TextView) findViewById(R.id.cate);

        Getintent();

        uid_key = new ArrayList<>();
        gDialog = new ArrayList<>();

        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();

        //리사이클러뷰 클릭했을때 나오는 도장판 연결
        items = new ArrayList<g_GridItem>();

        gd = new g_GridItem();

        storageRef.child("not.png").getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    // Got the download URL for 'plus.png'
                    not_uri = uri.toString();

                }).addOnFailureListener(Throwable::printStackTrace);



        ds = databaseReference.child(uid).child("dialog_group").child(_key);

        ReadCategoryDialog();

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //마감직전에 다이얼로그로 정말 마감할지를 또 한번 물어봐주기
                //여기서 다시 제한

                showDialog();

            }
        });


    }


    //클릭한 리사이클러뷰 아이템 값 가져오기 반영.
    private void Getintent() {
        intent = getIntent();

        _count = intent.getIntExtra("count",0); //총도장수
        _limit = intent.getIntExtra("limit",2); //제한인원
        _limit_count = intent.getIntExtra("limit_count",1); //참가인원
        _goal = intent.getStringExtra("tittle"); //목표
        _auth = intent.getStringExtra("auth"); //인증방법
        _cate = intent.getStringExtra("cate"); //카테고리
        _key  = intent.getStringExtra("key"); //리사이클러뷰 키
        w_uid = intent.getStringExtra("w_uid"); //작성자 uid

        goal.setText(_goal);
        count.setText(_count +"개");
        auth.setText(_auth);
        limit.setText(_limit_count +"/"+ _limit);
        cate.setText(_cate);

    }

    public void showDialog() {
        custom_close_dialog = new Dialog(close_add_goal.this);
        custom_close_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        custom_close_dialog.setContentView(R.layout.close_dialog);
        //다이얼로그를 보여준다
        custom_close_dialog.show();



        //예스, 노 버튼 연결
        Button noBtn = custom_close_dialog.findViewById(R.id.noBtn);
        Button yesBtn = custom_close_dialog.findViewById(R.id.yesBtn);

        //노버튼 클릭시 다이얼로그 닫기
        noBtn.setOnClickListener(view -> {
            custom_close_dialog.dismiss();
        });

        //예스 버튼 클릭시 다이얼로그 동작
        yesBtn.setOnClickListener(view -> {

            //boolen 타입 true로 변경
            //도장판 생성, 카테고리 지우기
            int uid_size = uid_key.size();
            //만든 유저 도장판 uid에 참가 uid 도장판 추가
            for (int t = 0; t < uid_size; t++) {
                //uid 참가한 유저 배열 위치 순서대로 접근해서 저장해주기
                uid_fixed = databaseReference.child(uid_key.get(t)).child("goal_group").child(_key).child("도장판");
                //uid 참가한 유저 boolen타입 접근
                uid_boolen = databaseReference.child(uid_key.get(t)).child("dialog_group").child(_key).child("close");
                uid_boolen.setValue(true);
                for (int j = 0; j <_count; j++) {
                    addGoal(j);
                }


            }

            //카테고리 지우기 및 마감페이지 종료
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    DatabaseReference remove_category = categoryReference.child(_cate).child(_key);
                    remove_category.removeValue();
                    custom_close_dialog.dismiss();

                    finish();
                }
            },400);



        });


    }
    //도장판칸 생성
    private void addGoal(int j) {
        gd = new g_GridItem(String.valueOf(j), not_uri);
        uid_fixed.child(String.valueOf(j)).setValue(gd);
        Log.d("TAG", "여기 돌고있나요? ");


    }

    //클릭한 uid 가져오기
    private void ReadCategoryDialog() {
        uid_key.clear();
        ds.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.child("uid").getChildren()) {
                    uid_key.add(dataSnapshot.getValue(String.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}