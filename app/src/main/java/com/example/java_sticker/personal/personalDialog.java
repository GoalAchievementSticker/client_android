package com.example.java_sticker.personal;

import java.util.ArrayList;

public class personalDialog {

        int pCount;
        int pGoal;
        String pTittle;
        String key;

        public personalDialog(){

        }

        public personalDialog(int pCount, String pTittle, String key, int pGoal){
            this.pCount = pCount;
            this.pTittle = pTittle;
            this.key = key;
            this.pGoal = pGoal;

        }

    public int getpCount() {
        return pCount;
    }

    public void setpCount(int pCount) {
        this.pCount = pCount;
    }

    public String getpTittle() {
        return pTittle;
    }

    public void setpTittle(String pTittle) {
        this.pTittle = pTittle;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getpGoal() {
        return pGoal;
    }

    public void setpGoal(int pGoal) {
        this.pGoal = pGoal;
    }
}
