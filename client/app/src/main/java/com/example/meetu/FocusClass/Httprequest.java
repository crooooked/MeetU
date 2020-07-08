package com.example.meetu.FocusClass;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Httprequest {


    public Handler handler=new Handler();

    int GETList=5;




    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    /*
   异步get请求
     */
    //flag:0-关注列表get   1-搜索get & 关注列表进个人资料get
    public void getRequest(String url, String name, final int flag) throws IOException {

        String requestUrl= String.format("%s?%s",url,name);

        //要有一个客户端，类似于要有一个浏览器
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        //创建请求内容
        final Request request = new Request.Builder()
                .get()
                .url(requestUrl)
                .build();
        //用client去创建请求任务
        okhttp3.Call task=client.newCall(request);
        //异步请求，发起请求可以继续做其他事情（同步请求，发起请求之后要等数据回来）
        task.enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

                Log.d("TAG","onFailure->"+e.toString());
            }
            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                Message message=handler.obtainMessage();
                String json=response.body().string();   //json格式数据转换为String类型

                message.obj=json;
                if(flag==0){
                    //关注列表的get
                    message.what=0;
                }

                if(flag==1){
                    //搜索的get
                    message.what=1;
                }

                if (flag==2){
                    message.what=2;
                }

                handler.sendMessage(message);

                Log.d("TAG","onSuccess->"+json);

                Log.d("TAG","getcode->"+response.code());



            }
        });


    }




    /*
    关注列表，听众列表的get
     */
    //flag=3
//    public void getListRequset(String url,String name){
//        String requestUrl= String.format("%s?%s",url,name);
//        OkHttpClient client = new OkHttpClient.Builder()
//                .build();
//        final Request request = new Request.Builder()
//                .get()
//                .url(requestUrl)
//                .build();
//        okhttp3.Call task=client.newCall(request);
//        task.enqueue(new Callback() {
//            @Override
//            public void onFailure(okhttp3.Call call, IOException e) {
//                Log.d("TAG","onListFailure->"+e.toString());
//            }
//            @Override
//            public void onResponse(okhttp3.Call call, Response response) throws IOException {
//                Message message=handler.obtainMessage();
//                String json=response.body().string();   //json格式数据转换为String类型
//                message.obj=json;
//                message.what=GETList;
//                handler.sendMessage(message);
//            }
//        });
//    }


    /*
    异步post请求
     */

    //
    public void post(String url, String json){
        //先有Client
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        //创建一个RequestBody对象
        RequestBody body = RequestBody.create(JSON,json);

        //创建请求
        Request request=new Request.Builder()
                .url(url)
                .post(body)
                .build();
        //创建请求任务
        okhttp3.Call task=client.newCall(request);
        task.enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("Tag","失败========"+call);
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                ResponseBody body=response.body();
                Log.d("TAG","onSuccess->"+body.string());
            }
        });
    }

}







