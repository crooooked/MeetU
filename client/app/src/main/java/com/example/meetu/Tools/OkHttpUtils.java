package com.example.meetu.Tools;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.meetu.Activities.BodyActivity;
import com.example.meetu.Activities.LoginActivity;
import com.example.meetu.FocusClass.IP;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OkHttpUtils {
    private static final String OK_RESPONSE="200";
    private static final String FAIL_RESPONSE="400";

    private String IP="http://"+ LoginActivity.ip +":8080";

    private final Handler mHandler;
    private final OkHttpClient mClient;
    //声明类的变量
    private static volatile OkHttpUtils sOkHttpUtils;

    //私有构造
    private OkHttpUtils() {
        mHandler = new Handler(Looper.getMainLooper());
        mClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(10, TimeUnit.MILLISECONDS)
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
    public void doPost(String url_tail, String jsonStr , final OkHttpCallBackLinener callBackLinener ){
        //完成请求体的创建
        String path=IP+url_tail;
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
    public void doGet(String url_tail, final OkHttpCallBackLinener callBack){
        String path=IP+url_tail;
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
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {

                final String json = Objects.requireNonNull(response.body()).string();
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
            =MediaType.parse("image/*");
    private static final MediaType STREAM
            =MediaType.parse("application/octet-stream");
    public void postImage(String url_tail, String filepath,String username, final OkHttpCallBackLinener callback) throws IOException {
            //构造表单数据
            String url=IP+url_tail;
            File file=new File(filepath);
            if(!file.exists()){
                file.createNewFile();
            }
            RequestBody fileBody=RequestBody.create(file,IMAGE);
            RequestBody requestBody=new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file",file.getName(),fileBody)
                    .addFormDataPart("username",username)
                    .build();
        Log.e("!!!!!", file.getPath());
            final Request request=new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .header("Content-Type","multipart/form-data")
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
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String json = Objects.requireNonNull(response.body()).string();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.success(json);
                            }
                        });
                    }
                }
            });

        Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);
    }

    public void postImage(String url_tail, String filepath, final OkHttpCallBackLinener callback) throws IOException {
        //构造表单数据
        String url=IP+url_tail;
        File file=new File(filepath);
        if(!file.exists()){
            file.createNewFile();
        }
        RequestBody fileBody=RequestBody.create(file,IMAGE);
        RequestBody requestBody=new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file",file.getName(),fileBody)
                .build();
        Log.e("!!!!!", file.getPath());
        final Request request=new Request.Builder()
                .url(url)
                .post(requestBody)
                .header("Content-Type","multipart/form-data")
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
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String json = Objects.requireNonNull(response.body()).string();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.success(json);
                        }
                    });
                }
            }
        });

        Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);
    }
}
