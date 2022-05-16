package com.example.java_sticker;

import android.net.Uri;

import java.util.ArrayList;

public class GridItem {

    String goal_id;
    Uri uri;


    public GridItem(String goal_id,Uri uri){
        this.goal_id = goal_id;
        //this.goal_img_Id = goal_img_Id;
        this.uri =uri;

    }

    public String getGoal_id() {
        return goal_id;
    }

    public void setGoal_id(String goal_id) {
        this.goal_id = goal_id;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }



    //    public int getGoal_img_Id() {
//        return goal_img_Id;
//    }
//
//    public void setGoal_img_Id(int goal_img_Id) {
//        this.goal_img_Id = goal_img_Id;
//    }
}
