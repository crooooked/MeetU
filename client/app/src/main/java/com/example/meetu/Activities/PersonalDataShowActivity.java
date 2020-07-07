package com.example.meetu.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.example.meetu.FocusClass.AnalyseJson;
import com.example.meetu.FocusClass.Httprequest;
import com.example.meetu.FocusClass.ToJson;

import com.example.meetu.R;
import com.example.meetu.Tools.GlideCircleTransform;

import org.json.JSONException;

import java.io.IOException;
import java.util.Objects;

/*
先别写，等关注列表显示出来了再写这个
 */
public class PersonalDataShowActivity extends AppCompatActivity {

    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {

            super.handleMessage(msg);
            int w = msg.what;
            String getMsg= msg.obj.toString();
            AnalyseJson analyseJson=new AnalyseJson();

            tv_nick=findViewById(R.id.tv_nick_show);
            tv_gender=findViewById(R.id.tv_gender_show);
            tv_addr=findViewById(R.id.tv_addr_show);

            image_head=findViewById(R.id.image_person_head);
            image_background=findViewById(R.id.image_background_show);
            btn_focus=findViewById(R.id.btn_focus);

            if (w==1){
                //json字符串转换为JSONObject对象
                JSONObject jsonObject=JSONObject.parseObject(getMsg);
                //解析JSONObject对象

                //get到服务器的图片 name gender address ，
                //w=1，显示在个人资料的iMAgeview和TextView中
                //解析message
                Bitmap head,background;
                String headUrl,backgroundUrl;
                String username,gender,address;
                username=analyseJson.getUsername(jsonObject);
                gender=analyseJson.getGender(jsonObject);
                address=analyseJson.getAddress(jsonObject);
                headUrl=analyseJson.getHeadUrl(jsonObject);
                backgroundUrl=analyseJson.getBackgroundUrl(jsonObject);
                Glide.with(PersonalDataShowActivity.this).load(headUrl).transform(new GlideCircleTransform(PersonalDataShowActivity.this)).into(image_head);
                Glide.with(PersonalDataShowActivity.this).load(backgroundUrl).centerCrop().into(image_background);


                tv_nick.setText(username);
                tv_gender.setText(gender);
                tv_addr.setText(address);

            }

        }


    };

    public TextView tv_nick,tv_gender,tv_addr;
    public Button btn_focus,btn_watch;
    public ImageButton return_iconButton;
    public ImageView image_head,image_background;
    public String focusText;
    Httprequest httprequest=new Httprequest();
    String ip=LoginActivity.ip;

    String url_getInformation="http://"+ip+":8080/get-information";


    String url_mangeAttention="http://"+ip+":8080/manage-attentions";
//=======
//    private String IP="http://10.234.184.71:8080";
//    String url_getInformation=IP+"/get-information";
//    String url_mangeAttention=IP+"/manage-attentions";

//<<<<<<< Updated upstream
//
//    String ip="10.234.184.71";
//    String url_getInformation="http://"+ip+":8080/get-information";
//    String url_mangeAttention="http://"+ip+":8080/manage-attentions";


//    String url_getInformation="http://10.234.184.71:8080/get-information";
//    String url_mangeAttention="http://10.234.184.71:8080/manage-attentions";

    String myName= BodyActivity.key_username;
//=======
//>>>>>>> Stashed changes

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.peopledata);
        Init();
        Intent intent=getIntent();
       String Name=intent.getStringExtra("Name");
        String flag=intent.getStringExtra("flag");


        //关注列表触发
        if(flag.equals("1")){
            btn_focus=findViewById(R.id.btn_focus);
            btn_focus.setText("取消关注");
        }

        Log.d("Tag","搜索的人是"+Name);

        httprequest.handler=handler;
        try {
            httprequest.getRequest(url_getInformation,"username="+Name,1);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //控件初始化
    void Init(){

        tv_nick=findViewById(R.id.tv_nick_show);
        tv_gender=findViewById(R.id.tv_gender_show);
        tv_addr=findViewById(R.id.tv_addr_show);

        btn_focus=findViewById(R.id.btn_focus);
        btn_watch=findViewById(R.id.btn_watch_moment);
        return_iconButton=findViewById(R.id.imageButton_return_icon);
        image_head=findViewById(R.id.image_person_head);
        image_background=findViewById(R.id.image_background_show);



    }



    //为查看空间按钮添加响应事件
    public void WatchMoment(View view){
        btn_watch=findViewById(R.id.btn_watch_moment);
        //跳转到个人空间
        /*
         **********************
         */

    }


    //"<"箭头响应，回到关注页面
    public void Return_Last_Page(View view) {
            Intent intent=new Intent();
            Log.e("!!!!!", " 执行了返回intent");
            setResult(105,intent);
            this.finish();

    }



    //为关注按钮添加响应事件
    public void PayAttentionTo(View view) throws JSONException {
        btn_focus=findViewById(R.id.btn_focus);
        focusText=btn_focus.getText().toString();
        tv_nick=findViewById(R.id.tv_nick_show);

        ToJson json=new ToJson();
        //JSONObject jsonObject;
        String targetname=tv_nick.getText().toString();
        Log.d("Tag","====targername==="+targetname);

        //这里要取得自己的昵称
        String username=myName;

        //flag:1-添加关注    0-取消关注
        int flag;
        String jsonString;

        //添加关注---->已关注
        if(focusText.equals("添加关注")){
            flag=1;
            jsonString=json.setJsonObject(username,targetname,flag).toString();

            //向后台发送数据，此人的nick写入数据库
            //post:myName,targetName
            httprequest.post(url_mangeAttention,jsonString);
            //点击“添加关注”，按钮变为“已关注”
            btn_focus.setText("取消关注");
            Log.d("Tag","关注成功==="+jsonString);

        }

        if(focusText.equals("取消关注")){
            flag=0;
            jsonString=json.setJsonObject(username,targetname,flag).toString();
            btn_focus.setText("添加关注");
            //向后台发送数据，删除一条数据
            httprequest.post(url_mangeAttention,jsonString);
            Log.d("Tag","取消关注成功==="+jsonString);

        }


    }

}


