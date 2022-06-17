package com.example.java_sticker.group;

import static android.app.Activity.RESULT_OK;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.load.model.Model;
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
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

/*그룹도장판*/
public class custom_g_goal_click extends Fragment {
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

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        view = inflater.inflate(R.layout.activity_custom_ggoal_click, container, false);

        //권한허가
        checkPermission();
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
            Log.d("camera", "12");
            //  imageView.setImageResource(R.drawable.ic_add_photo);
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
//            storageRef.child("sprout.png").getDownloadUrl()
//                    .addOnSuccessListener(uri -> {
//                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
//                        bsd.dismiss();
//
//                        //도장을 클릭했다면 프로그래스바 숫자를 늘린다
//                        goal_count();
//                    }).addOnFailureListener(Throwable::printStackTrace);
            //도장을 클릭했다면 프로그래스바 숫자를 늘린다


        });


    }

    /**
     * Bitmap이미지의 가로, 세로 사이즈를 리사이징 한다.
     *
     * @param source        원본 Bitmap 객체
     * @param maxResolution 제한 해상도
     * @return 리사이즈된 이미지 Bitmap 객체
     */
    public Bitmap resizeBitmapImage(Bitmap source, int maxResolution) {
        int width = source.getWidth();
        int height = source.getHeight();
        int newWidth = width;
        int newHeight = height;
        float rate = 0.0f;

        if (width > height) {
            if (maxResolution < width) {
                rate = maxResolution / (float) width;
                newHeight = (int) (height * rate);
                newWidth = maxResolution;
            }
        } else {
            if (maxResolution < height) {
                rate = maxResolution / (float) height;
                newWidth = (int) (width * rate);
                newHeight = maxResolution;
            }
        }

        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap photo;
        Log.d("camera", "4");
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            // filePath = data.getData();
            try {
                //compressImage(imageUri);

                assert data != null;
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");


                //  Bitmap resized = resizeBitmapImage(imageBitmap, 80);
//                getImageUri(requireContext(),imageBitmap);

//               bitmap = MediaStore.Images.Media.getBitmap(
//                        requireActivity().getContentResolver(), imageUri);


                uploadToFirebase(String.valueOf(getImageUri(requireContext(), imageBitmap)));

            } catch (Exception e) {
                Log.d("camera", "9");
                e.printStackTrace();
            }

//            assert data != null;
//            final Bundle extras = data.getExtras();
//            photo = extras.getParcelable("data");
//            img.setImageBitmap(photo);
//            uploadToFirebase(photo);


//            mImageUri = data.getData();
//            Log.d("uri", String.valueOf(mImageUri));
//            img.setImageURI(mImageUri);
        }
        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            Uri selectedImage = data.getData();
            uploadToFirebase(String.valueOf(selectedImage));
        }

//        uploadToFirebase(photo);


    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void compressImage(Uri filePath) {
        try {
            OutputStream outStream = null;

            Log.i("filePath", String.valueOf(filePath));
            outStream = new FileOutputStream(String.valueOf(filePath));
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 8;
            bitmap = BitmapFactory.decodeFile(String.valueOf(filePath), bmOptions);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);


            outStream.flush();
            outStream.close();


            Log.i("file path compress", String.valueOf(filePath));

        } catch (Exception e) {

            Log.i("exception", e.toString());
        }
    }


    private Bitmap resizeBitmap(Bitmap thumbnail, int i) {
        int width = thumbnail.getWidth();
        int height = thumbnail.getHeight();
        double x;

        if (width >= height && width > i) {
            x = width / height;
            width = i;
            height = (int) (i / x);
        } else if (height >= width && height > i) {
            x = height / width;
            height = i;
            width = (int) (i / x);
        }
        return Bitmap.createScaledBitmap(thumbnail, width, height, false);
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
