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
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.load.model.Model;
import com.example.java_sticker.Group_main;
import com.example.java_sticker.R;
import com.example.java_sticker.UserRegister;
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

/*???????????????*/

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

    //??????????????????
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("GroupDialog");
    DatabaseReference user_databaseReference = firebaseDatabase.getReference("user");
    private ImageView sticker_img;
    DatabaseReference ds;
    DatabaseReference uid_key_ds;


    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference(); //???????????? ????????????
    private ValueEventListener postListener;


    Toolbar toolbar;
    ImageView camera, gallery;
    View v;
    BottomSheetDialog bsd;
    private View view;

    //??????
    View container;

    ActionBarDrawerToggle barDrawerToggle;


    //????????? ??????
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


    ImageView s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15, s16;

    //???????????? ?????? ??????
    View screenView;
    Bitmap bitmap;
    static File file;
    static String dirPath;
    static boolean cachePreviousState = true;

    String myname;


    //??????
    Date mDate;
    long mNow;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");

    String date;


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


        //????????????
        checkPermission();
        //toolbar
        toolbar = view.findViewById(R.id.goal_toolbar);
        toolbar.inflateMenu(R.menu.goal_menu);


        toolbar.setNavigationOnClickListener(view -> getActivity().onBackPressed());


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.share) {
                //?????? ?????? ?????? ??????
                builder.setTitle("SNS ??????").setMessage("?????? ???????????? ?????????????????????????")
                        .setPositiveButton("????????????", (dialogInterface, i) -> {


                            //????????????
                            View rootView = getActivity().getWindow().getDecorView();

                            getScreenShot(rootView);
                            store(bitmap, "??? ?????????", screenView);
                            shareImage(file);


                        }).setNeutralButton("??????", null)
                        .show();
            }

            return true;
        });


        setHasOptionsMenu(true);


        // Create a storage reference from our app
        sticker_img = view.findViewById(R.id.sticker_img);
        items = new ArrayList<>();
        adapter = new Custom_gAdapter(getContext(), items);
        gridView = (GridViewWithHeaderAndFooter) view.findViewById(R.id.g_gridView);

        //??????????????????
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

        ReadPersonalDate();

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


