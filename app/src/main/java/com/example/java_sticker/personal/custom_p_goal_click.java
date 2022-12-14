package com.example.java_sticker.personal;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class custom_p_goal_click extends AppCompatActivity {

    private TextView header_goal;
    private Intent intent;
    Custom_pAdapter adapter;
    GridItem gd;
    private ArrayList<GridItem> items;
    GridViewWithHeaderAndFooter gridView;
    //RecyclerView gridView;
    String p_tittle;
    String key;
    String uid;
    int count;
    int goal_count;
    int p;

    //??????????????????
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("personalDialog");
    ImageView sticker_img;
    DatabaseReference ds;

    private List<String> goal_key = new ArrayList<>();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    private ValueEventListener postListener;



    Toolbar toolbar;
    ImageView s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15, s16;
    View v;
    BottomSheetDialog bsd;


    //??????
    Date mDate;
    long mNow;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");

    String date;



    @SuppressLint({"NonConstantResourceId", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_pgoal_click);

        //toolbar
        toolbar = (Toolbar) findViewById(R.id.goal_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //toolbar.inflateMenu(R.menu.goal_menu);



        // Create a storage reference from our app
        sticker_img = findViewById(R.id.sticker_img_2);
        items = new ArrayList<>();
        adapter = new Custom_pAdapter(this, items);
        gridView = (GridViewWithHeaderAndFooter) findViewById(R.id.gridView);

        //??????????????????
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();
        intent = getIntent();
        p_tittle = intent.getStringExtra("tittle");
        key = intent.getStringExtra("key");
        count = intent.getIntExtra("count", 5);
        goal_count = intent.getIntExtra("goal_count",0);



        View header = getLayoutInflater().inflate(R.layout.header, null, false);
        header_goal = (TextView) header.findViewById(R.id.header_goal);
        gridView.addHeaderView(header);
        gridView.setAdapter(adapter);



        ds = databaseReference.child(uid).child("goal_personal").child(key).child("?????????");
        header_goal.setText(p_tittle);

        //bottom sheet
        v = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        bsd = new BottomSheetDialog(this);
        bsd.setContentView(v);

        // ???????????? ??????????????? ????????????, ????????? for??? ?????? ??????
        ds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (int i = 0; i < count; i++)
                        ReadPersonalDialog2(i);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

        //?????? ????????????
        ReadPersonalDate();

        Log.d("TAG", "date"+date);
        Log.d("TAG", "getTime"+getTime());


        adapter.notifyDataSetChanged();

        s1 = v.findViewById(R.id.s1);
        s2 = v.findViewById(R.id.s2);
        s3 = v.findViewById(R.id.s3);
        s4 = v.findViewById(R.id.s4);
        s5 = v.findViewById(R.id.s5);
        s6 = v.findViewById(R.id.s6);
        s7 = v.findViewById(R.id.s7);
        s8 = v.findViewById(R.id.s8);
        s9 = v.findViewById(R.id.s9);
        s10 = v.findViewById(R.id.s10);
        s11 = v.findViewById(R.id.s11);
        s12 = v.findViewById(R.id.s12);
        s13 = v.findViewById(R.id.s13);
        s14 = v.findViewById(R.id.s14);
        s15 = v.findViewById(R.id.s15);
        s16 = v.findViewById(R.id.s16);


//        //???????????? ??? ??? ?????????, ????????? ??????
        gridView.setOnItemClickListener((adapterView, view, i, l) -> {
            Log.d("TAG", String.valueOf(i));


            //??????????????? ????????? ?????? ????????? ?????????
            if(date.equals(getTime())){
                //????????? ?????? ?????? ??????
                Toast.makeText(getApplicationContext(), "????????? ????????? ??????????????????", Toast.LENGTH_SHORT).show();
            }else{
                //???????????? ????????? ?????? db??? ????????? ?????? ????????? ???????????????
                stickerClick(i);

            }

        });



        //0??????????????? ??????
        ReadPersonalDialog();
        //gridView.setAdapter(adapter);

    }

    //?????? ?????????
    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);

        Log.d("TAG", "?????? ??????"+mFormat.format(mDate));
        return mFormat.format(mDate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.goal_p_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_p_goal:
                //Toast.makeText(getApplicationContext(), "??????", Toast.LENGTH_LONG).show();

                Log.d("TAG", "?????? ?????? ????????????????");
                AlertDialog.Builder msgBuilder = new AlertDialog.Builder(custom_p_goal_click.this)
                        .setTitle("??????")
                        .setMessage("?????? ???????????? ?????????????????????????")
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Toast.makeText(getApplicationContext(), "??????", Toast.LENGTH_SHORT).show();
                                View container;
                                container = getWindow().getDecorView();
                                container.buildDrawingCache();
                                Bitmap captureView = container.getDrawingCache();

                                //????????? ????????????

                                 //?????? ??????
                                File adress = Environment.getExternalStoragePublicDirectory("/DCIM/Camera/");

                                FileOutputStream fos;
                                if (!adress.exists()) {
                                     adress.mkdir();
                                }

                                //??????
                                String strFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/";
                                try {
                                    fos = new FileOutputStream(strFilePath + System.currentTimeMillis() + ".jpeg");
                                    captureView.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                                    //sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(adress))));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                 }

                                 //????????? ??????
                                // Uri uir = Uri.fromFile(new File(adress));
                            }
                        })
                        .setNeutralButton("??????", null);

                AlertDialog msgDlg = msgBuilder.create();
                msgDlg.show();
                return true;
                //????????? ???????????? ??? ?????? ????????? ???
                //?????? ?????? ?????? ??????
