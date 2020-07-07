package com.example.meetu.Fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.meetu.Activities.BodyActivity;
import com.example.meetu.Activities.LoginActivity;
import com.example.meetu.Activities.MainActivity;
import com.example.meetu.Entities.Convert;
import com.example.meetu.Entities.User;
import com.example.meetu.Layouts.InformationActivity;
import com.example.meetu.Tools.OkHttpUtils;
import com.example.meetu.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int CHANGE_INFORMATION=103;
    private static final int MAIN_ACTIVITY=106;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PersonalFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static PersonalFragment newInstance(String param1, String param2) {
        PersonalFragment fragment = new PersonalFragment();
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

    //自定义页面
    private TextView tvName;
    private TextView tvAttention;
    private TextView tvFollow;
    private TextView tvPersonalSpace;
    private TextView tvSetting;
    private ImageView ivBgImage;
    private ImageView ivHeadImage;
    private User user;
    private ImageButton ibInfo;
    //页面初始化
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view=inflater.inflate(R.layout.fragment_personal, container, false);
        tvName=view.findViewById(R.id.tv_name);
        tvAttention=view.findViewById(R.id.tv_attention);
        tvFollow=view.findViewById(R.id.tv_audience);
        tvPersonalSpace=view.findViewById(R.id.tv_personal_space);
        tvSetting=view.findViewById(R.id.tv_setting);
        ivBgImage=view.findViewById(R.id.iv_bg_mine);
        ivHeadImage=view.findViewById(R.id.iv_head_mine);
        ibInfo=view.findViewById(R.id.ib_info);

        return view;
    }

    //需要开线程或者非动态
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String username=BodyActivity.key_username;

        setImage(username);
        setCount(username);

        //添加点击事件，进入修改信息页面
        ibInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), InformationActivity.class);
                intent.putExtra("user",user);
                startActivityForResult(intent, CHANGE_INFORMATION);
            }
        });
        //跳转到我的主页
        tvPersonalSpace.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivityForResult(intent, MAIN_ACTIVITY);
            }
        });

        //退出
        tvSetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
    //设置图片
    private void setCount(String username) {
        String url="http://172.20.10.2:8080/get-number?username="+username;
        OkHttpUtils instance=OkHttpUtils.getInstance();
        instance.doGet(url, new OkHttpUtils.OkHttpCallBackLinener() {
            @Override
            public void failure(Exception e) {
                //Toast.makeText(getContext(),"访问失败！",Toast.LENGTH_LONG).show();
            }

            @Override
            public void success(String json) {
                try {
                    JSONObject jsonObject=new JSONObject(json);
                    String attention=jsonObject.getString("attention")+"关注";
                    String audience=jsonObject.getString("audience")+"听众";

                    tvAttention.setText(attention);
                    tvFollow.setText(audience);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    //设置关注听众数量
    private void setImage(String username){
        String url="http://10.236.66.58:8080/get-information?username="+username;
        OkHttpUtils instance=OkHttpUtils.getInstance();
        instance.doGet(url, new OkHttpUtils.OkHttpCallBackLinener() {
            @Override
            public void failure(Exception e) {
                Toast.makeText(getContext(),"此人不存在",Toast.LENGTH_LONG).show();
            }
            @Override
            public void success(String json) {
                user= Convert.getUserFromStr(json);
                setView();
            }
        });



    }

    //从修改信息页面返回
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode==CHANGE_INFORMATION)&&(resultCode==104)){
            assert data != null;
            user=data.getParcelableExtra("user");
            setView(data.getStringExtra("head"),data.getStringExtra("background"));
        }
    }

    //设置的是初始化图片
    private void setView(){
        tvName.setText(user.getUsername());
        String bgUrl=user.getBackground_url();
        String hdUrl=user.getHead_url();

//        if(!bgUrl.equals("null")){
//            Glide.with(getContext()).load(user.getBackground_url()).into(ivBgImage);
//        }
//        if(!hdUrl.equals("null")){
//            Glide.with(getContext()).load(user.getHead_url()).into(ivHeadImage);
//        }
    }
    //设置的是更新后的图片
    private void setView(String mParam1,String mParam2){
        if(mParam1!=null){
            Glide.with(getActivity()).load(new File(mParam1)).into(ivHeadImage);
        }
        if(mParam2!=null){
            Glide.with(getActivity()).load(new File(mParam1)).into(ivBgImage);
        }
    }
}