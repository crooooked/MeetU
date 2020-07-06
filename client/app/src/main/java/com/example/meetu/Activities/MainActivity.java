package com.example.meetu.Activities;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.meetu.Entities.Content;
import com.example.meetu.Layouts.ContentCard;
import com.example.meetu.Layouts.ContentImage;
import com.example.meetu.R;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout layout = findViewById(R.id.linear_layout_space);

        //测试用的数据
        Bitmap head = BitmapFactory.decodeResource(getResources(), R.mipmap.sample_head);
        //9张图
        ArrayList<Bitmap> images = new ArrayList<>();
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image1));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image2));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image3));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image4));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image5));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image6));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image7));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image8));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image9));

        //测试无图片+无转发的状态
        Content content1 = new Content(head, null);
        layout.addView(new ContentCard(this, content1));

        //测试有图片+无转发的状态
        Content content2 = new Content(head, null);
        content2.setImages(images);
        ContentCard contentCard2 = new ContentCard(this, content2);
        layout.addView(contentCard2);

        //测试无图片+有转发的状态
        Content content3 = new Content(head, null);
        content3.setRepost(123);
        content3.setRepostContent(content1);
        layout.addView(new ContentCard(this, content3));

//        //测试从网络获取content
//        Content content4 = new Content(this, 111);
//        layout.addView(new ContentCard(this, content4));
    }

    //"<"箭头响应，回到关注页面
    public void Return_Last_Page(View view) {
        if(NavUtils.getParentActivityName(getParent())!=null) {
            Intent intent = new Intent();
            Log.e("!!!!!", " 执行了返回intent");
            setResult(106, intent);
            this.finish();
        }
    }
    //顶栏返回父activity
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        if(NavUtils.getParentActivityName(getParent())!=null){
            Objects.requireNonNull(getActionBar()).setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setImage(Bitmap image) {

    }
}