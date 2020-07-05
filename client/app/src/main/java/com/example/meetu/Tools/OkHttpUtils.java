package com.example.meetu.Tools;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;

import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtils {
    private static final String OK_RESPONSE="200";
    private static final String FAIL_RESPONSE="400";

    private final Handler mHandler;
    private final OkHttpClient mClient;
    //声明类的变量
    private static volatile OkHttpUtils sOkHttpUtils;

    //私有构造
    private OkHttpUtils() {
        mHandler = new Handler(Looper.getMainLooper());
        mClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    //暴露外界访问的方法,双层锁
    public static OkHttpUtils getInstance(){
        if(sOkHttpUtils == null){
            synchronized (OkHttpUtils.class){
                if(sOkHttpUtils == null){
                    //创建对象
                    sOkHttpUtils = new OkHttpUtils();
                }
            }
        }
        //如果不等于空直接返回对象,符合单例的概念
        return sOkHttpUtils;
    }
    //创建接口
    public interface OkHttpCallBackLinener{
        void failure(Exception e);
//        void failure();
        void success(String json);
    }

    private static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    //封装post参数定义三个
    public void doPost(String path, String jsonStr , final OkHttpCallBackLinener callBackLinener ){
        //完成请求体的创建
        RequestBody requestBody=RequestBody.create(jsonStr,JSON);

        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();

        Call call = mClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                //更新到主线程
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBackLinener.failure(e);
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //保险
                if(response != null & response.isSuccessful()){
                    final String json = response.body().string();
                    if(callBackLinener != null){
                        //切换到主线程更新
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                    callBackLinener.success(json);
                            }
                        });
                    }
                }
            }
        });
    }
    //封装get参数定义两个,一个是URL网址   一个实现接口的对象
    public void doGet(String path, final OkHttpCallBackLinener callBack){
        Request request = new Request.Builder()
                .get()
                .url(path)
                .build();
        Call call = mClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                //切换到主线程
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.failure(e);
                    }
                });
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                final String json = response.body().string();
                if(response != null){
                    //切换到主线程
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.success(json);
                        }
                    });
                }
            }
        });
    }
    //封装图片传输的函数
    //url网络地址
    //filepath文件路径
    private static final MediaType IMAGE
            =MediaType.get("image/*");
    public void postImage(String url, File file,String username, final OkHttpCallBackLinener callback){
            //构造表单数据
            RequestBody fileBody=RequestBody.create(file,IMAGE);

            RequestBody requestBody=new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file","file",fileBody)
                    .addFormDataPart("username",username)
                    .build();

            final Request request=new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            Call call=mClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.failure(e);
                        }
                    });
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String json = response.body().string();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.success(json);
                            }
                        });
                    }
                }
            });


    }


}
