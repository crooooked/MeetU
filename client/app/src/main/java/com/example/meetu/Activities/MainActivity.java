package com.example.meetu.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.meetu.Entities.Content;
import com.example.meetu.Layouts.ContentCard;
import com.example.meetu.Layouts.ContentImage;
import com.example.meetu.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout layout = findViewById(R.id.test);

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
        //3张图
        ArrayList<Bitmap> images2 = new ArrayList<>();
        images2.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image1));
        images2.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image2));
        images2.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image3));

        //测试无图片+无转发的状态
        Content content1 = new Content(head, null);
        //layout.addView(new ContentCard(this, content1));

        //测试有图片+无转发的状态
        Content content2 = new Content(head, null);
        content2.setImages(images2);
        //layout.addView(new ContentCard(this, content2));

        //测试无图片+有转发的状态
        Content content3 = new Content(head, null);
        content3.setRepost(123);
        content3.setRepostContent(content1);
        //layout.addView(new ContentCard(this, content3));

//        //测试ContentImage
//        ContentImage tst = new ContentImage(this, 9);
//        layout.addView(tst);
//        tst.initGrid();

//        tst.showImages(images);
    }
}