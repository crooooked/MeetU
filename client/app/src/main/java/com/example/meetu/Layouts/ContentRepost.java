package com.example.meetu.Layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.meetu.R;

//此类用于显示状态中的转发栏
public class ContentRepost extends ConstraintLayout {
    public ContentRepost(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_repost_content, this, true);
    }

    public ContentRepost(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.view_repost_content, this, true);
    }
}
