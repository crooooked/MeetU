package com.example.meetu.Entities;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Content {
    public final int NO_REPOST = -1;

    int content_id;     //id
    int uid;            //发布者id
    User user;          //发布者信息（通过uid获取）
    long time;           //发布时间戳
    String content;     //状态内容
    int repost;         //转发，不是转发则为NO_REPOST，是转发则为被转发状态的content_id
    Content repostContent;  //转发内容（通过repost获取）
    String[] image_urls;    //图片的url，没有图片则为null
    ArrayList<Bitmap> images;    //图片，没有则为null（通过image_urls获取）

    public Content(Bitmap head, Bitmap background) {
        content_id = 0;
        uid = 0;
        time = 1593764071000L;
        content = "今天吃了米线，很开心！\n还和朋友逛街买了一个玩偶。";
        repost = -1;
        user = new User(head, background);
    }

    public int getContent_id() {
        return content_id;
    }

    public void setContent_id(int content_id) {
        this.content_id = content_id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRepost() {
        return repost;
    }

    public void setRepost(int repost) {
        this.repost = repost;
    }

    public Content getRepostContent() {
        return repostContent;
    }

    public void setRepostContent(Content repostContent) {
        this.repostContent = repostContent;
    }

    public String[] getImage_urls() {
        return image_urls;
    }

    public void setImage_urls(String[] image_urls) {
        this.image_urls = image_urls;
    }

    public ArrayList<Bitmap> getImages() {
        return images;
    }

    public void setImages(ArrayList<Bitmap> images) {
        this.images = images;
    }
}
