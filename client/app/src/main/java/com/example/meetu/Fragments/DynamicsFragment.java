package com.example.meetu.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.meetu.Entities.Content;
import com.example.meetu.Entities.User;
import com.example.meetu.Layouts.ContentCard;
import com.example.meetu.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DynamicsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DynamicsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int myId = 2;
    String IP = "10.236.66.58";

    public DynamicsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DynamicsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DynamicsFragment newInstance(String param1, String param2) {
        DynamicsFragment fragment = new DynamicsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dynamics, container, false);
        LinearLayout layout = view.findViewById(R.id.linear_layout_space);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //显示空间头
        OkHttpClient client = new OkHttpClient();
        String url = "http://" + IP + ":8080/get-zone-head?uid=" + myId;
        Log.i("url", url);
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String str = response.body().string();
            Log.i("zone_head_res", str);

            JSONObject json = new JSONObject(str);

            TextView textView = view.findViewById(R.id.zone_head_user_name);
            textView.setText(json.getString("username"));

            String head_url = json.getString("head");
            String background_url = json.getString("background");
            User user = new User(myId);
            user.setHead_url(head_url);
            user.setBackground_url(background_url);
            user.getHeadImage();
            user.getBackgroundImage();

            ImageView head = view.findViewById(R.id.zone_head_image);
            head.setImageBitmap(user.getHead_image());
            ImageView background = view.findViewById(R.id.background_image);
            background.setImageBitmap(user.getBackground_image());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        //获取要显示的状态列表
        boolean getMyStateOnly = true;
        client = new OkHttpClient();
        JSONObject getStatePostJson = new JSONObject();
        url = "http://" + IP + ":8080/get-state-list";
        Log.i("url", url);
        Response response = null;
        String res = new String();
        try {
            getStatePostJson.put("begin_time", null);
            getStatePostJson.put("end_time", null);
            getStatePostJson.put("uid", myId);
            getStatePostJson.put("get_friends", !getMyStateOnly);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, getStatePostJson.toString());
        request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            response = client.newCall(request).execute();
            res = response.body().string();
            Log.i("state_res", res);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //逐个显示卡片
        try {
            JSONArray cardsArray = new JSONArray(res);
            for (int i = 0; i < cardsArray.length(); i++) {
                int id = cardsArray.getJSONObject(i).getInt("content_id");
                Log.i("content_id", ""+id);
                Content content = new Content(getContext(), id);
                layout.addView(new ContentCard(getContext(), content));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (true) return view;

        //测试用的数据
        Bitmap head = BitmapFactory.decodeResource(getResources(), R.mipmap.sample_head);
        //9张图
        ArrayList<Bitmap> images = new ArrayList<>();
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image1));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image2));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image3));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image4));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image5));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image6));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image7));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image8));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image9));
        //评论
        String[] remark_content = new String[]{"好的！", "知道了"};
        String[] remark_username = new String[]{"小A", "小B"};

        return view;
    }


//        //测试无图片+无转发的状态
//        Content content1 = new Content(head, null);
//        layout.addView(new ContentCard(getContext(), content1));
//
//        //测试有图片+无转发的状态
//        Content content2 = new Content(head, null);
//        content2.setImages(images);
//        ContentCard contentCard2 = new ContentCard(getContext(), content2);
//        layout.addView(contentCard2);
//
//        //测试无图片+有转发的状态
//        Content content3 = new Content(head, null);
//        content3.setRepost(123);
//        content3.setRepostContent(content1);
//        layout.addView(new ContentCard(getContext(), content3));
//
//        //测试有图片+有评论的状态
//        Content content4 = new Content(head, null);
//        content4.setImages(images);
//        content4.setRemarks_content(remark_content);
//        content4.setRemarks_username(remark_username);
//        ContentCard contentCard4 = new ContentCard(getContext(), content4);
//        layout.addView(contentCard4);
//
//        //测试从网络获取content
//        Content content5 = null;
//        try {
//            content5 = new Content(getContext(), 1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        layout.addView(new ContentCard(getContext(), content5));
//
//        return view;
//
//
//    }

}