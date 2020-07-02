package com.example.meetu.Layouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.meetu.Entities.Content;
import com.example.meetu.R;

public class ContentCard extends ConstraintLayout {
    Content content;

    public ContentCard(Context context) {
        super(context);
        initView(context);
    }

    public void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_content_card, this, true);
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
