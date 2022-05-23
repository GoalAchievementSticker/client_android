package com.example.java_sticker;

public class GroupDialog {
    public String key;
    int gCount;
    int gGoal;
    String gTittle;
    String limit;
    String auth;


    public GroupDialog(){

    }

    public GroupDialog(int gCount, String gTittle,String limit,String auth, String key, int gGoal){
        this.gCount = gCount;
        this.gTittle = gTittle;
        this.limit=limit;
        this.auth=auth;
        this.key = key;
        this.gGoal = gGoal;

    }


    public int getgCount() {
        return gCount;
    }

    public void setgCount(int pCount) {
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

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
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
}