//                builder.setTitle("??????").setMessage("?????? ???????????? ?????????????????????????").setPositiveButton("????????????", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                        View container;
//                        container = getWindow().getDecorView();
//                        container.buildDrawingCache();
//                        Bitmap captureView = container.getDrawingCache();
//
//                        //????????? ????????????
//
//                        //?????? ??????
//                        File adress = Environment.getExternalStoragePublicDirectory("/DCIM/Camera/");
//
//                        FileOutputStream fos;
//                        if (!adress.exists()) {
//                            adress.mkdir();
//                        }
//
//                        //??????
//                        String strFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/";
//                        try {
//                            fos = new FileOutputStream(strFilePath + System.currentTimeMillis() + ".jpeg");
//                            captureView.compress(Bitmap.CompressFormat.JPEG, 90, fos);
//                            //sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(adress))));
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                        //????????? ??????
//                        // Uri uir = Uri.fromFile(new File(adress));
//
//                        //?????? uri???????????? intent??? ??? ???????????????
//
//                    }
//                }).setNeutralButton("??????", null).show();
            default:
                Toast.makeText(getApplicationContext(), "??????", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
        }

    }


    private void stickerClick(int i) {
        //bottom sheet dialog ????????????
        bsd.show();
        //height ?????? ????????? ???
        bsd.getBehavior().setState(STATE_COLLAPSED);

        //s1??????
        s1.setOnClickListener(view -> {
                storageRef.child("goal_sticker/cat_green.png").getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                            bsd.dismiss();

                            //????????? ??????????????? ?????????????????? ????????? ?????????
                            goal_count();
                            //????????? db??? ????????????
                            databaseReference.child(uid).child("dialog_personal").child(key).child("date").setValue(getTime());
                            ReadPersonalDate();


                        }).addOnFailureListener(Throwable::printStackTrace);

        });

        //s2??????
        s2.setOnClickListener(view -> {
                storageRef.child("goal_sticker/cat_black.png").getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                            bsd.dismiss();

                            //????????? ??????????????? ?????????????????? ????????? ?????????
                            goal_count();
                            //????????? db??? ????????????
                            databaseReference.child(uid).child("dialog_personal").child(key).child("date").setValue(getTime());
                            ReadPersonalDate();


                        }).addOnFailureListener(Throwable::printStackTrace);

        });

        //s3??????
        s3.setOnClickListener(view -> {
                storageRef.child("goal_sticker/cat_grap.png").getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                            bsd.dismiss();

                            //????????? ??????????????? ?????????????????? ????????? ?????????
                            goal_count();
                            //????????? db??? ????????????
                            databaseReference.child(uid).child("dialog_personal").child(key).child("date").setValue(getTime());
                            ReadPersonalDate();


                        }).addOnFailureListener(Throwable::printStackTrace);

        });

        //s4??????
        s4.setOnClickListener(view -> {
                storageRef.child("goal_sticker/cat_pink.png").getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                            bsd.dismiss();

                            //????????? ??????????????? ?????????????????? ????????? ?????????
                            goal_count();
                            //????????? db??? ????????????
                            databaseReference.child(uid).child("dialog_personal").child(key).child("date").setValue(getTime());
                            ReadPersonalDate();


                        }).addOnFailureListener(Throwable::printStackTrace);

        });

        //s5??????
        s5.setOnClickListener(view -> {
                storageRef.child("goal_sticker/check_green.png").getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                            bsd.dismiss();

                            //????????? ??????????????? ?????????????????? ????????? ?????????
                            goal_count();
                            //????????? db??? ????????????
                            databaseReference.child(uid).child("dialog_personal").child(key).child("date").setValue(getTime());
                            ReadPersonalDate();


                        }).addOnFailureListener(Throwable::printStackTrace);

        });

        //s6??????
        s6.setOnClickListener(view -> {
                storageRef.child("goal_sticker/check_1.png").getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                            bsd.dismiss();

                            //????????? ??????????????? ?????????????????? ????????? ?????????
                            goal_count();
                            //????????? db??? ????????????
                            databaseReference.child(uid).child("dialog_personal").child(key).child("date").setValue(getTime());
                            ReadPersonalDate();


                        }).addOnFailureListener(Throwable::printStackTrace);

        });

        //s7??????
        s7.setOnClickListener(view -> {
            storageRef.child("goal_sticker/flower_red.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //????????? ??????????????? ?????????????????? ????????? ?????????
                        goal_count();

                        //????????? db??? ????????????
                        databaseReference.child(uid).child("dialog_personal").child(key).child("date").setValue(getTime());
                        ReadPersonalDate();

                    }).addOnFailureListener(Throwable::printStackTrace);
        });

        //s8??????
        s8.setOnClickListener(view -> {
            storageRef.child("goal_sticker/flower_1.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //????????? ??????????????? ?????????????????? ????????? ?????????
                        goal_count();

                        //????????? db??? ????????????
                        databaseReference.child(uid).child("dialog_personal").child(key).child("date").setValue(getTime());
                        ReadPersonalDate();

                    }).addOnFailureListener(Throwable::printStackTrace);
        });

        //s9??????
        s9.setOnClickListener(view -> {
            storageRef.child("goal_sticker/moon_1.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //????????? ??????????????? ?????????????????? ????????? ?????????
                        goal_count();

                        //????????? db??? ????????????
                        databaseReference.child(uid).child("dialog_personal").child(key).child("date").setValue(getTime());
                        ReadPersonalDate();

                    }).addOnFailureListener(Throwable::printStackTrace);
        });

        //s10??????
        s10.setOnClickListener(view -> {
            storageRef.child("goal_sticker/moon_3.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //????????? ??????????????? ?????????????????? ????????? ?????????
                        goal_count();

                        //????????? db??? ????????????
                        databaseReference.child(uid).child("dialog_personal").child(key).child("date").setValue(getTime());
                        ReadPersonalDate();

                    }).addOnFailureListener(Throwable::printStackTrace);
        });

        //s11??????
        s11.setOnClickListener(view -> {
            storageRef.child("goal_sticker/moon_4.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //????????? ??????????????? ?????????????????? ????????? ?????????
                        goal_count();

                        //????????? db??? ????????????
                        databaseReference.child(uid).child("dialog_personal").child(key).child("date").setValue(getTime());
                        ReadPersonalDate();

                    }).addOnFailureListener(Throwable::printStackTrace);
        });

        //s12??????
        s12.setOnClickListener(view -> {
            storageRef.child("goal_sticker/moon_full.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //????????? ??????????????? ?????????????????? ????????? ?????????
                        goal_count();

                        //????????? db??? ????????????
                        databaseReference.child(uid).child("dialog_personal").child(key).child("date").setValue(getTime());
                        ReadPersonalDate();


                    }).addOnFailureListener(Throwable::printStackTrace);
        });

        //s13??????
        s13.setOnClickListener(view -> {
            storageRef.child("goal_sticker/sprout_green.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //????????? ??????????????? ?????????????????? ????????? ?????????
                        goal_count();
                    }).addOnFailureListener(Throwable::printStackTrace);
        });

        //s14??????
        s14.setOnClickListener(view -> {
            storageRef.child("goal_sticker/sprout_green_2.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //????????? ??????????????? ?????????????????? ????????? ?????????
                        goal_count();

                        //????????? db??? ????????????
                        databaseReference.child(uid).child("dialog_personal").child(key).child("date").setValue(getTime());
                        ReadPersonalDate();

                    }).addOnFailureListener(Throwable::printStackTrace);
        });

        //s15??????
        s15.setOnClickListener(view -> {
            storageRef.child("goal_sticker/sprout_grow_1.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //????????? ??????????????? ?????????????????? ????????? ?????????
                        goal_count();

                        //????????? db??? ????????????
                        databaseReference.child(uid).child("dialog_personal").child(key).child("date").setValue(getTime());
                        ReadPersonalDate();

                    }).addOnFailureListener(Throwable::printStackTrace);
        });

        //s16??????
        s16.setOnClickListener(view -> {
            storageRef.child("goal_sticker/sprout_grow_2.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //????????? ??????????????? ?????????????????? ????????? ?????????
                        goal_count();

                        //????????? db??? ????????????
                        databaseReference.child(uid).child("dialog_personal").child(key).child("date").setValue(getTime());
                        ReadPersonalDate();


                    }).addOnFailureListener(Throwable::printStackTrace);
        });


        //0??????????????? ??????
        //ReadPersonalDialog();
       // gridView.setAdapter(adapter);

    }



    private GridItem addGoal(int i) {
        // Handle any errors
        storageRef.child("not.png").getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    // Got the download URL for 'plus.png'
                    gd = new GridItem(String.valueOf(i), uri.toString());
                    ds.child(String.valueOf(i)).setValue(gd);

                }).addOnFailureListener(Throwable::printStackTrace);

        return gd;
    }


    //??????????????? ????????? ?????? ????????????
    private void ReadPersonalDialog2(int i) {

        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
               // sticker_img.setImageResource(0);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GridItem gridItem = dataSnapshot.getValue(GridItem.class);

                    //test
                    assert gridItem != null;
                    gridItem.setGoal_id(String.valueOf(i));

                    items.add(gridItem);
                    Log.d("TAG", String.valueOf(items));

                }
                adapter.notifyDataSetChanged();
                //gridView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(custom_p_goal_click.this, "???????????? ??????", Toast.LENGTH_SHORT).show();
            }
        };
        ds.addValueEventListener(postListener);
    }

    //?????????????????? ?????? ?????????
    private void goal_count(){
        databaseReference.child(uid).child("dialog_personal").child(key).child("pGoal").setValue(++p);
        ReadPersonalDialog();
    }

    //??????????????? ????????? ?????? ????????????
    private String ReadPersonalDate(){
        date = null;
        databaseReference.child(uid).child("dialog_personal").child(key).child("date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                date = snapshot.getValue(String.class);
                Log.d("TAG", date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return date;
    }

    //??????????????? ????????? ?????? ????????????
    private int ReadPersonalDialog() {
        databaseReference.child(uid).child("dialog_personal").child(key).child("pGoal").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               p = snapshot.getValue(Integer.class);
               Log.d("TAG", String.valueOf(p));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                //Toast.makeText(MainActivity.this, "???????????? ??????", Toast.LENGTH_SHORT).show();
            }
        });

        return p;

    }




}