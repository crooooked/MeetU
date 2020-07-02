package com.example.meetu.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.meetu.Entities.Content;
import com.example.meetu.Layouts.ContentCard;
import com.example.meetu.Layouts.ContentImage;
import com.example.meetu.R;

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
    }
}