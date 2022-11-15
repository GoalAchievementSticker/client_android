package com.example.java_sticker;

public class UserRegister {
    public String userName; //사용자 이름
    public String profileImageUrl; //사용자 프로필사진
    public String userEmail; //사용자 이메일
    public String uid; //로그인한 사용자 고유아이디


    public  UserRegister(String userName,String profileImageUrl,String userEmail,String uid){
        this.userName=userName;
        this.profileImageUrl=profileImageUrl;
        this.userEmail=userEmail;
        this.uid=uid;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String title) {
        this.userName =userName;
    }
}
