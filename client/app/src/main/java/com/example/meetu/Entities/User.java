package com.example.meetu.Entities;

import android.graphics.Bitmap;

//用户类
public class User {
    int uid;            //id
    String username;    //用户名
    String password;    //密码
    String address;     //地址
    String gender;      //性别
    String head_url;    //头像url，对应后端数据库中的head
    Bitmap head_image;  //头像图片（通过head_url获取）
    String background_url;      //背景图片url，对应后端数据库中的background
    Bitmap background_image;    //背景图片（通过background_url获取）

    public User(int uid) {
        this.uid = uid;
    }

    public void setInfo(String username, String password, String address, String gender, String head_url, String background_url) {
        this.username = username;
        this.password = password;
        this.address = address;
        this.gender = gender;
        this.head_url = head_url;
        this.background_url = background_url;
    }

    //已知head_url，获取head_image
    public void getHeadImage() {

    }

    //已知background_url，获取background_image
    public void getBackgroundImage() {

    }

    public int getUid() {
        return uid;
    }

    public Bitmap getHead_image() {
        return head_image;
    }

    public Bitmap getBackground_image() {
        return background_image;
    }

    public String getBackground_url() {
        return background_url;
    }

    public String getAddress() {
        return address;
    }

    public String getGender() {
        return gender;
    }

    public String getHead_url() {
        return head_url;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }


    public void setAddress(String address) {
        this.address = address;
    }

    public void setBackground_image(Bitmap background_image) {
        this.background_image = background_image;
    }

    public void setBackground_url(String background_url) {
        this.background_url = background_url;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setHead_image(Bitmap head_image) {
        this.head_image = head_image;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