//        //???????????? ??? ??? ?????????, ????????? ??????
        gridView.setOnItemClickListener((adapterView, view, i, l) -> {
            if (uid_auth.equals(uid)) {
                Log.d("TAG", String.valueOf(i));
                //??????????????? ????????? ?????? ????????? ?????????
                if(date.equals(getTime())){
                    //????????? ?????? ?????? ??????
                    Toast.makeText(getContext(), "????????? ????????? ??????????????????", Toast.LENGTH_SHORT).show();
                }else{
                    //???????????? ????????? ?????? db??? ????????? ?????? ????????? ???????????????
                    stickerClick(i);

                }
            } else {
                Toast.makeText(getContext(), "?????? ???????????? ???????????? ?????? ??? ????????????", Toast.LENGTH_SHORT).show();
            }


        });


        //0??????????????? ??????
        ReadPersonalDialog();
        gridView.setAdapter(adapter);


        //????????? ????????? ?????? ????????? ????????????
        ds = databaseReference.child(uid_auth).child("goal_group").child(key).child("?????????");
        //????????? ????????????!
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

    //?????? ?????????
    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);

        Log.d("TAG", "?????? ??????"+mFormat.format(mDate));
        return mFormat.format(mDate);
    }

    //?????? ????????? ????????? ????????? ???????????? ?????? ??????
    public void getScreenShot(View view) {
        screenView = view.getRootView();
        screenView.layout(0, 0, screenView.getMeasuredWidth(), screenView.getMeasuredHeight());
        cachePreviousState = screenView.isDrawingCacheEnabled();
        final int backgroundPreviousColor = screenView.getDrawingCacheBackgroundColor();
        screenView.setDrawingCacheEnabled(true);
        screenView.setDrawingCacheBackgroundColor(0xfffafafa);

        //??????????????? ???????????? ?????????
        screenView.buildDrawingCache();

        bitmap = Bitmap.createBitmap(screenView.getWidth(), screenView.getHeight(),
                Bitmap.Config.ARGB_8888);
        screenView.setDrawingCacheBackgroundColor(backgroundPreviousColor);

        Canvas canvas = new Canvas(bitmap);
        screenView.draw(canvas);

        //??????????????? Bitmap ???????????? ?????????
        // bitmap = view.getDrawingCache();


    }

    //???????????? SDC????????? ????????????
    public static void store(Bitmap bm, String fileName, View screenView) {
        dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.betterme/BetterMe" + "/Screenshot.png";
        Log.d("sns_dirpath: ", dirPath);


        File dir = new File(dirPath);


        Log.d("dirPath", dirPath);
        Log.d("dir", String.valueOf(dir));


        FileOutputStream fOut;
        try {
            //????????? ????????? ????????? ????????? ??????
            if (!dir.exists())
                dir.mkdirs(); //??????????????? ????????? ????????? ?????? ??????

            file = new File(dirPath, fileName);

            //???????????? ???????????? ?????? ?????????
            //FileOutputStream ??? ?????????
            fOut = new FileOutputStream(file);

            //????????? ????????? bitmap ??? ?????? ????????? ??????
            bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            screenView.setDrawingCacheEnabled(cachePreviousState);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shareImage(File file) {
        Uri uri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".provider", file);


        // ????????? ?????? uri ??????
        Uri stickerAssetUri = Uri.parse(String.valueOf(uri));
        String sourceApplication = "com.example.java_sticker";

        //?????? ?????? ????????? ????????????
        //share sheet ??????
        Intent intent = new Intent(Intent.ACTION_SEND);


        //????????? ?????? ??????: ????????? ??????
        intent.setType("image/*");

        intent.putExtra("interactive_asset_uri", stickerAssetUri);
        intent.putExtra("source_application", sourceApplication);

        intent.putExtra("top_background_color", "#33FF33");
        intent.putExtra("bottom_background_color", "#FF00FF");

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setAction(Intent.ACTION_SEND);


        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        //???????????? Uri ??? ????????? ?????? Intent ??? ?????????
        intent.putExtra(Intent.EXTRA_STREAM, stickerAssetUri);

//        // ???????????? ?????????????????? ?????? ????????? ????????? ???????????? ????????????
//        Activity activity = getActivity();
//        assert activity != null;
//        activity.grantUriPermission(
//                "com.instagram.android", stickerAssetUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        if (activity.getPackageManager().resolveActivity(intent, 0) != null) {
//            activity.startActivityForResult(intent, 0);
//        }

        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "No App Available", Toast.LENGTH_SHORT).show();
        }

    }


    //?????? ????????? ??????
    public static String getExternalFilePath(Context context) {
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/";
        return filePath;
    }

    public File ScreenShot(View view) {
        view.setDrawingCacheEnabled(true);

        Bitmap screenBitmap = view.getDrawingCache();

        String filename = "goal.png";
        File file = new File(Environment.getExternalStorageDirectory() + "/Pictures", filename);
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 90, os);
            os.close();
        } catch (IOException e) {
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
//                Toast.makeText(getContext(), "???????????? ", Toast.LENGTH_SHORT).show();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }


    //???????????? ?????? ?????????
    @SuppressLint("ObsoleteSdkInt")
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(getContext(), "?????? ????????? ????????? ?????? ??????/?????? ??????", Toast.LENGTH_SHORT).show();
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
                //???????????????
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "????????? ????????????", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "????????????????????????", Toast.LENGTH_SHORT).show();
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

            //????????? ??????????????? ?????????????????? ????????? ?????????
            goal_count();
            bsd.dismiss();


            //?????????????????? ??????
            //progressBar.setVisibility(View.INVISIBLE);

            Toast.makeText(getContext(), "????????? ??????", Toast.LENGTH_SHORT).show();
            showNoti();
        })).addOnFailureListener(Throwable::printStackTrace);
        Log.d("camera", "13");

    }

    //???????????? ????????????
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
                        databaseReference.child(uid_auth).child("dialog_group").child(key).child("date").setValue(getTime());
                        ReadPersonalDate();

                        //push Noti
                        showNoti();

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
                        databaseReference.child(uid_auth).child("dialog_group").child(key).child("date").setValue(getTime());
                        ReadPersonalDate();

                        //push Noti
                        showNoti();
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
                        databaseReference.child(uid_auth).child("dialog_group").child(key).child("date").setValue(getTime());
                        ReadPersonalDate();

                        //push Noti
                        showNoti();
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
                        databaseReference.child(uid_auth).child("dialog_group").child(key).child("date").setValue(getTime());
                        ReadPersonalDate();

                        //push Noti
                        showNoti();
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
                        databaseReference.child(uid_auth).child("dialog_group").child(key).child("date").setValue(getTime());
                        ReadPersonalDate();

                        //push Noti
                        showNoti();
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
                        databaseReference.child(uid_auth).child("dialog_group").child(key).child("date").setValue(getTime());
                        ReadPersonalDate();

                        //push Noti
                        showNoti();
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
                        databaseReference.child(uid_auth).child("dialog_group").child(key).child("date").setValue(getTime());
                        ReadPersonalDate();

                        //push Noti
                        showNoti();
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
                        databaseReference.child(uid_auth).child("dialog_group").child(key).child("date").setValue(getTime());
                        ReadPersonalDate();

                        //push Noti
                        showNoti();
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
                        databaseReference.child(uid_auth).child("dialog_group").child(key).child("date").setValue(getTime());
                        ReadPersonalDate();

                        //push Noti
                        showNoti();
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
                        databaseReference.child(uid_auth).child("dialog_group").child(key).child("date").setValue(getTime());
                        ReadPersonalDate();


                        //push Noti
                        showNoti();
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
                        databaseReference.child(uid_auth).child("dialog_group").child(key).child("date").setValue(getTime());
                        ReadPersonalDate();

                        //push Noti
                        showNoti();
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
                        databaseReference.child(uid_auth).child("dialog_group").child(key).child("date").setValue(getTime());
                        ReadPersonalDate();


                        //push Noti
                        showNoti();
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

                        //????????? db??? ????????????
                        databaseReference.child(uid_auth).child("dialog_group").child(key).child("date").setValue(getTime());
                        ReadPersonalDate();


                        //push Noti
                        showNoti();
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
                        databaseReference.child(uid_auth).child("dialog_group").child(key).child("date").setValue(getTime());
                        ReadPersonalDate();


                        //push Noti
                        showNoti();
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
                        databaseReference.child(uid_auth).child("dialog_group").child(key).child("date").setValue(getTime());
                        ReadPersonalDate();


                        //push Noti
                        showNoti();
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
                        databaseReference.child(uid_auth).child("dialog_group").child(key).child("date").setValue(getTime());
                        ReadPersonalDate();

                        //push Noti
                        showNoti();
                    }).addOnFailureListener(Throwable::printStackTrace);
        });

        //????????? ??????
        camera.setOnClickListener(view -> {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
            values.put("order", i);


            if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
                //????????? db??? ????????????
                databaseReference.child(uid_auth).child("dialog_group").child(key).child("date").setValue(getTime());
                ReadPersonalDate();
            }

            Toast.makeText(getContext(), "????????? ??????", Toast.LENGTH_SHORT).show();
            //push Noti
            // showNoti();
        });

        //????????? ??????
        gallery.setOnClickListener(view -> {
            Toast.makeText(getContext(), "????????? ??????", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            String[] mimeTypes = {"image/jpeg", "image/png"};
            values.put("order", i);
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            startActivityForResult(intent, GALLERY_REQUEST);

            //????????? db??? ????????????
            databaseReference.child(uid_auth).child("dialog_group").child(key).child("date").setValue(getTime());
            ReadPersonalDate();

            //push Noti
            // showNoti();


        });


    }

    private void showNoti() {
//        builder = null;
//        manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
//        //?????? ????????? ????????? ??????
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            manager.createNotificationChannel(
//                    new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
//            );
//
//            builder = new NotificationCompat.Builder(getContext(),CHANNEL_ID);
//
//            //?????? ????????? ??????
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
//        //????????? ??????
//        builder.setContentTitle("BetterMe");
//
//        //????????? ?????????
//        builder.setContentText("user ?????? ???????????? ???????????????");
//
//        //????????? ?????????
//        builder.setSmallIcon(R.mipmap.ic_main_round);
//
//        //????????? ????????? ?????? ????????????????????? ????????? ???????????? ???????????? ?????????.
//        builder.setAutoCancel(true);
//
//
//        //pendingIntent??? builder??? ?????? ????????????.
//        //????????? ????????? ???????????? ????????? ??? ????????? ????????????.
//        builder.setContentIntent(pendingIntent);
//
//        Notification notification = builder.build();
//
//        //????????? ??????
//        manager.notify(1,notification);
//

        //??? uid ????????? userName ???????????? ?????? get
        DatabaseReference name = user_databaseReference.child(uid);



        SharedPreferences prefs =this.getActivity().getSharedPreferences("noti",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        //????????? ?????? ?????? ????????? ???????????? ????????? null??? ?????????...
        name.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                        myname = dataSnapshot.child("userName").getValue(String.class);
                        Log.d("????????????: ", "" + myname);


                    Log.d("noti_name_put: ",""+ myname);
                    //?????? ??????
                    editor.putString("noti_name", myname);
                    editor.commit();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("noti_err", databaseError.getMessage());
            }
        });
