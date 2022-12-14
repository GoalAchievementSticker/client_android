package com.example.java_sticker.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java_sticker.Group_main;
import com.example.java_sticker.R;
import com.example.java_sticker.group.GroupDialog;
import com.example.java_sticker.group.close_add_goal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class CategorySearchAdapter extends RecyclerView.Adapter<CategorySearchAdapter.ViewHolder>{

    private ArrayList<GroupDialog> mDataset;
    Intent intent_close;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tittle, person_count, goal_count;
        CardView cardView;

        public ViewHolder(View view){
            super(view);
            tittle = (TextView) view.findViewById(R.id.fh_tittle);
            person_count = (TextView) view.findViewById(R.id.personal_count_view);
            goal_count = (TextView) view.findViewById(R.id.goal_count_view);
            cardView = (CardView) view.findViewById(R.id.custom_fraghome_cardView);
        }
    }

    public CategorySearchAdapter(ArrayList<GroupDialog> mDataset){
        this.mDataset = mDataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_fraghome, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final GroupDialog item = mDataset.get(position);

        holder.tittle.setText(mDataset.get(position).getgTittle());//?????????
        holder.person_count.setText(mDataset.get(position).getLimit_count() +"/"+ mDataset.get(position).getLimit()); //???????????? ???
        holder.goal_count.setText(mDataset.get(position).getgCount() +"???"); //????????? ??????

        holder.cardView.setOnClickListener(view -> {

            if(mDataset.get(position).getW_uid().equals(uid)){
                intent_close = new Intent(view.getContext(), close_add_goal.class);
                intent_close.putExtra("tittle",item.getgTittle()); //????????? ??????
                intent_close.putExtra("key", item.getKey()); //?????????????????? ?????????
                intent_close.putExtra("count", item.getgCount()); //????????? ??? ????????????
                intent_close.putExtra("limit", item.getLimit()); //????????? ?????? ?????????
                intent_close.putExtra("limit_count", item.getLimit_count()); //????????? ????????? ???
                intent_close.putExtra("auth",item.getAuth()); //????????? ????????????
                intent_close.putExtra("cate",item.getCate()); //????????? ????????????
                intent_close.putExtra("w_uid", item.getW_uid()); //????????? ?????????
                view.getContext().startActivity(intent_close);

            }else{
                //????????? ?????????????????? ????????? ?????????
                Bundle bundle = new Bundle();

                bundle.putString("goal",mDataset.get(position).getgTittle()); //????????????
                bundle.putInt("limit",mDataset.get(position).getLimit());//????????????
                bundle.putInt("limit_count", mDataset.get(position).getLimit_count()); //????????? ??????
                bundle.putInt("count",mDataset.get(position).getgCount());//??? ?????????
                bundle.putString("auth",mDataset.get(position).getAuth());//????????????
                bundle.putString("cate",mDataset.get(position).getCate()); //????????????
                bundle.putString("key", mDataset.get(position).getKey()); //?????????????????? ?????????

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment DetailFragment = new DetailFragment();
                DetailFragment.setArguments(bundle);
                FragmentManager fragmentManager = ((Group_main)view.getContext()).getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.group_layout,DetailFragment).addToBackStack(null).commit();
            }

            //activity.getFragmentManager().beginTransaction().replace(R.id.group_layout, DetailFragment).addToBackStack(null).commit();

            // ((Group_main) view.getContext()).getFragmentManager().beginTransaction().replace(R.id.group_layout, DetailFragment).commit();

        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
