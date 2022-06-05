package com.example.java_sticker.group;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.java_sticker.CustomProgress;
import com.example.java_sticker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Custom_g_item_adapter extends RecyclerView.Adapter<Custom_g_item_adapter.ViewHolder> {
    Context context;
    ArrayList<GroupDialog> items;
    Intent intent;
    Intent intent_close;
    Intent intent2;
    //int item_layout;

    //그룹방 이미지 불러오기 위한 장치
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference profile_databaseReference = firebaseDatabase.getReference();

    String uid = user.getUid();


    public Custom_g_item_adapter(Context context,ArrayList<GroupDialog> items){
        this.context = context;
        this.items = items;

    }




    @NonNull
    @Override
    public Custom_g_item_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_g_goal,parent,false);
        return new Custom_g_item_adapter.ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull Custom_g_item_adapter.ViewHolder holder, int position) {

        //파이어베이스
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();

        GroupDialog item = items.get(position);

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
        //holder.g_img.setImageResource(R.drawable.ic_baseline_supervised_user_circle_24);
        holder.g_goal_tittle.setText(item.getgTittle());
        holder.g_goal_progressBar.setMaxValue(item.gCount);
       // Log.d("TAG", String.valueOf(item.gCount));
        holder.g_goal_progressBar.setCurValue(item.gGoal);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(item.isClose() == true) {

                    intent = new Intent(view.getContext(), custom_g_goal_click_main.class);
                    intent.putExtra("tittle", item.getgTittle()); //도장판 제목
                    intent.putExtra("key", item.getKey()); //리사이클러뷰 고유키
                    intent.putExtra("count", item.getgCount()); //도장판 총 도장갯수
                    intent.putExtra("limit", item.getLimit()); //도장판 인원 제한수
                    intent.putExtra("limit_count", item.getLimit_count()); //도장판 참가한 수
                    intent.putExtra("auth", item.getAuth()); //도장판 인증방식
                    intent.putExtra("cate", item.getCate()); //도장판 카테고리

                    view.getContext().startActivity(intent);
                }else if(item.getW_uid().equals(uid)){
                    intent_close = new Intent(view.getContext(), close_add_goal.class);
                    intent_close.putExtra("tittle",item.getgTittle()); //도장판 제목
                    intent_close.putExtra("key", item.getKey()); //리사이클러뷰 고유키
                    intent_close.putExtra("count", item.getgCount()); //도장판 총 도장갯수
                    intent_close.putExtra("limit", item.getLimit()); //도장판 인원 제한수
                    intent_close.putExtra("limit_count", item.getLimit_count()); //도장판 참가한 수
                    intent_close.putExtra("auth",item.getAuth()); //도장판 인증방식
                    intent_close.putExtra("cate",item.getCate()); //도장판 카테고리
                    intent_close.putExtra("w_uid", item.getW_uid()); //도장판 작성자
                    view.getContext().startActivity(intent_close);

                }else{
                    Toast.makeText(view.getContext(), "아직 인원이 모이지 않았습니다", Toast.LENGTH_SHORT).show();
                }

            }
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

