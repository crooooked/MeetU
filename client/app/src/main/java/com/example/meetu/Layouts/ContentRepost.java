package com.example.meetu.Layouts;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.meetu.Entities.Content;
import com.example.meetu.R;

import java.util.ArrayList;

//此类用于显示状态中的转发栏
public class ContentRepost extends ConstraintLayout {
    Content content;
    TextView repostContent;
    ContentImage repostContentImage;

    public ContentRepost(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_repost_content, this, true);
        init();
    }

    public ContentRepost(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.view_repost_content, this, true);
        init();
    }

    public void init() {
        repostContent = findViewById(R.id.repost_text);
        repostContentImage = findViewById(R.id.repost_content_image);

    }

    public void setContent(String username, String content) {
        repostContent.setText(username + "：" + content);
    }

    public void setImages(ArrayList<Bitmap> images) {
        repostContentImage.initGrid(images.size());
        repostContentImage.showImages(images);
    }
}
