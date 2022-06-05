package com.example.java_sticker.group;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.java_sticker.R;

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

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //마감직전에 다이얼로그로 정말 마감할지를 또 한번 물어봐주기
                //원래는 여기서 다시 제한줘야하는데 이미 어댑터에서 걸러서 굳이? 일단 보류
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
}