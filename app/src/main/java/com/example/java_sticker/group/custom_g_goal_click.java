package com.example.java_sticker.group;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.load.model.Model;
import com.example.java_sticker.Group_main;
import com.example.java_sticker.R;
import com.example.java_sticker.personal.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

/*그룹도장판*/

public class custom_g_goal_click extends Fragment {


    //noti
    String noti_name;
    private static String CHANNEL_ID = "channel1";
    private static String CHANEL_NAME = "Channel1";


    private static final String TAG = "MainActivity";
    private static final int NOTIFICATION_REQUEST_CODE = 1234;

    private TextView header_goal;
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
    String w_uid;
    String uid_auth;
    int p;

    //파이어베이스
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

    //공유
    View container;

    ActionBarDrawerToggle barDrawerToggle;


    //카메라 촬영
    private Bitmap mImageUri = null;
    private static final int GALLERY_REQUEST = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private StorageReference mStorage;
    ImageView img;
    TextView ok;
    Uri imageUri;
    String imageurl;
    Bitmap thumbnail;

    ContentValues values = new ContentValues();
    String[] permission_list = {Manifest.permission.WRITE_CONTACTS};
    private Uri filePath;
    private Bitmap bitmap;

    ImageView s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15, s16;

    @RequiresApi(api = 33)
    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        view = inflater.inflate(R.layout.activity_custom_ggoal_click, container, false);

        // [START handle_data_extras]
        if (requireActivity().getIntent().getExtras() != null) {
            for (String key : requireActivity().getIntent().getExtras().keySet()) {
                Object value = requireActivity().getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        // [END handle_data_extras]


        //권한허가
        checkPermission();
        //toolbar
        toolbar = view.findViewById(R.id.goal_toolbar);
        toolbar.inflateMenu(R.menu.goal_menu);




        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });



        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.share) {
                    //현재 화면 캡처 저장
                    builder.setTitle("공유").setMessage("해당 도장판을 저장하시겠습니까?")
                            .setPositiveButton("저장하기", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //공유
//                                    view.buildDrawingCache();
//                                    Bitmap caputer = view.getDrawingCache();
//                                    FileOutputStream fos;
//
//                                    try{
//                                        fos = new FileOutputStream(Environment.getExternalStorageDirectory().toString()+"/goal.jpeg");
//                                        caputer.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                                        Toast.makeText(view.getContext(), "저장됐습니다", Toast.LENGTH_SHORT).show();
//                                    }catch (FileNotFoundException e){
//                                        e.printStackTrace();
//                                    }

                                       View rootView = getView();
                                       File screenShot = ScreenShot(rootView);
                                       if(screenShot !=null){
                                           getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.fromFile(screenShot)));
                                           Toast.makeText(getContext(), "저장했습니다.", Toast.LENGTH_SHORT).show();
                                       }



                                }
                            }).setNeutralButton("취소", null)
                            .show();
                }

                return true;
            }
        });


        setHasOptionsMenu(true);


        // Create a storage reference from our app
        sticker_img = view.findViewById(R.id.sticker_img);
        items = new ArrayList<>();
        adapter = new Custom_gAdapter(getContext(), items);
        gridView = (GridViewWithHeaderAndFooter) view.findViewById(R.id.g_gridView);

        //파이어베이스
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();

        GetBundle();
        View header = getLayoutInflater().inflate(R.layout.header, null, false);
        header_goal = (TextView) header.findViewById(R.id.header_goal);
        gridView.addHeaderView(header);
        header_goal.setText(tittle);


        //bottom sheet
        v = getLayoutInflater().inflate(R.layout.g_bottom_sheet, null);
        bsd = new BottomSheetDialog(getActivity());
        bsd.setContentView(v);


        View cv = getLayoutInflater().inflate(R.layout.ggoal_sticker_img, null);
        //img
        img = cv.findViewById(R.id.img);
        ok = cv.findViewById(R.id.ok);

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


        camera = v.findViewById(R.id.camera);
        gallery = v.findViewById(R.id.gallery);


