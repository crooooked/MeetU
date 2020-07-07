package com.example.meetu.Layouts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.meetu.Activities.BodyActivity;
import com.example.meetu.Entities.User;
import com.example.meetu.Fragments.PersonalFragment;
import com.example.meetu.R;
import com.example.meetu.Tools.FileUtil;
import com.example.meetu.Tools.OkHttpUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

import okhttp3.OkHttpClient;

public class InformationActivity extends AppCompatActivity {

    private TextView tvHead;
    private EditText edgender;
    private EditText edaddress;
    private TextView tvbg;
    private String ip="http://10.236.66.58:8080";

    private static final int []RC_SELECT={101,102};
    private String []url={ip+"/upload-head",ip+"/upload-background"};

    //初始化
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        initView();
    }

    private User user;
    //初始化页面
    private void initView(){
        Intent intent=getIntent();

        tvHead=findViewById(R.id.tv_head);
        tvbg=findViewById(R.id.tv_background);
        edaddress=findViewById(R.id.ed_address);
        edgender=findViewById(R.id.ed_gender);

        user=intent.getParcelableExtra("user");

        assert user != null;
        edaddress.setText(user.getAddress());
        edgender.setText(user.getGender());
    }

    //点击事件
    public void onClickChangeInformation(View view){
        switch (view.getId()){
            case R.id.tv_head:
                captureImage(0);
                break;
            case R.id.tv_background:
                captureImage(1);
                break;
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

    private String bHDUrl=null;
    private String bBGUrl=null;
    //通过顶栏返回键返回父activity
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                if(NavUtils.getParentActivityName(getParent())!=null){
//                    NavUtils.navigateUpFromSameTask(getParent());
                    Intent intent=new Intent();
                    Log.e("!!!!!", " 执行了返回intent");
                    intent.putExtra("user",user);
                    intent.putExtra("head",bHDUrl);
                    intent.putExtra("background",bBGUrl);
                    setResult(104,intent);
                    this.finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //获取本地图片
    private void captureImage(int index){
        Intent intent=new Intent(Intent.ACTION_PICK,null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        startActivityForResult(intent,RC_SELECT[index]);
    }

    @Override
    //获取本地图片路径
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null&&data.getData()!=null) {
            String filePath = FileUtil.getFilePathByUri(this, data.getData());
            int index=0;
            switch (requestCode) {
                case 101:
                    index=0;
                    bHDUrl=filePath;
                    break;
                case 102:
                    index=1;
                    bBGUrl=filePath;
                    break;
            }
            postImage(filePath,index);
        }
    }

    //上传图片的网络行为
    private void postImage(final String filepath, final int index){
        Log.e("!!!!!!", filepath );

        OkHttpUtils instance=OkHttpUtils.getInstance();
        final File file=new File(filepath);
        instance.postImage(url[index], file, user.getUsername(), new OkHttpUtils.OkHttpCallBackLinener() {
            @Override
            public void failure(Exception e) {
                Toast.makeText(getApplicationContext(),"网罗请求错误1",Toast.LENGTH_LONG).show();
            }
            @Override
            public void success(String json) {
                Toast.makeText(getApplicationContext(),"上传成功！",Toast.LENGTH_LONG).show();
                View view=View.inflate(getApplicationContext(),R.layout.fragment_personal,null);
                ImageView imageView;
                switch (index){
                    case 0:
                        imageView=view.findViewById(R.id.iv_head_mine);
                        Glide.with(getParent()).load(file).into(imageView);
                        break;
                    case 1:
                        imageView=view.findViewById(R.id.iv_bg_mine);
                        Glide.with(getParent()).load(file).into(imageView);
                        break;
                }
            }
        });
    }



}