package com.example.java_sticker;

public class personalDialog {

        int pCount;
        String pTittle;

        public personalDialog(){

        }

        public personalDialog(int pCount, String pTittle){
            this.pCount = pCount;
            this.pTittle = pTittle;
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

    @Override
    public String toString() {
        return "personalDialog{" +
                "pCount='" + pCount + '\'' +
                ", pTittle='" + pTittle + '\'' +
                '}';
    }
}
