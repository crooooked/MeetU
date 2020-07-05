package com.example.meetu.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.meetu.Activities.BodyActivity;
import com.example.meetu.Entities.Convert;
import com.example.meetu.Entities.User;
import com.example.meetu.Layouts.InformationActivity;
import com.example.meetu.Tools.OkHttpUtils;
import com.example.meetu.R;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PersonalFragment() {
        // Required empty public constructor
    }
     /* @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalFragment.
     */
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
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    private TextView tvName;
    private TextView tvAttention;
    private TextView tvFollow;
    private TextView tvPersonalSpace;
    private TextView tvSetting;
    private ImageView ivBgImage;
    private ImageView ivHeadImage;
    private User user;
    private ImageButton ibInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

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

//        initToolbar(toolbar,"个人信息",true);

        return view;
    }
    public void initToolbar(Toolbar toolbar, String title, boolean isDisplayHomeAsUp) {
        AppCompatActivity appCompatActivity= (AppCompatActivity) getActivity();
        assert appCompatActivity != null;
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(isDisplayHomeAsUp);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String username=BodyActivity.key_username;
//        String u="cql";
//        tvName.setText(u);

        String url="http://10.234.184.24:8080/get-information?username="+username;
//        final String json= Convert.strJsonObject("username",username);
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

        //添加点击事件，进入修改信息页面
        ibInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), InformationActivity.class);
                intent.putExtra("user",user);
                startActivityForResult(intent, CHANGE_INFORMATION);
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
        }
    }

    //设置的是
    private void setView(){
        tvName.setText(user.getUsername());
        String bgUrl=user.getBackground_url();
        String hdUrl=user.getHead_url();

        if(!bgUrl.equals("null")){
            Glide.with(getContext()).load(user.getBackground_url()).into(ivBgImage);
        }
        if(!hdUrl.equals("null")){
            Glide.with(getContext()).load(user.getHead_url()).into(ivHeadImage);
        }
    }


}