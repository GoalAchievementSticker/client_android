package com.example.java_sticker;

//스티커이미지,헤더,도장판개수
public class GridItem {

    int resId;


    public GridItem(int resId){
        this.resId = resId;
    }

    public int getResId(){
        return resId;
    }
    public void  setResId(int resId){
        this.resId = resId;
    }


}
