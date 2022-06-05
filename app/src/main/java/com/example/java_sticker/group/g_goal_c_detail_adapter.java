package com.example.java_sticker.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.java_sticker.CustomProgress;
import com.example.java_sticker.Fragment.DetailFragment;
import com.example.java_sticker.Group_main;
import com.example.java_sticker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class g_goal_c_detail_adapter extends RecyclerView.Adapter<g_goal_c_detail_adapter.ViewHolder> {
    Context context;
    ArrayList<GroupDialog> items;
    Intent intent;
    Intent intent_close;
    //int item_layout;

    //그룹방 이미지 불러오기 위한 장치
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference profile_databaseReference = firebaseDatabase.getReference();

    String uid = user.getUid();

    public g_goal_c_detail_adapter(Context context,ArrayList<GroupDialog> items){
        this.context = context;
        this.items = items;


    }




    @NonNull
    @Override
    public g_goal_c_detail_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_g_goal,parent,false);
        return new g_goal_c_detail_adapter.ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull g_goal_c_detail_adapter.ViewHolder holder, int position) {

        //파이어베이스
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();

        final GroupDialog item = items.get(position);

        //그룹방 이미지(일단 프사로 설정)
        profile_databaseReference.child("user").child(uid).child("profileImageUrl").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uri = snapshot.getValue(String.class);
                Glide.with(context)
                        .load(uri)
                        .into(holder.g_img);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.g_goal_tittle.setText(item.getName());
        holder.g_goal_progressBar.setMaxValue(item.gCount);
        // Log.d("TAG", String.valueOf(item.gCount));
        holder.g_goal_progressBar.setCurValue(item.gGoal);

        holder.cardView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();

            bundle.putString("tittle",item.getgTittle()); //목표제목
            bundle.putInt("count", item.getgCount());//총 도장수
            bundle.putInt("goal_count", item.getgGoal());//찍은 도장수
            bundle.putString("auth",item.getAuth());//인증방식
            bundle.putString("key", item.getKey()); //리사이클러뷰 고유키
            bundle.putString("w_uid", item.getW_uid()); //작성자 uid
            bundle.putString("uid_auth",item.getUid_auth()); //카드뷰 클릭한 사람의 uid

            Log.d("auth","" +item.getUid_auth());


            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Fragment custom_g_goal_click = new custom_g_goal_click();
            custom_g_goal_click.setArguments(bundle);
            FragmentManager fragmentManager = ((custom_g_goal_click_main)view.getContext()).getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.group_goal_click_layout,custom_g_goal_click).addToBackStack(null).commit();


        });

    }

    @Override
    public int getItemCount() {
        return (items != null ? items.size() : 0);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        //ProgressBar p_goal_progressBar;
        ImageView g_img;
        CustomProgress g_goal_progressBar;
        TextView g_goal_tittle;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            g_img=itemView.findViewById(R.id.g_pic);
            g_goal_progressBar = (CustomProgress) itemView.findViewById(R.id.customProgress_g_goal_c);
            g_goal_tittle = (TextView) itemView.findViewById(R.id.custom_g_goal_tittle);
            cardView = (CardView) itemView.findViewById(R.id.custom_g_goal_cardView);


        }

    }
}
