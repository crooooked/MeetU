package com.example.meetu.Layouts;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import com.example.meetu.Entities.Content;
import com.example.meetu.Fragments.DynamicsFragment;
import com.example.meetu.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.INPUT_METHOD_SERVICE;

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

    TextView remark1;
    TextView remark2;
    EditText remarkEdit;

    DynamicsFragment parent;

    boolean editTextForRemark = true;


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
        remark1 = findViewById(R.id.remark_text1);
        remark2 = findViewById(R.id.remark_text2);
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
            Log.i("image_size", ""+images.size());
            contentImage.showImages(images);
            Log.i("show", "success");
            //contentImage.setBackground(getResources().getDrawable(R.color.colorGreyForRepostBackground));
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

        //显示评论
        String[] remarks_content = content.getRemarks_content();
        String[] remarks_username = content.getRemarks_username();
        if(remarks_content != null && remarks_content.length != 0) {
            remark1.setText(remarks_username[0] + "：" + remarks_content[0]);
            if(remarks_content.length ==2 && remarks_content[1] != null)
                remark2.setText(remarks_username[1] + "：" + remarks_content[1]);
            else
                remark2.setVisibility(GONE);
        } else {
            remark1.setVisibility(GONE);
            remark2.setVisibility(GONE);
        }

        //设置按钮点击事件
        //点赞
        likeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                like(view);
            }
        });
        likeSlogan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                like(view);
            }
        });
        //转发
        repostButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                repost(view);
            }
        });
        repostSlogan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                repost(view);
            }
        });
        //查看评论
        remarkButton.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                remark(view);
            }
        });
        remarkSlogan.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                remark(view);
            }
        });

        //评论回车监听
        remarkEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.i("OnEditorAction", "executed");
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(remarkEdit, InputMethodManager.HIDE_IMPLICIT_ONLY);
                submit_remark(remarkEdit.getText().toString());
                return true;
            }});
    }

    //事件源：userhead头像
    //进入该用户的个人空间
    public void visitPersonalSpace(View view) {

    }

    //事件源：repost_button + repost_slogan
    //转发状态
    //点击弹出键盘
    public void repost(View view) {
        remarkEdit.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(remarkEdit, InputMethodManager.SHOW_IMPLICIT);
        editTextForRemark = false;
    }

    //事件源：remark_button + remark_slogan
    //查看该状态的所有评论
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void remark(View view) {
        RemarkUnfold remarkUnfold = new RemarkUnfold(getContext());
        remarkUnfold.show();
        remarkUnfold.setInfo("http://" + content.getIP() + ":8080/get-remark?content_id="+content.getContent_id());
    }

    //事件源：like_button + like_slogan
    //评论状态
    public void like(View view) {
        boolean result = content.like();
        if(result)
            likeButton.setImageResource(R.mipmap.like_red);
        else
            likeButton.setImageResource(R.mipmap.like_grey);
    }

    //事件源：remark_edit（输入结束后按回车）
    //评论状态
    public void submit_remark(String text) {
        if(remarkEdit.getText().length() == 0)
            return;
        //发表评论
        if(editTextForRemark) {
            Log.i("remark", text);
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(remarkEdit, InputMethodManager.HIDE_IMPLICIT_ONLY);
            content.remark(text);
            remarkEdit.setText("");
        }
        //转发
        else {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
            Log.i("repost", "keyboard hide!");

            Log.i("repost", text);
            repostButton.setImageResource(R.mipmap.repost_red);
            content.repost(text);
            Log.i("repost", "finished!");

            remarkEdit.setText("");
            parent.fresh();
            Log.i("repost", "refreshed!");
        }
    }

    public void setfragment(DynamicsFragment parent) {
        this.parent = parent;
    }

}

