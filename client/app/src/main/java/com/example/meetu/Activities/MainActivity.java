package com.example.meetu.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

        //测试无图片+无转发的状态
        Content content1 = new Content(head, null);
        layout.addView(new ContentCard(this, content1));

        //测试有图片+无转发的状态
        Content content2 = new Content(head, null);
        content2.setImages(images);
        ContentCard contentCard2 = new ContentCard(this, content2);
        layout.addView(contentCard2);

//        android.view.ViewGroup.LayoutParams lp = contentCard2.getLayoutParams();
//        lp.height = contentCard2.getMeasuredWidth();
//        Log.i("height", ""+lp.height);
//        contentCard2.setLayoutParams(lp);

        //测试无图片+有转发的状态
        Content content3 = new Content(head, null);
        content3.setRepost(123);
        content3.setRepostContent(content1);
        layout.addView(new ContentCard(this, content3));

//        ContentImage contentImage = new ContentImage(this);
//        contentImage.initGrid(images.size());
//        contentImage.showImages(images);
//        //layout.addView(contentImage);
//
//        ContentImage contentImage2 = new ContentImage(this);
//        contentImage2.initGrid(images.size());
//        contentImage2.showImages(images);
//        layout.addView(contentImage2);
//
//        View view = LayoutInflater.from(this).inflate(R.layout.content_image_item, null);
//        final ImageView imageView = view.findViewById(R.id.image_item);
//        imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image2));
//        layout.addView(view);
//
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(360, 360);
//        view.setLayoutParams(lp);
//
//        View view2 = LayoutInflater.from(this).inflate(R.layout.content_image_item, null);
//        final ImageView imageView2 = view2.findViewById(R.id.image_item);
//        imageView2.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image2));
//        layout.addView(view2);
//
//        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(360, 360);
//        view2.setLayoutParams(lp2);

        //测试ContentImage
//        ContentImage tst = new ContentImage(this);
//        layout.addView(tst);
//        tst.initGrid(9);

//        tst.showImages(images);
    }
}