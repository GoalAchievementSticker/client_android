package com.example.java_sticker.personal;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.java_sticker.CustomProgress;
import com.example.java_sticker.Group_main;
import com.example.java_sticker.R;
import com.example.java_sticker.mypage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
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
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class MainActivity extends AppCompatActivity {


    private TextView header_goal;
    private Custom_pAdapter adapter;
    ArrayList<GridItem> items;
    //GridItem gitems;
    ArrayList<personalDialog> pDialog;
    Dialog custom_dialog;
    Custom_p_item_adapter pAdapter;
    RecyclerView p_goal_recycler;
    //ProgressBar circleProgressBar;
    com.example.java_sticker.CustomProgress customProgress;
    TextView custom_p_goal_tittle;

    String uid;
    GridViewWithHeaderAndFooter gridView;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("personalDialog");
    DatabaseReference profile_databaseReference = firebaseDatabase.getReference();

    //??????????????? ?????????
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle barDrawerToggle;
    FloatingActionButton fab_main;

    //FAB
    boolean isFabOpen;

    //nav??? ?????????,?????????, ??????
    TextView nav_name;
    TextView user_email;
    CircleImageView nav_img;

    //???????????? ????????? ??????
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    DatabaseReference ds;
    GridItem gd;

    View view;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //??????????????? ??????
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawerLayout = (DrawerLayout) findViewById(R.id.layout_drawer);
        navigationView = (NavigationView) findViewById(R.id.nav);
        navigationView.setItemIconTintList(null);

        //??????????????????
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();


        //Drawer ???????????? ??????
        barDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        //??????????????? ???????????? ?????????, ????????????
        barDrawerToggle.syncState();
        //?????? ????????? ????????? ????????? ?????? ??????
        drawerLayout.addDrawerListener(barDrawerToggle);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);


        //??????????????? ????????? ??????,?????????, ????????? ????????????
        profile_databaseReference.child("user").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("userName").getValue(String.class);
                String uri = snapshot.child("profileImageUrl").getValue(String.class);
                String email=snapshot.child("userEmail").getValue(String.class);
                nav_img = findViewById(R.id.iv_header);
                Glide.with(navigationView).load(uri).into(nav_img);
                nav_name = findViewById(R.id.nav_name);
                user_email=findViewById(R.id.user_email);
                user_email.setText(email);
                nav_name.setText(name+"???");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //FAT
        fab_main = findViewById(R.id.fab);
        isFabOpen = false; // Fab ?????? default??? ????????????

        //FAB ?????? ???
        fab_main.setOnClickListener(view -> toggleFab());


        //????????????????????? ????????? ?????????
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.group_goal:
                    Intent groupIntent = new Intent(MainActivity.this, Group_main.class);
                    startActivity(groupIntent);
                    finish();
                    //Toast.makeText(MainActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
                    break;
            }

            drawerLayout.closeDrawer(navigationView);

            return false;
        });


        //??????????????? ??????
        pDialog = new ArrayList<>();
        gridView = (GridViewWithHeaderAndFooter) findViewById(R.id.gridView);
        //Button btn = (Button) findViewById(R.id.dialogButton);


        //?????????????????? ??????
        p_goal_recycler = (RecyclerView) findViewById(R.id.recyclerview_p_goal);
        //circleProgressBar = (ProgressBar) findViewById(R.id.custom_p_goal_progressbar);
        customProgress = (CustomProgress) findViewById(R.id.customProgress_g_goal_c);
        custom_p_goal_tittle = (TextView) findViewById(R.id.custom_p_goal_tittle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        p_goal_recycler.setLayoutManager(linearLayoutManager);

        //?????????????????? ????????? ??????
        pAdapter = new Custom_p_item_adapter(this, pDialog);
        p_goal_recycler.setAdapter(pAdapter);
        //?????????????????? ??????????????? ????????? ????????? ??????
        items = new ArrayList<>();
        adapter = new Custom_pAdapter(this, items);


        //??????????????? ??? ???????????? ?????????
        if (pDialog != null) {
            ReadPersonalDialog();

        }


    }

    //?????? FAB ?????? ???
    @SuppressLint("Recycle")
    private void toggleFab() {
        // ????????? ?????? ?????? ??????
        showDialog();
        ObjectAnimator.ofFloat(fab_main, View.ROTATION, 0f, 45f).start();
    }


    public void showDialog() {
        custom_dialog = new Dialog(MainActivity.this);
        custom_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        custom_dialog.setContentView(R.layout.custom_p_dialog);
        custom_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //?????????????????? ????????????
        custom_dialog.show();


        //?????????????????? ?????? ???????????? ??????
        EditText sticker_count = (EditText) custom_dialog.findViewById(R.id.sticker_count);
        EditText sticker_goal = (EditText) custom_dialog.findViewById(R.id.sticker_goal);

        //??????, ??? ?????? ??????
        Button noBtn = custom_dialog.findViewById(R.id.noBtn);
        Button yesBtn = custom_dialog.findViewById(R.id.yesBtn);

        custom_dialog.setOnDismissListener(view -> {
                    ObjectAnimator.ofFloat(fab_main, View.ROTATION, 45f, 0f).start();
                }
        );
        //????????? ????????? ??????????????? ??????
        noBtn.setOnClickListener(view -> {
            custom_dialog.dismiss();
        });

        //?????? ?????? ????????? ??????????????? ??????
        yesBtn.setOnClickListener(view -> {

            //?????????????????? ???????????? ??? ??????
            int vi = Integer.parseInt(sticker_count.getText().toString());
            String goal = sticker_goal.getText().toString();
            //?????????????????? ??????
            //???????????? ?????? ???????????? ?????? ??????
            String key = databaseReference.push().getKey();
            DatabaseReference keyRef = databaseReference.child(uid).child("dialog_personal").child(key);
            //list??? ??????
            personalDialog personalDialog = new personalDialog(vi, goal, key, 0, "");
            pDialog.add(personalDialog);

            pAdapter.notifyDataSetChanged();


            //????????? ????????? ?????????????????? ??????
            keyRef.setValue(personalDialog);


            //????????? gridview ????????? ??????
            ds = databaseReference.child(uid).child("goal_personal").child(key).child("?????????");
            for (int i = 0; i < vi; i++) {
                items.add(addGoal(i));
            }

            new Handler().postDelayed(() -> ReadPersonalDialog(), 400);


            custom_dialog.dismiss();


        });


    }


    //???????????? ??????
    private GridItem addGoal(int i) {
        // Handle any errors
        storageRef.child("not2.png").getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    // Got the download URL for 'plus.png'
                    gd = new GridItem(String.valueOf(i), uri.toString());
                    ds.child(String.valueOf(i)).setValue(gd);

                }).addOnFailureListener(Throwable::printStackTrace);

        return gd;
    }

    //??????????????? ????????? ?????? ????????????
    private void ReadPersonalDialog() {

        databaseReference.child(uid).child("dialog_personal").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pDialog.clear();
                // Log.d("TAG", String.valueOf(snapshot));
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    personalDialog read_p = dataSnapshot.getValue(personalDialog.class);
                    assert read_p != null;
                    read_p.key = key;
                    //Log.d("TAG", key);

                    pDialog.add(read_p);

                }
                pAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MainActivity.this, "???????????? ??????", Toast.LENGTH_SHORT).show();
            }
        });

    }


}