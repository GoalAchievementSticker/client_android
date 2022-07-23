package com.example.java_sticker.group;

public class GroupDialog {
    public String key; //고유키
    int gCount; //토탈스티커개수
    int gGoal;//찍은스티커개수
    String gTittle;//목표
    int limit;//제한인원
    String auth;//인증방식
    String cate;// 카테고리
    int limit_count; //제한인원 얼마나 들어왔나 카운트세기
    String w_uid; //게시글 작성자 uid
    String name;//본인 이름
    String uid_auth; //본인 uid(참가버튼 누른 uid or 작성자 자신 uid)
    boolean close; //마감버튼 클릭시 true변경하고 마감
    String date;
   // String[] uid;


    public GroupDialog(){

    }

    public GroupDialog(int gCount, String gTittle,int limit,String auth, String key, int gGoal,String cate, int limit_count, String w_uid, String name, String uid_auth, boolean close, String date){
        this.gCount = gCount;
        this.gTittle = gTittle;
        this.limit=limit;
        this.auth=auth;
        this.key = key;
        this.gGoal = gGoal;
        this.cate=cate;
        this.limit_count = limit_count;
        this.w_uid = w_uid;
        this.name = name;
        this.uid_auth = uid_auth;
        this.close = close;
        this.date = date;

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


    public void setLimit_count(int limit_count) {
        this.limit_count = limit_count;
    }

    public int getLimit_count() {
        return limit_count;
    }

    public String getW_uid() {
        return w_uid;
    }

    public void setW_uid(String w_uid) {
        this.w_uid = w_uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getUid_auth() {
        return uid_auth;
    }

    public void setUid_auth(String uid_auth) {
        this.uid_auth = uid_auth;
    }

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }
}
