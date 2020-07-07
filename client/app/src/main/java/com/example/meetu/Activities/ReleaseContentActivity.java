package com.example.meetu.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.meetu.Entities.Content;
import com.example.meetu.Entities.Convert;
import com.example.meetu.R;
import com.example.meetu.Tools.FileUtil;
import com.example.meetu.Tools.OkHttpUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class ReleaseContentActivity extends AppCompatActivity {

    //对应图片
    private int []imageLocation={
            R.id.image_1,
            R.id.image_2,
            R.id.image_3,
            R.id.image_4,
            R.id.image_5,
            R.id.image_6,
            R.id.image_7,
            R.id.image_8,
            R.id.image_9,
    };

    //动态
    private Content content;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_content);
        Objects.requireNonNull(getSupportActionBar()).hide();

        requestPermissions();
        content=new Content(1);
        content.setRepost(-1);
        content.setUid(2);
    }

    //获得list to array 的数据
    private String[] list2array(List<String>a){
        int size=a.size();
        String []s=new String[size];
        for(int i=0;i<size;i++){
            s[i]=a.get(i);
        }
        return s;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    //发布点击的事件
    public void onclickrelease(View view){
        TextView tvContent=findViewById(R.id.tv_release_content);
        content.setContent(tvContent.getText().toString());
        content.setImage_urls(list2array(imagesList));
        switch (view.getId()){
            case R.id.tv_release_content:
                if(currentLocation==0||content.getContent()==null){
                    Toast.makeText(getApplicationContext(),"你还未进行编辑！",Toast.LENGTH_SHORT).show();
                }else {
                    postContent();
                }
                break;
        }
    }

    //发布动态的网络行为
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void postContent(){
        String urlTail="/release";
        OkHttpUtils instance=OkHttpUtils.getInstance();
        String posJson=Convert.getStrContentFromContent(content);
        instance.doPost(urlTail, posJson, new OkHttpUtils.OkHttpCallBackLinener() {
            @Override
            public void failure(Exception e) {
                Toast.makeText(getApplicationContext(),"发布状态失败！",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void success(String json) {
                Toast.makeText(getApplicationContext(),"发布成功！",Toast.LENGTH_SHORT).show();
                setResult(0);
                ReleaseContentActivity.this.finish();
            }
        });
    }


    private List<String> imagesList=new ArrayList<>();
    //当前添加图片的位置位置的
    private int currentLocation=0;
    public void onClickChooseImage(View view) {
        if((currentLocation<10) && (view.getId()==imageLocation[currentLocation])){
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, 2005);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 2005: {
                if (data != null && data.getData() != null) {
                    String filePath = FileUtil.getFilePathByUri(this, data.getData());
                    try {
                        postImage(filePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }
    private void requestPermissions() {
        String []permissions=new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.CAMERA,

                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                Manifest.permission.ACCESS_CHECKIN_PROPERTIES
        };

        int []permissionCode=new int[]{101,102,103,104,105,106,107};
        for (int i=0;i<permissions.length;i++){
            if (ContextCompat.checkSelfPermission(this,
                    permissions[i]) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        permissions[i]
                }, permissionCode[i]);
            }
        }
    }
    //上传图片的网络行为
    private void postImage(final String filepath) throws IOException {
        String url="/upload-image";
        OkHttpUtils instance=OkHttpUtils.getInstance();
        instance.postImage(url, filepath, new OkHttpUtils.OkHttpCallBackLinener() {
            @Override
            public void failure(Exception e) {
                if(e!=null&&e.getMessage()!=null) {
                    Log.e("ERROR", e.getMessage());
                }
            }
            @Override
            public void success(String json) {
                Toast.makeText(getApplicationContext(),"上传成功！",Toast.LENGTH_LONG).show();
                ImageView imageView1=findViewById(imageLocation[currentLocation]);
                Glide.with(ReleaseContentActivity.this).load(filepath).centerCrop().into(imageView1);
                File file=new File(filepath);
                imagesList.add(file.getName());
                currentLocation++;
                ImageView imageView2=findViewById(imageLocation[currentLocation]);
                Glide.with(ReleaseContentActivity.this).load(R.drawable.icon_add_image).into(imageView2);
            }
        });
    }
}