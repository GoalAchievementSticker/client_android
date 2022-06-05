package com.example.java_sticker.group;

import static android.app.Activity.RESULT_OK;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.load.model.Model;
import com.example.java_sticker.R;
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
import com.google.firebase.storage.UploadTask;

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
    String g_tittle;
    String key;
    String uid;
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
    StorageReference storageRef = storage.getReference(); //뽑아오는 스토리지
    private ValueEventListener postListener;


    Toolbar toolbar;
    ImageView camera, gallery;
    View v;
    BottomSheetDialog bsd;
    private View view;



    //카메라 촬영
    private Uri mImageUri = null;
    private static final int GALLERY_REQUEST = 1;
    private static final int CAMERA_REQUEST_CODE = 1;
    private StorageReference mStorage;
    ImageView img;
    TextView ok;

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
        v = getLayoutInflater().inflate(R.layout.g_bottom_sheet, null);
        bsd = new BottomSheetDialog(getActivity());
        bsd.setContentView(v);

        ReadUidKeyDialog();


        //img
        img = v.findViewById(R.id.img);
        ok = v.findViewById(R.id.ok);

        adapter.notifyDataSetChanged();

        camera = v.findViewById(R.id.camera);
        gallery = v.findViewById(R.id.gallery);


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
        }

        ok.setOnClickListener(view -> {
            uploadToFirebase(mImageUri);


        });


        return view;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            assert data != null;
            mImageUri = data.getData();
            img.setImageURI(mImageUri);
        }

        uploadToFirebase(mImageUri);


    }

    private void uploadToFirebase(Uri mImageUri) {
        StorageReference fileRef = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
        fileRef.putFile(mImageUri).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {

            //이미지 모델에 담기
            Model model = other -> false;

           // ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
            bsd.dismiss();

            //도장을 클릭했다면 프로그래스바 숫자를 늘린다
            goal_count();


            //프로그래스바 숨김
            //progressBar.setVisibility(View.INVISIBLE);

            Toast.makeText(getContext(), "업로드 성공", Toast.LENGTH_SHORT).show();

            //  imageView.setImageResource(R.drawable.ic_add_photo);
        })).addOnFailureListener(Throwable::printStackTrace);
    }

    //파일타입 가져오기
    private String getFileExtension(Uri uri) {

        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void onCaptureImageResult(Intent data) {


        mImageUri = data.getData();
        img.setImageURI(mImageUri);

    }

    private void stickerClick(int i) {
        //bottom sheet dialog 보이기기
        bsd.show();
        //height 만큼 보이게 됨
        bsd.getBehavior().setState(STATE_COLLAPSED);


        //카메라 클릭
        //카메라 접근 허용창
        //카메라 찎
        camera.setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra("order",i);

            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }

            Toast.makeText(getContext(), "카메라 클릭", Toast.LENGTH_SHORT).show();
//                storageRef.child("check.png").getDownloadUrl()
//                        .addOnSuccessListener(uri -> {
//                            // Got the download URL for 'plus.png'
////                        gd = new GridItem(String.valueOf(i), uri.toString());
//                            ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
//                            bsd.dismiss();
//
//                            //도장을 클릭했다면 프로그래스바 숫자를 늘린다
//                            goal_count();
//                        }).addOnFailureListener(Throwable::printStackTrace);
        });

        //갤러리 클릭
        gallery.setOnClickListener(view -> {
            Toast.makeText(getContext(), "이미지 클릭", Toast.LENGTH_SHORT).show();
            storageRef.child("sprout.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //도장을 클릭했다면 프로그래스바 숫자를 늘린다
                        goal_count();
                    }).addOnFailureListener(Throwable::printStackTrace);
            //도장을 클릭했다면 프로그래스바 숫자를 늘린다


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


    //클릭한 리사이클러뷰 아이템의 참가한 유저의 uid를 가져오는 함수
    private void ReadUidKeyDialog() {
        uid_key.clear();
        uid_key_ds.addListenerForSingleValueEvent(new ValueEventListener() {
            //@SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.child("uid").getChildren()) {
                    uid_key.add(dataSnapshot.getValue(String.class));
                    //Log.d("TAG", String.valueOf(uid_key));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
