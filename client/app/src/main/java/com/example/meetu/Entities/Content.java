package com.example.meetu.Entities;

import android.graphics.Bitmap;

public class Content {
    int content_id;     //id
    int uid;            //发布者id
    User user;          //发布者信息（通过uid获取）
    int time;           //发布时间戳
    String content;     //状态内容
    int repost;         //转发，不是转发则为null，是转发则为被转发状态的content_id
    Content repostContent;  //转发内容（通过repost获取）
    String[] image_urls;    //图片的url，没有图片则为null
    Bitmap[] images;    //图片，没有则为null（通过image_urls获取）
}
