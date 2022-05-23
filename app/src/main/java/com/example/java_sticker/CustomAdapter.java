package com.example.java_sticker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class CustomAdapter extends BaseAdapter {


    Context context;
    ArrayList<GridItem> items = null;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    GridItem gi;
    ImageView sticker_img;
    LinearLayout ll;
    View select_v;
    GridViewWithHeaderAndFooter gridView;

    public CustomAdapter(Context context, ArrayList<GridItem> items) {
        this.context = context;
        this.items = items;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        GridItem gridItem = items.get(position);


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_grid, viewGroup, false);

        sticker_img = convertView.findViewById(R.id.sticker_img);
        sticker_img.setImageResource(R.drawable.plus);

//        select_v=inflater.inflate(R.layout.activity_custom_pgoal_click,viewGroup,false);
//        ll = select_v.findViewById(R.id.select_sticker);
//        gridView = select_v.findViewById(R.id.gridView);

        // Got the download URL for 'plus.png'

        if(gridItem!=null) {
            // Got the download URL for 'plus.png'
            Glide.with(context)
                    .load(gridItem.getTest())
                    .into(sticker_img);

        }else{
            Toast.makeText(context,"해당 셀이 null 입니다",Toast.LENGTH_LONG).show();
        }



        int pos = position;
        sticker_img.setOnClickListener(view->{});
//            ll.setVisibility(View.VISIBLE); //스티커 보이기
//            //그리드뷰 각 칸 클릭시, 데이터 수정
//
//            switch (.getId()) {
//                //check
//                case R.id.s1:
//
//                    Toast.makeText(context, "s1클릭", Toast.LENGTH_SHORT).show();
////                storageRef.child("check.png").getDownloadUrl()
////                        .addOnSuccessListener(uri -> {
////                            // Got the download URL for 'plus.png'
//////                        gd = new GridItem(String.valueOf(i), uri.toString());
////                            ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
////                            Log.d("TAG", uri.toString());
////
////                        }).addOnFailureListener(Throwable::printStackTrace);
//
//                    ll.setVisibility(View.GONE); //없애기
//                    break;
//                //sprout
//                case R.id.s2:
//                    Toast.makeText(context, "s2클릭", Toast.LENGTH_SHORT).show();
////                storageRef.child("sprout.png").getDownloadUrl()
////                        .addOnSuccessListener(uri -> {
////                            // Got the download URL for 'plus.png'
//////                        gd = new GridItem(String.valueOf(i), uri.toString());
////                            ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
////                            Log.d("TAG", uri.toString());
////
////                        }).addOnFailureListener(Throwable::printStackTrace);
//                    ll.setVisibility(View.GONE); //없애기
//                    break;
//                //star
//                case R.id.s3:
//                    Toast.makeText(context, "s3클릭", Toast.LENGTH_SHORT).show();
////                storageRef.child("y_star.png").getDownloadUrl()
////                        .addOnSuccessListener(uri -> {
////                            // Got the download URL for 'plus.png'
//////                        gd = new GridItem(String.valueOf(i), uri.toString());
////                            ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
////                            Log.d("TAG", uri.toString());
////
////                        }).addOnFailureListener(Throwable::printStackTrace);
//                    ll.setVisibility(View.GONE); //없애기
//                    break;
//                //triangular
//                case R.id.s4:
//                    Toast.makeText(context, "s4클릭", Toast.LENGTH_SHORT).show();
////                storageRef.child("triangular.png").getDownloadUrl()
////                        .addOnSuccessListener(uri -> {
////                            // Got the download URL for 'plus.png'
//////                        gd = new GridItem(String.valueOf(i), uri.toString());
////                            ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
////                            Log.d("TAG", uri.toString());
////
////                        }).addOnFailureListener(Throwable::printStackTrace);
//                    ll.setVisibility(View.GONE); //없애기
//                    break;
//                //new-moon
//                case R.id.s5:
//                    Toast.makeText(context, "s5클릭", Toast.LENGTH_SHORT).show();
////                storageRef.child("new-moon.png").getDownloadUrl()
////                        .addOnSuccessListener(uri -> {
////                            // Got the download URL for 'plus.png'
//////                        gd = new GridItem(String.valueOf(i), uri.toString());
////                            ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
////                            Log.d("TAG", uri.toString());
////
////                        }).addOnFailureListener(Throwable::printStackTrace);
//                    ll.setVisibility(View.GONE); //없애기
//                    break;
//            }


        View view = new View(context);
        view = (View) convertView;


        return convertView;
    }


}