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
import com.example.meetu.Fragments.DynamicsFragment;
import com.example.meetu.Fragments.PersonalSpaceFragment;
import com.example.meetu.Layouts.ContentCard;
import com.example.meetu.Layouts.ContentImage;
import com.example.meetu.R;

import java.util.ArrayList;
import java.util.Objects;
/*
* code 110
* */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ImageView imageView=LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_personal_space,null).findViewById(R.id.zone_return_arrow);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

//        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_space_personal,
//                new PersonalSpaceFragment()).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_space_personal,
                DynamicsFragment.newInstance(true)).commit();
    }

    //"<"箭头响应，回到关注页面
    public void Return_Last_Page(View view) {
        if(NavUtils.getParentActivityName(getParent())!=null) {
            Intent intent = new Intent();
            Log.e("!!!!!", " 执行了返回intent");
            setResult(160, intent);
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

}