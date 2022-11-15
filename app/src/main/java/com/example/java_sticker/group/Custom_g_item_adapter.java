package com.example.java_sticker.group;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
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
import java.util.List;


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
    DatabaseReference databaseReference = firebaseDatabase.getReference("GroupDialog");
    DatabaseReference categoryReference = firebaseDatabase.getReference("Category");

    String uid = user.getUid();

    Boolean goal_exists = false;

    List<String> uid_key;

    int uid_key_number = 0;


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
        holder.cardView.setOnClickListener(view -> {

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

            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){

                        DatabaseReference ref = databaseReference.child(uid).child("dialog_group").child(items.get(position).getKey());
                        DatabaseReference ds = databaseReference.child(uid).child("goal_group").child(items.get(position).getKey()).child("도장판");
                        DatabaseReference cf = categoryReference.child(items.get(position).getCate()).child(items.get(position).getKey());
                        uid_key = new ArrayList<>();



                        try {
                            ReadUidDialog(items.get(position).getCate(),items.get(position).getKey());

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //도장판이 생성이 됐다는 가정
                                if(check_goal(position,ds) == true){
                                    //삭제 x
                                    builder.setTitle("Message").setMessage(items.get(position).getgTittle() + "는 아직 완료하지 못했습니다")
                                            .setNeutralButton("확인", null).show();
                                    //but, 도장수가 꽉찼다는 가정하에 삭제 가능
                                    if(items.get(position).getgCount() == items.get(position).getgGoal()){
                                        builder.setTitle("삭제하기").setMessage(items.get(position).getgTittle()+"를 정말 삭제 하시겠습니까?")
                                                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        //리사이클러뷰 db, 도장판 삭제
                                                        ref.removeValue();
                                                        ds.removeValue();
                                                        items.remove(position);
                                                        //참여자 uid 삭제

                                                        notifyDataSetChanged();
                                                    }
                                                }).setNeutralButton("취소", null)
                                                .show();
                                    }
                                }else{
                                    //도장판이 생성이 안됐다는 가정
                                    //최조 작성자라면?
                                    if(items.get(position).getW_uid().equals(uid)){
                                        builder.setTitle("삭제하기").setMessage(items.get(position).getgTittle() + "을(를) 정말로 삭제하겠습니까?")
                                                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        //최조 작성자 -> 본인 리사이클러뷰 아이템 삭제 + 카테고리 삭제 + 참여자가 있다면 나머지 삭제
                                                        //카테고리 삭제
                                                        if(uid_key.size() == 1){
                                                            //참여인원이 본인 뿐이라면
                                                            ref.removeValue();
                                                            ds.removeValue();
                                                            cf.removeValue();
                                                            items.remove(position);
                                                        }else{
                                                            ref.removeValue();
                                                            ds.removeValue();
                                                            cf.removeValue();
                                                            items.remove(position);
                                                            //아닐 경우 참여자 리사이클러뷰까지 삭제
                                                            for(int j = 0; j<uid_key.size(); j++){
                                                                DatabaseReference uid_info = databaseReference.child(uid_key.get(j)).child("dialog_group").child(items.get(position).getKey());
                                                                uid_info.removeValue();
                                                            }
                                                        }
                                                        notifyDataSetChanged();

                                                    }
                                                }).setNeutralButton("취소", null)
                                                .show();
                                    }else{
                                        builder.setTitle("삭제하기").setMessage(items.get(position).getgTittle() + "을(를) 정말로 삭제하겠습니까?")
                                                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        //참여자라면?
                                                        uid_key.remove(uid);
                                                        //참여자 -> 참여자 리사이클러뷰 아이템 삭제 + 카테고리 참가 user id 삭제, 카운트 감소
                                                        //카테고리 참가자 user id 삭제
                                                        //참가자 id삭제하려면 키값을 알아야한다
                                                        //키값 변경 1~ n 숫자로 변경완료
                                                        //uidkey를 돌리면서 삭제하려는 작성자의 uid가 몇번째 순서인지 알아서 그걸 child 즉, 키값으로 던져준다
//                                                        for(int u = 0; u<uid_key.size(); u++){
//                                                            uid_key_number++;
//                                                            if(uid_key.get(u).equals(uid)){
//                                                                break;
//                                                            }
//                                                            Log.d("TAG", String.valueOf(uid_key_number));
//                                                        }
//                                                        DatabaseReference cf_uid = cf.child("uid");
//                                                        Log.d("TAG", "삭제"+String.valueOf(uid_key_number));
//                                                        cf_uid.child(String.valueOf(uid_key_number+1)).removeValue();
                                                        //But 문제점... 임의로 강제로 번호를 줬기때문에.. 중간에 2번이라는 숫자가 사라지면 1번 3번부터 시작한다면
                                                        //3번이란 친구가 또 삭제를 할때 문제가 생긴다... 그외에도 추가할때 3번까지 참여를했고 2번이 삭제를 한다면
                                                        //새로 추가한 사람은 4번으로 가던가 2번으로 가야하는데 참여한사람이 2번이기에 중복 3번키값이 생긴다..ㅋ

                                                        //uid 키 삭제하는 방법 2번
                                                        //uid를 추가할때 키값을 uid자체를 준다
                                                        //uid자체를 준걸로 키값을 찾아서 그대로 삭제!!!!
                                                        DatabaseReference cf_uid = cf.child("uid");
                                                        cf_uid.child(uid).removeValue();
                                                        //카운트 감소
                                                        DatabaseReference cf_count = cf.child("limit_count");
                                                        cf_count.setValue(items.get(position).getLimit_count()-1);
                                                        //참여자가 더 있다는 경우하에 해당 리사이클러뷰 참여 id삭제, 카운트 감소
                                                        for(int t =0; t<uid_key.size(); t++){
                                                            //id삭제
                                                            Log.d("TAG", "삭제_참여자"+String.valueOf(uid_key_number));
                                                            DatabaseReference df_uid = databaseReference.child(uid_key.get(t)).child("dialog_group").child(items.get(position).getKey()).child("uid");
                                                            df_uid.child(uid).removeValue();
                                                            //카운트 감소
                                                            DatabaseReference df_uid_count = databaseReference.child(uid_key.get(t)).child("dialog_group").child(items.get(position).getKey()).child("limit_count");
                                                            df_uid_count.setValue(items.get(position).getLimit_count()-1);

                                                        }
                                                        //참여자 리사이크러뷰 아이템 삭제
                                                        ref.removeValue();
                                                        notifyDataSetChanged();

                                                    }
                                                }).setNeutralButton("취소", null)
                                                .show();

                                    }

                                }

                            }
                        },400);

                    }
                    return false;
                }
            });


        }

    }

    private Boolean check_goal(int p, DatabaseReference ds){
        // 도장판이 존재한다면 읽어오기, 없다면 for문 만큼 생성
        ds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    goal_exists = true;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

        return goal_exists;
    }

    //참여한사람 정보 가져오기
    private void ReadUidDialog(String cate2, String key2) {
        uid_key.clear();
        categoryReference.child(cate2).child(key2).addListenerForSingleValueEvent(new ValueEventListener() {
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

            }
        });

    }

}

