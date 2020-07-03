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
        //layout.addView(new ContentCard(this));
        ContentImage tst = new ContentImage(this, 9);
        layout.addView(tst);
        tst.initGrid();

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
        tst.showImages(images);
    }
}