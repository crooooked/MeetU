package com.example.meetu.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import com.example.meetu.Fragments.AttentionFragment;
import com.example.meetu.Fragments.DynamicsFragment;
import com.example.meetu.Fragments.PersonalFragment;
import com.example.meetu.R;

public class BodyActivity extends AppCompatActivity {
    private RadioGroup radioGroupButtom;
    private static String key_username;
    private static String key_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body);

        Intent intent=getIntent();
        key_username =intent.getStringExtra("userName");
        key_password=intent.getStringExtra("password");

        adatpterView();
        initView();
    }

    //此处初始化开始为进入个人信息页fragment_personal
    private void initView(){
        radioGroupButtom=findViewById(R.id.rg_nav);
        View view=View.inflate(BodyActivity.this,R.layout.fragment_personal,null);
        getSupportFragmentManager().beginTransaction().replace(R.id.view_above,
                PersonalFragment.newInstance(key_username,key_password)).commit();
        initDynamic();
    }

    private void adatpterView(){

    }
    //初始化
    private void initDynamic(){

    }

    //根据底栏按钮选择相应页面
    public void onclick(View view){
        Fragment fragment=new Fragment();
        switch (view.getId()){
            case R.id.rb_dynamic:
                fragment= DynamicsFragment.newInstance(key_username,key_password);
                break;
            case R.id.rb_personal:
                fragment= PersonalFragment.newInstance(key_username,key_password);
                break;
            case R.id.rb_follow:
                fragment= AttentionFragment.newInstance(key_username,key_password);
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.view_above,
                fragment).commit();
    }

    //顶栏设计
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_person,menu);
        return true;
    }
    //顶栏菜单
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_info:
                Log.e("menu", "onOptionsItemSelected: " );

                break;
        }
        return super.onOptionsItemSelected(item);
    }

}