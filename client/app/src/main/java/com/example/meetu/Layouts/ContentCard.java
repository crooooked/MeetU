package com.example.meetu.Layouts;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.meetu.Entities.Content;
import com.example.meetu.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//此类用于显示状态中的图片
public class ContentCard extends ConstraintLayout {
    Content content;

    ImageButton userHead;
    TextView userName;
    TextView postTime;

    TextView contentText;
    ContentImage contentImage;
    ContentRepost contentRepost;

    ImageButton repostButton;
    TextView repostSlogan;
    ImageButton remarkButton;
    TextView remarkSlogan;
    ImageButton likeButton;
    TextView likeSlogan;

    EditText remarkEdit;


    public ContentCard(Context context, Content content) {
        super(context);
        initView(context, content);
    }

    public void initView(Context context, Content content) {
        LayoutInflater.from(context).inflate(R.layout.view_content_card, this, true);
        this.content = content;

        userHead = findViewById(R.id.user_head);
        userName = findViewById(R.id.user_name);
        postTime = findViewById(R.id.post_time);
        contentText = findViewById(R.id.content);
        contentImage = findViewById(R.id.image_content);
        contentRepost = findViewById(R.id.repost_content);
        repostButton = findViewById(R.id.repost_button);
        repostSlogan = findViewById(R.id.repost_slogan);
        remarkButton = findViewById(R.id.remark_button);
        remarkSlogan = findViewById(R.id.remark_slogan);
        likeButton = findViewById(R.id.like_button);
        likeSlogan = findViewById(R.id.like_slogan);
        remarkEdit = findViewById(R.id.remark_edit);

        //加入数据
        //userHead头像
        Bitmap headImage = content.getUser().getHead_image();
        if(headImage != null)
            userHead.setImageBitmap(headImage);
        //userName用户名
        userName.setText(content.getUser().getUsername());
        //postTime发布时间
        SimpleDateFormat format =  new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String date_String = format.format(content.getTime());
        postTime.setText(date_String);
        //contentText状态内容
        contentText.setText(content.getContent());

        //显示图片模块contentImage
        ArrayList<Bitmap> images = content.getImages();
        if(images != null && images.size() != 0) {
            contentImage.initGrid(images.size());
            contentImage.showImages(images);
            contentImage.setBackground(getResources().getDrawable(R.color.colorGreyForRepostBackground));
        }

        //显示转发模块contentRepost
        if(content.getRepost() == content.NO_REPOST)
            contentRepost.setVisibility(GONE);
        else {
            Content repost = content.getRepostContent();
            contentRepost.setContent(repost.getUser().getUsername(), repost.getContent());
            if(repost.getImages() != null)
                contentRepost.setImages(repost.getImages());
        }

        //设置按钮点击事件

    }

    //事件源：userhead头像
    //进入该用户的个人空间
    public void visitPersonalSpace(View view) {

    }

    //事件源：repost_button + repost_slogan
    //转发状态
    public void repost(View view) {

    }

    //事件源：remark_button + remark_slogan
    //查看该状态的所有评论
    public void remark(View view) {

    }

    //事件源：like_button + like_slogan
    //评论状态
    public void like(View view) {

    }

    //事件源：remark_edit（输入结束后按回车）
    //评论状态
    public void submit_remark(View view) {

    }

}
