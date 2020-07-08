package com.example.meetu.Fragments;

import android.content.Intent;
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
import android.widget.TextView;

import com.example.meetu.Activities.BodyActivity;
import com.example.meetu.Activities.LoginActivity;
import com.example.meetu.Activities.ReleaseContentActivity;
import com.example.meetu.Entities.Content;
import com.example.meetu.Entities.User;
import com.example.meetu.Layouts.ContentCard;
import com.example.meetu.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalSpaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalSpaceFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PersonalSpaceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalSpaceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalSpaceFragment newInstance(String param1, String param2) {
        PersonalSpaceFragment fragment = new PersonalSpaceFragment();
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


    ImageView returnBtrn;
    int myId = BodyActivity.key_id;
    String IP = LoginActivity.ip;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_personal_space, container, false);

        LinearLayout layout = view.findViewById(R.id.linear_layout_personal_space);




        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Intent intent = getActivity().getIntent();

        boolean getMyStateOnly = intent.getBooleanExtra("isPersonalSpace", true);

        int ID = intent.getIntExtra("id", this.myId);

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

            TextView textView = view.findViewById(R.id.zone_head_user_personal_name);
            textView.setText(BodyActivity.key_username);

            String head_url = json.getString("head");
            String background_url = json.getString("background");
            User user = new User(ID);
            user.setHead_url(head_url);
            user.setBackground_url(background_url);
            user.getHeadImage();
            user.getBackgroundImage();

            ImageView head = view.findViewById(R.id.zone_head_personal_image);
            head.setImageBitmap(user.getHead_image());
            ImageView background = view.findViewById(R.id.background_personal_image);
            background.setImageBitmap(user.getBackground_image());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        //获取要显示的状态列表
        client = new OkHttpClient();
        JSONObject getStatePostJson = new JSONObject();
        url = "http://" + IP + ":8080/get-state-list";
        Log.i("url", url);
        Response response = null;
        String res = new String();
        try {
            getStatePostJson.put("begin_time", null);
            getStatePostJson.put("end_time", null);
            getStatePostJson.put("uid", ID);
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

//        //逐个显示卡片
//        try {
//            JSONArray cardsArray = new JSONArray(res);
//            for (int i = 0; i < cardsArray.length(); i++) {
//                int id = cardsArray.getJSONObject(i).getInt("content_id");
//                Log.i("content_id", ""+id);
//                Content content = new Content(getContext(), id, this);
//                ContentCard card = new ContentCard(getContext(), content);
//
//                layout.addView(card);
//                Log.i("addView", "success");
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //添加动态
        ImageButton btnRelease=view.findViewById(R.id.new_repost_personal_button);
        btnRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.new_repost_personal_button){
                    startActivityForResult(new Intent(getActivity(), ReleaseContentActivity.class),REQUEST_CODE);
                }
            }
        });
        return view;
    }




    private final int REQUEST_CODE=130;
}