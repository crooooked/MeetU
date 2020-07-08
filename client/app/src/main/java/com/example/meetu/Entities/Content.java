package com.example.meetu.Entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.meetu.Activities.BodyActivity;
import com.example.meetu.Activities.LoginActivity;
import com.example.meetu.Fragments.DynamicsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Content {
    final String IP = LoginActivity.ip;
    public final int NO_REPOST = -1;

    int myId = BodyActivity.key_id;

    int content_id;     //id
    int uid;            //发布者id
    User user;          //发布者信息（通过uid获取）
    long time;           //发布时间戳
    String content;     //状态内容
    int repost;         //转发，不是转发则为NO_REPOST，是转发则为被转发状态的content_id
    Content repostContent;  //转发内容（通过repost获取）
    String[] image_urls;    //图片的url，没有图片则为null
    ArrayList<Bitmap> images;    //图片，没有则为null（通过image_urls获取）
    String[] remarks_username;
    String[] remarks_content;
    int like_times;
    int remark_times;
    int repost_times;

    DynamicsFragment fragment;

    public Content(Bitmap head, Bitmap background) {
        content_id = 0;
        uid = 0;
        time = 1593764071000L;
        content = "今天吃了米线，很开心！\n还和朋友逛街买了一个玩偶。";
        repost = -1;
        user = new User(head, background);
    }
    public Content(int content_id) {
        this.content_id = content_id;
    }

    //通过网络获取Content
    public Content(Context context, int content_id, DynamicsFragment fragment) throws IOException {
        this.content_id = content_id;
        this.fragment = fragment;
        if(this.fragment == null)
            Log.i("fragment", "null");
        String url = "http://" + IP + ":8080/get-specific-state?content_id="+content_id;
        init(url, false);
        if(repost != NO_REPOST) {
            Log.i("repost", "" + repost);
            repostContent = new Content(repost);
            repostContent.setFragment(fragment);
            String repost_url = "http://" + IP + ":8080/get-simple-state?content_id="+repost;
            repostContent.init(repost_url, true);
        }
    }

    public void init(String url, boolean isSimple) throws IOException {
        //获取Content
        OkHttpClient client = new OkHttpClient();
        Log.i("url", url);
        Request request = new Request.Builder()
                .get()
                .url(url)
                //.header("content_id", ""+content_id)
                .build();
        Response response = client.newCall(request).execute();
        String str = response.body().string();
        Log.i("res", str);
        //解析JSON
        try {
            JSONObject res = new JSONObject(str);
            content = res.getString("content");
            //Log.i("content", content);

            //poster
            JSONObject poster = res.getJSONObject("poster");
            uid = poster.getInt("uid");
            user = new User(uid);
            user.setUsername(poster.getString("username"));
            user.setHead_url(poster.getString("head"));

            //images
            JSONArray image_list = res.getJSONArray("images");
            Log.i("image_list", image_list.toString());
            image_urls = new String[image_list.length()];
            images = new ArrayList<Bitmap>();
            Log.i("image_list_length", image_list.length()+"");
            for (int i = 0; i < image_list.length(); i++) {
                image_urls[i] = image_list.getJSONObject(i).getString("image");
                Log.i("image"+i, image_urls[i]);

                request = new Request.Builder()
                        .url(image_urls[i])
                        .build();
                response = client.newCall(request).execute();
                byte[] bytes = response.body().bytes();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                images.add(bitmap);
            }
            Log.i("myid", ""+myId);
            if(user.getUid() == myId)
                user.setHead_image(fragment.getMyHead());
            else
                user.getHeadImage();
            //remarks
            //获取简单状态时不解析此项
            if(!isSimple) {
                JSONArray remark_list = res.getJSONArray("remarks");
                if (remark_list != null && remark_list.length() != 0) {
                    remarks_username = new String[remark_list.length()];
                    remarks_content = new String[remark_list.length()];
                    Log.i("remarks", remark_list.toString());
                    for (int i = 0; i < remark_list.length(); i++) {
                        remarks_username[i] = remark_list.getJSONObject(i).getString("username");
//                        Log.i("remark_username", remarks_username[i]);
                        remarks_content[i] = remark_list.getJSONObject(i).getString("remark");
//                        Log.i("remark_content", remarks_content[i]);
                    }
                }
            }

            repost = res.getInt("repost");

            //获取简单状态时不解析此项
            if(!isSimple) {
                time = res.getInt("time");
                time = res.getLong("time");
                Log.i("time", time+"");
                like_times = res.getInt("like_times");
                remark_times = res.getInt("remark_times");
                repost_times = res.getInt("repost_times");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //点赞
    public boolean like() {
        String url = "http://"+IP+":8080/like";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("content_id", ""+content_id);
            jsonObject.put("uid", ""+myId);
            jsonObject.put("flag", "true");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            Log.i("like_response", response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //评论
    public void remark(String text) {
        String url = "http://"+IP+":8080/remark";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("content_id", ""+content_id);
            jsonObject.put("uid", ""+myId);
            jsonObject.put("content", text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            Log.i("Remark_res", response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //转发
    public void repost(String text) {
        String url = "http://"+IP+":8080/repost-state";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("content_id", ""+content_id);
            jsonObject.put("uid", ""+myId);
            jsonObject.put("content", text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            Log.i("Repost_res", response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFragment(DynamicsFragment fragment) {
        this.fragment = fragment;
    }

    public int getContent_id() {
        return content_id;
    }

    public void setContent_id(int content_id) {
        this.content_id = content_id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRepost() {
        return repost;
    }

    public void setRepost(int repost) {
        this.repost = repost;
    }

    public Content getRepostContent() {
        return repostContent;
    }

    public void setRepostContent(Content repostContent) {
        this.repostContent = repostContent;
    }

    public String[] getImage_urls() {
        return image_urls;
    }

    public void setImage_urls(String[] image_urls) {
        this.image_urls = image_urls;
    }

    public ArrayList<Bitmap> getImages() {
        return images;
    }

    public void setImages(ArrayList<Bitmap> images) {
        this.images = images;
    }

    public String getIP() {
        return IP;
    }

    public int getNO_REPOST() {
        return NO_REPOST;
    }

    public String[] getRemarks_username() {
        return remarks_username;
    }

    public void setRemarks_username(String[] remarks_username) {
        this.remarks_username = remarks_username;
    }

    public String[] getRemarks_content() {
        return remarks_content;
    }

    public void setRemarks_content(String[] remarks_content) {
        this.remarks_content = remarks_content;
    }

    public int getLike_times() {
        return like_times;
    }

    public void setLike_times(int like_times) {
        this.like_times = like_times;
    }

    public int getRemark_times() {
        return remark_times;
    }

    public void setRemark_times(int remark_times) {
        this.remark_times = remark_times;
    }

    public int getRepost_times() {
        return repost_times;
    }

    public void setRepost_times(int repost_times) {
        this.repost_times = repost_times;
    }
}
