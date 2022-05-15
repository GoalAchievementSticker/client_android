package com.example.java_sticker;

import java.util.ArrayList;

public class GridItem {

    int goal_id;
    String test;


    public GridItem(int goal_id,String test){
        this.goal_id = goal_id;
        //this.goal_img_Id = goal_img_Id;
        this.test = test;

    }

    public int getGoal_id() {
        return goal_id;
    }

    public void setGoal_id(int goal_id) {
        this.goal_id = goal_id;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }



    //    public int getGoal_img_Id() {
//        return goal_img_Id;
//    }
//
//    public void setGoal_img_Id(int goal_img_Id) {
//        this.goal_img_Id = goal_img_Id;
//    }
}
