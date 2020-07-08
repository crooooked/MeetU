package com.example.meetu.Layouts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meetu.Entities.Convert;
import com.example.meetu.Entities.User;
import com.example.meetu.R;
import com.example.meetu.Tools.FileUtil;
import com.example.meetu.Tools.OkHttpUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.IOException;

public class InformationActivity extends AppCompatActivity {

    private TextView tvHead;
    private TextView edgender;
    private TextView edaddress;
    private TextView tvbg;

    private static final int [] IMAGE_SELECT_OBJ ={114,115};
    private String []url={"/upload-head","/upload-background"};

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
        String username=getIntent().getStringExtra("username");

        tvHead=findViewById(R.id.tv_head);
        tvbg=findViewById(R.id.tv_background);
        edaddress=findViewById(R.id.tv_address_content);
        edgender=findViewById(R.id.tv_gender_content);


//                intent.getStringExtra("username");
        getInformationMethod(username);
    }

    //获取数据
    private void getInformationMethod(String username){
        String urlTail="/get-information?username="+username;
        OkHttpUtils instance=OkHttpUtils.getInstance();
        instance.doGet(urlTail, new OkHttpUtils.OkHttpCallBackLinener() {
            @Override
            public void failure(Exception e) {
            }
            @Override
            public void success(String json) {
                user=Convert.getUserFromStr(json);
                edgender.setText(user.getGender());
                edaddress.setText(user.getAddress());
            }
        });
    }

    //点击事件
    public void onClickChangeInformation(View view){
        switch (view.getId()){
            case R.id.tv_head:
                getImage(0);break;
            case R.id.tv_address_content:
                break;
            case R.id.tv_gender_content:
                break;
            case R.id.tv_background:
                getImage(1);
                break;
        }
    }

    //出现底部选择栏
    private void getImageOptions(final int select){
        final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(this);
        bottomSheetDialog.setCancelable(true);
        View view= LayoutInflater.from(InformationActivity.this).inflate(R.layout.view_getimage_bottom,null);
        bottomSheetDialog.setContentView(R.layout.view_getimage_bottom);
        final Button btnSelectCamera=view.findViewById(R.id.btn_select_camera);

        final Button btnSelectPhoto=view.findViewById(R.id.tv_select_photo);


//        bottomSheetDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
//                return false;
//            }
//        });

        btnSelectCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("!!!!!", "onClick: ");
                bottomSheetDialog.dismiss();
                capturePhoto(select);
            }
        });

        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                getImage(select);
            }
        });
        bottomSheetDialog.show();
    }
//    Button btnSelectCamera;
//    Button btnSelectPhoto;
//    //顶栏返回父activity
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//        if(NavUtils.getParentActivityName(getParent())!=null){
//            Objects.requireNonNull(getActionBar()).setDisplayHomeAsUpEnabled(true);
//        }
//    }
    //通过顶栏返回键返回父activity
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                return true;
            default:
                Intent intent=new Intent();
                refresh();
                setResult(112,intent);
                this.finish();
                return super.onOptionsItemSelected(item);
        }
    }
    //更新文本资料
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void refresh(){
        String curAddress=edaddress.getText().toString();
        String curGender=edgender.getText().toString();
        if(!curAddress.equals(user.getAddress())
                ||!curGender.equals(user.getGender()))
        {
            updateInformation(curAddress,curGender);
        }
    }
    //更新个人资料的网络行为
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void updateInformation(final String address, final String gender){
        final String tailUrl="/change-information";
        OkHttpUtils instance=OkHttpUtils.getInstance();
        String []keys={"username","address","gender"};
        String []values={user.getUsername(),address,gender};
        String information= Convert.strJsonObject(keys,values);
        instance.doPost(tailUrl, information, new OkHttpUtils.OkHttpCallBackLinener() {
            @Override
            public void failure(Exception e) {
            }
            @Override
            public void success(String json) {
            }
        });
    }

    //拍照
    private void capturePhoto(int index){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//       File fileDir=new File(Environment.getExternalStoragePublicDirectory()+File.separator+"photo"+File.separator);
        startActivityForResult(intent, IMAGE_SELECT_OBJ[index]);
    }

    //获取本地图片
    private void getImage(int index){
        Intent intent=new Intent(Intent.ACTION_PICK,null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        startActivityForResult(intent, IMAGE_SELECT_OBJ[index]);
    }
    @Override
    //获取本地图片路径
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int index=0;
        switch (requestCode){
            case 114:
                if(resultCode==RESULT_OK)
                    index=0;
                break;
            case 115:
                if(RESULT_OK==resultCode)
                    index=1;
                break;
        }
        if (data != null&&data.getData()!=null) {
            String filePath = FileUtil.getFilePathByUri(this, data.getData());

            try {
                postImage(filePath,index);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //上传图片的网络行为
    private void postImage(final String filepath, final int index) throws IOException {
        OkHttpUtils instance=OkHttpUtils.getInstance();
        instance.postImage(url[index], filepath, user.getUsername(), new OkHttpUtils.OkHttpCallBackLinener() {
            @Override
            public void failure(Exception e) {
                if(e!=null&&e.getMessage()!=null) {
                    Log.e("ERROR", e.getMessage());
                }
            }
            @Override
            public void success(String json) {
                if(json.equals("400")){
                    Toast.makeText(getApplicationContext(),"请重新上传！",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"上传成功！",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}