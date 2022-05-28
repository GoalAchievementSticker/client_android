package com.example.java_sticker.group;

public class GroupDialog {
    public String key; //고유키
    int gCount; //토탈스티커개수
    int gGoal;//찍은스티커개수
    String gTittle;//목표
    int limit;//제한인원
    String auth;//인증방식
    String cate;// 카테고리
   // String[] uid;


    public GroupDialog(){

    }

    public GroupDialog(int gCount, String gTittle,int limit,String auth, String key, int gGoal,String cate){
        this.gCount = gCount;
        this.gTittle = gTittle;
        this.limit=limit;
        this.auth=auth;
        this.key = key;
        this.gGoal = gGoal;
        this.cate=cate;
       // this.uid = uid;

    }


    public int getgCount() {
        return gCount;
    }

    public void setgCount(int gCount) {
        this.gCount = gCount;
    }

    public String getgTittle() {
        return gTittle;
    }

    public void setgTittle(String gTittle) {
        this.gTittle = gTittle;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public int getgGoal() {
        return gGoal;
    }

    public void setgoal(int pGoal) {
        this.gGoal = pGoal;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }
    public String getCate(){
        return cate;
    }

//    public String[] getUid() {
//        return uid;
//    }
//
//    public void setUid(String[] uid) {
//        this.uid = uid;
//    }
}