//        Log.d("noti_body:  ", String.valueOf(name));






        try {
            //?????? ??????
            String noti_name= prefs.getString("noti_name", " ");

            RequestQueue mRequestQue = Volley.newRequestQueue(getContext());
            JSONObject json = new JSONObject();

            //?????? ?????? ??????

            String noti_title = prefs.getString("noti_title", " ");
            Log.d("Noti_title: ",""+noti_title);

            json.put("to", "/topics/" + key);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", noti_title);
            notificationObj.put("body", noti_name + " ?????? ???????????? ???????????????");
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
        } catch (Exception e) {
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

//        if (requestCode == 5) {
//            if (resultCode == Activity.RESULT_OK) {
//                Toast.makeText(getContext(), "????????? ?????? ??????", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(getContext(), "????????? ?????? ??????", Toast.LENGTH_SHORT).show();
//            }
//        }


    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    //????????? ?????????????????? ????????? ??? ???????????? ??????.
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


    //????????? ?????? ????????????!
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

                Toast.makeText(getContext(), "???????????? ??????", Toast.LENGTH_SHORT).show();
            }

        });

        //items??? ???????????? ??????????????? ???????????? ????????????!
        return items;
    }

    //?????????????????? ?????? ?????????
    private void goal_count() {
        databaseReference.child(uid_auth).child("dialog_group").child(key).child("gGoal").setValue(++p);
        ReadPersonalDialog();
    }

    //??????????????? ????????? ?????? ????????????
    private String ReadPersonalDate(){
        date = null;
        databaseReference.child(uid_auth).child("dialog_group").child(key).child("date").addValueEventListener(new ValueEventListener() {
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


    //????????? ?????????????????? ???????????? ????????? ????????? uid??? ???????????? ??????
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
//                Toast.makeText(getContext(), "???????????? ??????", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
}
