package com.example.java_sticker;

//그리드뷰 데이터 클래스
public class GridItem {

    int goal_id; //도장판 아이디=도장판 개수(0~N)
    int goal_img_Id; //이미지

    public GridItem(){

    }


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
