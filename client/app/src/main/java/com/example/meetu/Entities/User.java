package com.example.meetu.Entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

    //用来测试的sample数据
    public User(Bitmap head, Bitmap background) {
        uid = 0;
        username = "萌神张飞";
        head_image = head;
        background_image = background;
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
        try {
            Log.i("head_url", head_url);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(head_url)
                    .build();
            Response response = client.newCall(request).execute();
            byte[] bytes = response.body().bytes();
            head_image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //已知background_url，获取background_image
    public void getBackgroundImage() {
        try {
            Log.i("background_url", background_url);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(background_url)
                    .build();
            Response response = client.newCall(request).execute();
            byte[] bytes = response.body().bytes();
            background_image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
