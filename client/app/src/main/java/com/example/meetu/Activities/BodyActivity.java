package com.example.meetu.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.meetu.Fragments.AttentionFragment;
import com.example.meetu.Fragments.DynamicsFragment;
import com.example.meetu.Fragments.NewsFragment;
import com.example.meetu.Fragments.PersonalFragment;
import com.example.meetu.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class BodyActivity extends AppCompatActivity {

    public static String key_username;
    public static int key_id;
    private static String key_password;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body);

        Intent intent=getIntent();
        key_username =intent.getStringExtra("userName");
        key_password=intent.getStringExtra("passWord");
//        requestPermissions();
        initView();
    }

    private String []tabTitles={"关注","动态","个人"};
//    private String []tabTitles={"关注","动态","消息","个人"};
    private int []icon_bottom={};
    //此处初始化开始为进入动态fragment
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initView(){
        //屏蔽顶栏
        Objects.requireNonNull(getSupportActionBar()).hide();
        //连接容器
        TabLayout tabGroup =findViewById(R.id.tl_bottom_nav);
        ViewPager viewPager=findViewById(R.id.view_above);
        //绑定viewpager与fragment
        FragmentAdapter fragmentAdapter=new FragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(1);
        //tablayout与viewpage连接
        tabGroup.setupWithViewPager(viewPager);
    }

    private void requestPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_NETWORK_STATE};

        int[] permissionCode = new int[]{100, 101, 102, 103};
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this,
                    permissions[i]) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        permissions[i]
                }, permissionCode[i]);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "你未获取网络权限！", Toast.LENGTH_SHORT).show();
                }
                break;
            case 101:
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "你未获取存储权限！", Toast.LENGTH_SHORT).show();
                }
                break;
            case 102:
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "你未获取读取权限！", Toast.LENGTH_SHORT).show();
                }
                break;
            case 103:
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "你未获取读取网络状态权限！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    //fragment适配器
    private class FragmentAdapter extends FragmentStatePagerAdapter{
        public FragmentAdapter(@NonNull FragmentManager fm) {
            super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position){
                case 0:
                    fragment=new AttentionFragment();
                    break;
                case 2:
                    fragment=new PersonalFragment();
                    break;
                case 1:
                default:
                    fragment=new DynamicsFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

    }

}