//        //그리드뷰 각 칸 클릭시, 데이터 수정
        gridView.setOnItemClickListener((adapterView, view, i, l) -> {
            if (uid_auth.equals(uid)) {
                Log.d("TAG", String.valueOf(i));
                stickerClick(i);
            } else {
                Toast.makeText(getContext(), "본인 도장판만 스티커를 찍을 수 있습니다", Toast.LENGTH_SHORT).show();
            }


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
        return view;
    }

    public File ScreenShot(View view){
        view.setDrawingCacheEnabled(true);

        Bitmap screenBitmap = view.getDrawingCache();

        String filename = "goal.png";
        File file = new File(Environment.getExternalStorageDirectory()+"/Pictures", filename);
        FileOutputStream os = null;
        try{
            os = new FileOutputStream(file);
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 90, os);
            os.close();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }

        view.setDrawingCacheEnabled(false);
        return file;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        switch (item.getItemId()){
//            case R.id.share:
//                Toast.makeText(getContext(), "쉐어버튼 ", Toast.LENGTH_SHORT).show();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }


    //사진찍기 권한 확인인
    @SuppressLint("ObsoleteSdkInt")
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(getContext(), "외부 저장소 사용을 위해 읽기/쓰기 필요", Toast.LENGTH_SHORT).show();
                }

                requestPermissions(new String[]
                        {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0) {
            for (int grantResult : grantResults) {
                //허용됬다면
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "앱권한 설정완료", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "앱권한설정하세요", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }
        }

        // [START handle_ask_post_notifications_request]
        switch (requestCode) {
            case NOTIFICATION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
        }

    }


    private String getRealPathFromURI(Uri imageUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(imageUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }

    private void uploadToFirebase(String mImageUri) {
        Object i = values.get("order");

        StorageReference fileRef = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));

        fileRef.putFile(Uri.parse(mImageUri)).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri_ -> {


            ds.child(String.valueOf(i)).child("test").setValue(uri_.toString());

            //도장을 클릭했다면 프로그래스바 숫자를 늘린다
            goal_count();
            bsd.dismiss();


            //프로그래스바 숨김
            //progressBar.setVisibility(View.INVISIBLE);

            Toast.makeText(getContext(), "업로드 성공", Toast.LENGTH_SHORT).show();
            showNoti();
        })).addOnFailureListener(Throwable::printStackTrace);
        Log.d("camera", "13");

    }

    //파일타입 가져오기
    // url = file path or whatever suitable URL you want.
    private static String getFileExtension(String uri) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(uri);

        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;

    }


    @RequiresApi(33)
    // [START ask_post_notifications]
    private void askNotificationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED) {
            // FCM SDK (and your app) can post notifications.
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            // TODO: display an educational UI explaining to the user the features that will be enabled
            //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
            //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
            //       If the user selects "No thanks," allow the user to continue without notifications.
        } else {
            // Directly ask for the permission
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_REQUEST_CODE);
        }
    }
    // [END ask_post_notifications]


    @RequiresApi(api = 33)
    private void stickerClick(int i) {
        //bottom sheet dialog 보이기기
        bsd.show();
        //height 만큼 보이게 됨
        bsd.getBehavior().setState(STATE_COLLAPSED);


        //s1클릭
        s1.setOnClickListener(view -> {
            storageRef.child("goal_sticker/cat_green.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //도장을 클릭했다면 프로그래스바 숫자를 늘린다
                        goal_count();

                        //push Noti
                        showNoti();

                    }).addOnFailureListener(Throwable::printStackTrace);
        });

        //s2클릭
        s2.setOnClickListener(view -> {
            storageRef.child("goal_sticker/cat_black.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //도장을 클릭했다면 프로그래스바 숫자를 늘린다
                        goal_count();
                        //push Noti
                        showNoti();
                    }).addOnFailureListener(Throwable::printStackTrace);
        });

        //s3클릭
        s3.setOnClickListener(view -> {
            storageRef.child("goal_sticker/cat_grap.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //도장을 클릭했다면 프로그래스바 숫자를 늘린다
                        goal_count();
                        //push Noti
                        showNoti();
                    }).addOnFailureListener(Throwable::printStackTrace);
        });

        //s4클릭
        s4.setOnClickListener(view -> {
            storageRef.child("goal_sticker/cat_pink.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //도장을 클릭했다면 프로그래스바 숫자를 늘린다
                        goal_count();
                        //push Noti
                        showNoti();
                    }).addOnFailureListener(Throwable::printStackTrace);
        });

        //s5클릭
        s5.setOnClickListener(view -> {
            storageRef.child("goal_sticker/check_green.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //도장을 클릭했다면 프로그래스바 숫자를 늘린다
                        goal_count();
                        //push Noti
                        showNoti();
                    }).addOnFailureListener(Throwable::printStackTrace);
        });

        //s6클릭
        s6.setOnClickListener(view -> {
            storageRef.child("goal_sticker/check_1.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //도장을 클릭했다면 프로그래스바 숫자를 늘린다
                        goal_count();
                        //push Noti
                        showNoti();
                    }).addOnFailureListener(Throwable::printStackTrace);
        });

        //s7클릭
        s7.setOnClickListener(view -> {
            storageRef.child("goal_sticker/flower_red.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //도장을 클릭했다면 프로그래스바 숫자를 늘린다
                        goal_count();
                        //push Noti
                        showNoti();
                    }).addOnFailureListener(Throwable::printStackTrace);
        });

        //s8클릭
        s8.setOnClickListener(view -> {
            storageRef.child("goal_sticker/flower_1.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //도장을 클릭했다면 프로그래스바 숫자를 늘린다
                        goal_count();
                        //push Noti
                        showNoti();
                    }).addOnFailureListener(Throwable::printStackTrace);
        });

        //s9클릭
        s9.setOnClickListener(view -> {
            storageRef.child("goal_sticker/moon_1.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //도장을 클릭했다면 프로그래스바 숫자를 늘린다
                        goal_count();
                        //push Noti
                        showNoti();
                    }).addOnFailureListener(Throwable::printStackTrace);
        });

        //s1클릭
        s10.setOnClickListener(view -> {
            storageRef.child("goal_sticker/moon_3.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //도장을 클릭했다면 프로그래스바 숫자를 늘린다
                        goal_count();
                        //push Noti
                        showNoti();
                    }).addOnFailureListener(Throwable::printStackTrace);
        });

        //s11클릭
        s11.setOnClickListener(view -> {
            storageRef.child("goal_sticker/moon_4.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //도장을 클릭했다면 프로그래스바 숫자를 늘린다
                        goal_count();
                        //push Noti
                        showNoti();
                    }).addOnFailureListener(Throwable::printStackTrace);
        });

        //s12클릭
        s12.setOnClickListener(view -> {
            storageRef.child("goal_sticker/moon_full.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //도장을 클릭했다면 프로그래스바 숫자를 늘린다
                        goal_count();
                        //push Noti
                        showNoti();
                    }).addOnFailureListener(Throwable::printStackTrace);
        });

        //s13클릭
        s13.setOnClickListener(view -> {
            storageRef.child("goal_sticker/sprout_green.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //도장을 클릭했다면 프로그래스바 숫자를 늘린다
                        goal_count();
                        //push Noti
                        showNoti();
                    }).addOnFailureListener(Throwable::printStackTrace);
        });

        //s14클릭
        s14.setOnClickListener(view -> {
            storageRef.child("goal_sticker/sprout_green_2.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //도장을 클릭했다면 프로그래스바 숫자를 늘린다
                        goal_count();
                        //push Noti
                        showNoti();
                    }).addOnFailureListener(Throwable::printStackTrace);
        });

        //s15클릭
        s15.setOnClickListener(view -> {
            storageRef.child("goal_sticker/sprout_grow_1.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //도장을 클릭했다면 프로그래스바 숫자를 늘린다
                        goal_count();
                        //push Noti
                        showNoti();
                    }).addOnFailureListener(Throwable::printStackTrace);
        });

        //s16클릭
        s16.setOnClickListener(view -> {
            storageRef.child("goal_sticker/sprout_grow_2.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                        bsd.dismiss();

                        //도장을 클릭했다면 프로그래스바 숫자를 늘린다
                        goal_count();
                        //push Noti
                        showNoti();
                    }).addOnFailureListener(Throwable::printStackTrace);
        });

        //카메라 클릭
        camera.setOnClickListener(view -> {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
            values.put("order", i);


            if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }

            Toast.makeText(getContext(), "카메라 클릭", Toast.LENGTH_SHORT).show();
            //push Noti
           // showNoti();
        });

        //갤러리 클릭
        gallery.setOnClickListener(view -> {
            Toast.makeText(getContext(), "이미지 클릭", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            String[] mimeTypes = {"image/jpeg", "image/png"};
            values.put("order", i);
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            startActivityForResult(intent, GALLERY_REQUEST);
            //push Noti
           // showNoti();


        });


    }

    private void showNoti() {
//        builder = null;
//        manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
//        //버전 오레오 이상일 경우
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            manager.createNotificationChannel(
//                    new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
//            );
//
//            builder = new NotificationCompat.Builder(getContext(),CHANNEL_ID);
//
//            //하위 버전일 경우
//        }else{
//            builder = new NotificationCompat.Builder(getContext());
//        }
//
//
//        Intent intent = new Intent(getContext(), Group_main.class);
////        intent.putExtra("name",name);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 101,
//                intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        //알림창 제목
//        builder.setContentTitle("BetterMe");
//
//        //알림창 메시지
//        builder.setContentText("user 님이 스티커를 찍었습니다");
//
//        //알림창 아이콘
//        builder.setSmallIcon(R.mipmap.ic_main_round);
//
//        //알림창 터치시 상단 알림상태창에서 알림이 자동으로 삭제되게 합니다.
//        builder.setAutoCancel(true);
//
//
//        //pendingIntent를 builder에 설정 해줍니다.
//        //알림창 터치시 인텐트가 전달할 수 있도록 해줍니다.
//        builder.setContentIntent(pendingIntent);
//
//        Notification notification = builder.build();
//
//        //알림창 실행
//        manager.notify(1,notification);
//

        DatabaseReference name = databaseReference.child(uid_auth).child("dialog_group").child(key).child("name");

        //스티커 찍은 사람 이름을 가져오고 싶은데 null만 리턴됨...
        name.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("noti_name: ", snapshot.getValue(String.class));
                    noti_name = snapshot.getValue(String.class);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("noti_name: ", String.valueOf(databaseError.toException()));
            }
        });
        Log.d("noti_body:  ", String.valueOf(name));


        RequestQueue mRequestQue = Volley.newRequestQueue(getContext());
        JSONObject json = new JSONObject();


        try {
            json.put("to", "/topics/" + key);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", header_goal);
            notificationObj.put("body", noti_name + "이 스티커를 찍었습니다");
            //replace notification with data when went send data
            json.put("notification", notificationObj);

            String URL = "https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    response -> Log.d("MUR", "onResponse: "),
                    error -> Log.d("MUR", "onError: " + error.networkResponse)
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAAD23x9HI:APA91bEWmpr5qSuL6l0GM4WqBP_KFza55YM83iWoBl35YydoQgx_785SyMJevytOHS50hgP4ZBIRWbEgtH8da85QRdiPYsPNkTEr_viTOlAUZAAfwXBhdke0kqyrTkDmGacI40qxkwhm");
                    return header;
                }
            };


            mRequestQue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap photo;
        Log.d("camera", "4");
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            try {

                assert data != null;
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                uploadToFirebase(String.valueOf(getImageUri(requireContext(), imageBitmap)));

            } catch (Exception e) {
                Log.d("camera", "9");
                e.printStackTrace();
            }

        }
        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            Uri selectedImage = data.getData();
            uploadToFirebase(String.valueOf(selectedImage));

        }


    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    //클릭한 리사이클러뷰 아이템 값 가져오기 반영.
    private void GetBundle() {
        Bundle bundle = this.getArguments();
        count = bundle.getInt("count");
        tittle = bundle.getString("tittle");
        goal_count = bundle.getInt("goal_count");
        uid_auth = bundle.getString("uid_auth");
        key = bundle.getString("key");
        w_uid = bundle.getString("w_uid");
        Log.d("getbundle", count + "\n" + tittle + "\n" + goal_count + "\n" + uid_auth + "\n" + key + "\n" + w_uid);

    }


    //도장판 함수 가져오기!
    private ArrayList<g_GridItem> ReadGoal(int i) {
        ds.addValueEventListener(new ValueEventListener() {
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
                // gridView.setAdapter(adapter);
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
        databaseReference
                .child(uid_auth)
                .child("dialog_group")
                .child(key)
                .child("gGoal")
                .addValueEventListener(new ValueEventListener() {
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.goal_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }


    //클릭한 리사이클러뷰 아이템의 참가한 유저의 uid를 가져오는 함수
//    private void ReadUidKeyDialog() {
//        uid_key.clear();
//        uid_key_ds.addListenerForSingleValueEvent(new ValueEventListener() {
//            //@SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.child("uid").getChildren()) {
//                    uid_key.add(dataSnapshot.getValue(String.class));
//                    //Log.d("TAG", String.valueOf(uid_key));
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getContext(), "불러오기 실패", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
}
