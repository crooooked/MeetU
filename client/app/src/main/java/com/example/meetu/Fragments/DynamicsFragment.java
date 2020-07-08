package com.example.meetu.Fragments;

import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.meetu.Activities.BodyActivity;
import com.example.meetu.Activities.LoginActivity;
import com.example.meetu.Activities.NewContentActivity;
import com.example.meetu.Activities.ReleaseContentActivity;
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

    int myId = BodyActivity.key_id;
    String IP = LoginActivity.ip;

    boolean getMyStateOnly=false;
    int ID;
    long lastTime;
    View view;
    LinearLayout layout;

    Bitmap myHead;

    public DynamicsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment DynamicsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DynamicsFragment newInstance(boolean param1) {
        DynamicsFragment fragment = new DynamicsFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            getMyStateOnly = getArguments().getBoolean(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dynamics, container, false);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Intent intent = getActivity().getIntent();
//        getMyStateOnly = intent.getBooleanExtra("isPersonalSpace", true);
        ID = intent.getIntExtra("id", this.myId);

        //显示空间头
        OkHttpClient client = new OkHttpClient();
        String url = "http://" + IP + ":8080/get-zone-head?uid=" + ID;
        Log.i("uid",""+ID);
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
            textView.setText(BodyActivity.key_username);

            String head_url = json.getString("head");
            String background_url = json.getString("background");
            User user = new User(ID);
            user.setHead_url(head_url);
            user.setBackground_url(background_url);
            user.getHeadImage();
            user.getBackgroundImage();

            ImageView head = view.findViewById(R.id.zone_head_image);
            myHead = user.getHead_image();
            if(myHead == null)
                Log.i("myHead", "null");
            head.setImageBitmap(myHead);

            ImageView background = view.findViewById(R.id.background_image);
            background.setImageBitmap(user.getBackground_image());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        //添加动态
        ImageButton btnRelease=view.findViewById(R.id.new_repost_button);
        btnRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.new_repost_button){
                    startActivityForResult(new Intent(getActivity(), ReleaseContentActivity.class),REQUEST_CODE);
                }
            }
        });

        load(0);
        return view;
    }

    //加载和刷新
    public void load(long beginTime) {
        layout = view.findViewById(R.id.linear_layout_space);

        //获取要显示的状态列表
        OkHttpClient client = new OkHttpClient();
        JSONObject getStatePostJson = new JSONObject();
        String url = "http://" + IP + ":8080/get-state-list";
        Log.i("url", url);
        Response response = null;
        String res = new String();
        try {
            getStatePostJson.put("end_time", null);
            if(beginTime == 0)
                getStatePostJson.put("begin_time", null);
            else
                getStatePostJson.put("begin_time", beginTime);
            getStatePostJson.put("uid", ID);
            getStatePostJson.put("get_friends", !getMyStateOnly);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, getStatePostJson.toString());
        Request request = new Request.Builder()
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
                Content content = new Content(getContext(), id, this);

                if(i == 0)
                    lastTime = content.getTime();
                ContentCard card = new ContentCard(getContext(), content);
                card.setfragment(this);

                layout.addView(card, 1+i);
                Log.i("addView", "success");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fresh() {
        load(lastTime+100);
        ScrollView scrollView = view.findViewById(R.id.scroll_view);
        scrollView.fullScroll(View.FOCUS_UP);
    }

    private final int REQUEST_CODE=130;
    private final int RESULT_CODE=131;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE){
            switch (resultCode){
                case 141:
                    fresh();
//                    onResume();
                    break;
                case 110:
                    break;

            }
        }

    }

    public Bitmap getMyHead() {
        return myHead;
    }

    //    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        ImageView addDynamics=
//    }

    //添加状态跳转
    public void newContent(View view) {
        Intent intent=new Intent(getActivity(), NewContentActivity.class);
        startActivity(intent);
    }

    public void test() {
    }
}