package com.example.meetu.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.meetu.Entities.Convert;
import com.example.meetu.Tools.CustomVideoView;
import com.example.meetu.Tools.OkHttpUtils;
import com.example.meetu.R;


import java.util.Objects;
import java.util.regex.Pattern;
import com.example.meetu.Tools.CustomVideoView;

public class RegisterActivity extends AppCompatActivity {
    private EditText userName;
    private EditText passWord;
    private EditText confirmPwd;
    //相关IP可在OkhttpUtils进行修改
    private String url = "/register";

    private Button btnReg;
    private CustomVideoView customVideoView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        if(NavUtils.getParentActivityName(getParent())!=null){
            Objects.requireNonNull(getActionBar()).setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                if(NavUtils.getParentActivityName(getParent())!=null){
                    NavUtils.navigateUpFromSameTask(getParent());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userName = findViewById(R.id.ed_username_register);
        passWord = findViewById(R.id.ed_password_register);
        confirmPwd = findViewById(R.id.ed_password_register2);
//<<<<<<< Updated upstream
//        requestPermissions();
        Objects.requireNonNull(getSupportActionBar()).hide();
//=======
////        requestPermissions();
//>>>>>>> Stashed changes
    }

//    private void requestPermissions() {
//        String[] permissions = new String[]{
//                Manifest.permission.INTERNET,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.ACCESS_NETWORK_STATE};
//
//        int[] permissionCode = new int[]{100, 101, 102, 103};
//        for (int i = 0; i < permissions.length; i++) {
//            if (ContextCompat.checkSelfPermission(this,
//                    permissions[i]) !=
//                    PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{
//                        permissions[i]
//                }, permissionCode[i]);
//            }
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case 100:
//                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(getApplicationContext(), "你未获取网络权限！", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case 101:
//                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(getApplicationContext(), "你未获取存储权限！", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case 102:
//                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(getApplicationContext(), "你未获取读取权限！", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case 103:
//                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(getApplicationContext(), "你未获取读取网络状态权限！", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            default:
//        }
//    }
    //按钮点击事件
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onclick(View view) {
        String username = userName.getText().toString();
        String pswd = passWord.getText().toString();
        String pswd2 = confirmPwd.getText().toString();

        switch (view.getId()) {
            case R.id.btn_logup:
                if (logup(username, pswd, pswd2)) {
                    postSynchronize(username, pswd);
                }
                break;
            case R.id.tv_login:
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                break;
        }
    }
    //注册实现
    private boolean logup(String username, String password1, String password2) {
        boolean flag = true;
        String[] cueWords = {
                isNull(username, password1),
                notMatchPwd(password1),
                notEqual(password1, password2),
                notEnoughPwd(password1),
//               notWriteIn(username,password1),
        };
        //根据错误返回相应的提示
        for (String s : cueWords) {
            if (s != null) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                flag = false;
                break;
            }
        }
        return flag;
    }

    //密码长度
    private String notEnoughPwd(String pwd) {
        if (pwd.length() > 16 || pwd.length() < 6) {
            return "密码长度为6-16！";
        }
        return null;
    }

    //输入是否为空
    private String isNull(String username, String password) {
        if (username.equals("") || password.equals("")) {
            return "请输入！";
        }
        return null;
    }

    //两次密码是否相等
    private String notEqual(String password1, String password2) {
        if (!password1.equals(password2)) {
            return "两次密码不一致！";
        }
        return null;
    }

    //密码格式判断
    private String notMatchPwd(String password) {
        Pattern pattern = Pattern.compile("^(?=.*\\d)(?!^[0-9]+$)(?!^[A-z]+$)(?!^[^A-z0-9]+$)^[^\\s\\u4e00-\\u9fa5]{6,16}");
        if (!pattern.matcher(password).matches()) {
            return "密码必须包含字母、数字和特殊字符的两种！";
        }
        return null;
    }
    //在这个方法中连接网络，将新账号数据存入数据库
    //**************************************************************************************
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void postSynchronize(String name, String password) {
        final String json = Convert.strRegLog(name, password);
        OkHttpUtils instance = OkHttpUtils.getInstance();
        instance.doPost(url, json, new OkHttpUtils.OkHttpCallBackLinener() {
            @Override
            public void failure(Exception e) {
                Toast.makeText(RegisterActivity.this, "网络请求错误", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void success(String json) {
                String s = json;
                if(s.equals("200")){
                    Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                    RegisterActivity.this.finish();
                }
                else{
                    Toast.makeText(RegisterActivity.this, "注册失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
