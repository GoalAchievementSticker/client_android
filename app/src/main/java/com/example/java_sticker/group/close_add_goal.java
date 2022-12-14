package com.example.java_sticker.group;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

    //???????????? ????????? ??????
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

        //?????????????????? ??????????????? ????????? ????????? ??????
        items = new ArrayList<g_GridItem>();

        gd = new g_GridItem();

        storageRef.child("not2.png").getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    // Got the download URL for 'plus.png'
                    not_uri = uri.toString();

                }).addOnFailureListener(Throwable::printStackTrace);



        ds = databaseReference.child(uid).child("dialog_group").child(_key);

        ReadCategoryDialog();

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //??????????????? ?????????????????? ?????? ??????????????? ??? ?????? ???????????????
                //????????? ?????? ??????

                showDialog();

            }
        });


    }


    //????????? ?????????????????? ????????? ??? ???????????? ??????.
    private void Getintent() {
        intent = getIntent();

        _count = intent.getIntExtra("count",0); //????????????
        _limit = intent.getIntExtra("limit",2); //????????????
        _limit_count = intent.getIntExtra("limit_count",1); //????????????
        _goal = intent.getStringExtra("tittle"); //??????
        _auth = intent.getStringExtra("auth"); //????????????
        _cate = intent.getStringExtra("cate"); //????????????
        _key  = intent.getStringExtra("key"); //?????????????????? ???
        w_uid = intent.getStringExtra("w_uid"); //????????? uid

        goal.setText(_goal);
        count.setText(_count +"???");
        auth.setText(_auth);
        limit.setText(_limit_count +"/"+ _limit);
        cate.setText(_cate);

    }

    public void showDialog() {
        custom_close_dialog = new Dialog(close_add_goal.this);
        custom_close_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        custom_close_dialog.setContentView(R.layout.close_dialog);
        custom_close_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //?????????????????? ????????????
        custom_close_dialog.show();



        //??????, ??? ?????? ??????
        Button noBtn = custom_close_dialog.findViewById(R.id.noBtn);
        Button yesBtn = custom_close_dialog.findViewById(R.id.yesBtn);

        //????????? ????????? ??????????????? ??????
        noBtn.setOnClickListener(view -> {
            custom_close_dialog.dismiss();
        });

        //?????? ?????? ????????? ??????????????? ??????
        yesBtn.setOnClickListener(view -> {

            //boolen ?????? true??? ??????
            //????????? ??????, ???????????? ?????????
            int uid_size = uid_key.size();
            //?????? ?????? ????????? uid??? ?????? uid ????????? ??????
            for (int t = 0; t < uid_size; t++) {
                //uid ????????? ?????? ?????? ?????? ???????????? ???????????? ???????????????
                uid_fixed = databaseReference.child(uid_key.get(t)).child("goal_group").child(_key).child("?????????");
                //uid ????????? ?????? boolen?????? ??????
                uid_boolen = databaseReference.child(uid_key.get(t)).child("dialog_group").child(_key).child("close");
                uid_boolen.setValue(true);
                for (int j = 0; j <_count; j++) {
                    addGoal(j);
                }


            }

            //???????????? ????????? ??? ??????????????? ??????
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
    //???????????? ??????
    private void addGoal(int j) {
        gd = new g_GridItem(String.valueOf(j), not_uri);
        uid_fixed.child(String.valueOf(j)).setValue(gd);
        Log.d("TAG", "?????? ???????????????? ");


    }

    //????????? uid ????????????
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