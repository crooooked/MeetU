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
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meetu.Entities.Convert;
import com.example.meetu.Entities.User;
import com.example.meetu.Fragments.AttentionFragment;
import com.example.meetu.Fragments.DynamicsFragment;
import com.example.meetu.Fragments.PersonalFragment;
import com.example.meetu.R;
import com.example.meetu.Tools.OkHttpUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class BodyActivity extends AppCompatActivity {

    public static String key_username;
    public static int key_id;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body);

        Intent intent=getIntent();
        key_username =intent.getStringExtra("userName");
        key_id=intent.getIntExtra("userId",0);
        requestPermissions();
        initView();
    }

    private String []tabTitles={"       关注","       动态","       个人"};

//    private int []icon_bottom={};

    private int []icon_bottom={R.drawable.focus_icon,R.drawable.dynamic_icon,R.drawable.my_icon};

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


        for(int i=0;i<tabGroup.getTabCount();i++){
            TabLayout.Tab tab=tabGroup.getTabAt(i);
            if(tab!=null){
                tab.setCustomView(getTabView(tabTitles[i],icon_bottom[i]));
            }
        }


        tabGroup.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // TODO Auto-generated method stub
                changeTabNormal(tab);
            }
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeTabSelect(tab);
            }
            @Override
            public void onTabReselected(TabLayout.Tab arg0) {
            }
        });


    }

    private void setKeyId() {
        String urlTail="/get-information?username="+key_username;

        OkHttpUtils instance=OkHttpUtils.getInstance();
        instance.doGet(urlTail, new OkHttpUtils.OkHttpCallBackLinener() {
            @Override
            public void failure(Exception e) {

            }

            @Override
            public void success(String json) {
                Log.e("!!!!!", json );
                User user= Convert.getUserFromStr(json);
                key_id=user.getUid();
            }
        });
    }


    private View getTabView(String tabTitle, int src) {
        Context mContext=getApplicationContext();
        View v = LayoutInflater.from(mContext).inflate(R.layout.bottom_tab_item, null);
        TextView textView = (TextView) v.findViewById(R.id.tab_textview);
        textView.setText(tabTitle);

        ImageView imageViewNull=(ImageView) v.findViewById(R.id.tab_imageviewNull);
        ImageView imageView = (ImageView) v.findViewById(R.id.tab_imageview);
        imageViewNull.setImageResource(R.drawable.my_icon);
        imageViewNull.setVisibility(View.INVISIBLE);
        imageView.setImageResource(src);
        return v;
    }


    private void changeTabSelect(TabLayout.Tab tab) {

        ViewPager viewPager=findViewById(R.id.view_above);
        View view = tab.getCustomView();
        ImageView img_title = (ImageView) view.findViewById(R.id.tab_imageview);
        TextView txt_title = (TextView) view.findViewById(R.id.tab_textview);
        txt_title.setTextColor(Color.parseColor("#47bafe"));
        if (txt_title.getText().toString().equals("       关注")) {
                  img_title.setImageResource(R.drawable.focus_icon);
                  viewPager.setCurrentItem(0);
        } else if (txt_title.getText().toString().equals("       动态")) {
            img_title.setImageResource(R.drawable.dynamic_icon);
            viewPager.setCurrentItem(1);

        }  else if (txt_title.getText().toString().equals("       个人")) {
            img_title.setImageResource(R.drawable.my_icon);
            viewPager.setCurrentItem(2);
        }
    }

    private void changeTabNormal(TabLayout.Tab tab) {
        View view = tab.getCustomView();
        ImageView img_title = (ImageView) view.findViewById(R.id.tab_imageview);
        TextView txt_title = (TextView) view.findViewById(R.id.tab_textview);
        txt_title.setTextColor(Color.parseColor("#C1B9B9"));
        if (txt_title.getText().toString().equals("       关注")) {
            img_title.setImageResource(R.drawable.unfocus_icon);
        } else if (txt_title.getText().toString().equals("       动态")) {
            img_title.setImageResource(R.drawable.undynamic_icon);
        }
        else if (txt_title.getText().toString().equals("       个人")) {
            img_title.setImageResource(R.drawable.unmy_icon);
        }
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
//                case 0:
//                    fragment=new AttentionFragment();
//                    break;
//                case 1:
//                    fragment=new DynamicsFragment();
//                    break;
//                case 2:
//                    fragment=new NewsFragment();
//                    break;
//
//                default:
//                    fragment=new  PersonalFragment();
//                    break;
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