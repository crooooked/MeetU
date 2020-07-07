package com.example.meetu.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.meetu.R;
import com.example.meetu.Tools.FileUtil;
import com.example.meetu.Tools.OkHttpUtils;

import java.io.IOException;

public class ReleaseContentActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_content);
        requestPermissions();
    }
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
                currentLocation++;
                ImageView imageView2=findViewById(imageLocation[currentLocation]);
                Glide.with(ReleaseContentActivity.this).load(R.drawable.icon_add_image).into(imageView2);
            }
        });
    }
}