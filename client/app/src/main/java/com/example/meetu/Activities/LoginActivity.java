package com.example.meetu.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.meetu.Entities.Convert;
import com.example.meetu.Entities.User;
import com.example.meetu.Tools.OkHttpUtils;
import com.example.meetu.R;

import org.json.JSONException;
import org.json.JSONObject;
import com.example.meetu.Tools.CustomVideoView;

import java.io.File;
import java.util.Objects;
/**
 * code 100-110
 *
 *
 */
public class LoginActivity extends AppCompatActivity {

    private EditText edUserName;
    private EditText edPassword;

    private User mainUser;
    public static String ip="10.234.184.80";
    private CustomVideoView customVideoView;
    private MediaPlayer mp1;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();
        customVideoView = (CustomVideoView)findViewById(R.id.videoview);
        //加载视频文件
        customVideoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.bg));
        //播放
        customVideoView.start();
        //循环播放
        customVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                customVideoView.start();
            }
        });
        customVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                                  @Override
                                                  public void onPrepared(MediaPlayer mp) {
                                                      mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                                                          @Override
                                                          public boolean onInfo(MediaPlayer mp, int what, int extra) {
                                                              System.out.println("执行到了prepare");
                                                              if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                                                                  View placeholder = findViewById(R.id.placeholder);
                                                                  placeholder.setVisibility(View.GONE);
                                                                  customVideoView.setBackgroundColor(Color.TRANSPARENT);
                                                                  System.out.println("执行到了最里面");
                                                              }
                                                              return true;
                                                          }
                                                      });
                                                  }
                                              });

                edUserName=findViewById(R.id.ed_username);
        edPassword=findViewById(R.id.ed_password);
        requestPermissions();
        //找VideoView控件

    }


    //request for all permissions
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 101:
                if(grantResults.length==0||grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(),"你未获取网络权限！",Toast.LENGTH_SHORT).show();
                }
                break;
            case 102:
                if(grantResults.length==0||grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(),"你未获取存储权限！",Toast.LENGTH_SHORT).show();
                }
                break;
            case 103:
                if(grantResults.length==0||grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(),"你未获取读取权限！",Toast.LENGTH_SHORT).show();
                }
                break;
            case 104:
                if(grantResults.length==0||grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(),"你未获取读取网络状态权限！",Toast.LENGTH_SHORT).show();
                }
                break;
            case 105:
                if(grantResults.length==0||grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(),"你未获取相机权限！",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onclick(View v){
        String userName=edUserName.getText().toString().trim();
        String password=edPassword.getText().toString().trim();
        switch(v.getId()){

            case R.id.btn_login:
                if(login(userName,password)){

                    PostLogin(userName,password);
                }

                break;
            case R.id.tv_register:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
//                setContentView(R.layout.activity_register);
//                this.finish();
                break;
        }
    }

    private boolean login(String username,String password){
        boolean flag=true;
        String []cueWords={
                isNull(username,password),
                notRight(username,password),
        };
        for(String s:cueWords){
            if(s!=null){
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                flag=false;
                break;
            }
        }
        return flag;
    }
    //检查是否未输入
    private String isNull(String username,String password){
        if(username.equals("")||password.equals("")){
            return "请输入！";
        }
        return null;
    }
    //检查密码正确性
    private String notRight(String username,String password){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("username",username);
            jsonObject.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
//    //连接网络
//<<<<<<< Updated upstream
//    private static final String url="http://"+ip+":8080/login";
//=======
    //相关IP可在OkhttpUtils进行修改
    private static final String urlTail="/login";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void PostLogin(final String name, final String pwd){
        final String json = Convert.strRegLog(name, pwd);
        OkHttpUtils instance = OkHttpUtils.getInstance();
        Log.i("json", json);
        instance.doPost(urlTail, json, new OkHttpUtils.OkHttpCallBackLinener() {
            @Override
            public void failure(Exception e) {
                Toast.makeText(LoginActivity.this, "网络请求错误", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void success(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int key_id = jsonObject.getInt("uid");
                    if (key_id != 0) {
                        Intent intent = new Intent(LoginActivity.this, BodyActivity.class);
                        intent.putExtra("userName", name);
                        intent.putExtra("userId", key_id);

                        startActivity(intent);
                        LoginActivity.this.finish();
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "密码错误或账号不存在！", Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        );
    }

}