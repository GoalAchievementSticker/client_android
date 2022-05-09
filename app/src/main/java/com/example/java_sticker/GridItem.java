package com.example.java_sticker;

//스티커이미지,헤더,도장판개수
public class GridItem {

    int goal_id;
    int goal_img_Id;


    public GridItem(int goal_id, int goal_img_Id){
        this.goal_id = goal_id;
        this.goal_img_Id = goal_img_Id;
    }

    public int getGoal_id() {
        return goal_id;
    }

    public void setGoal_id(int goal_id) {
        this.goal_id = goal_id;
    }

    public int getGoal_img_Id() {
        return goal_img_Id;
    }

    public void setGoal_img_Id(int goal_img_Id) {
        this.goal_img_Id = goal_img_Id;
    }
}
