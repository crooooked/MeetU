package com.example.meetu.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.meetu.Activities.BodyActivity;
import com.example.meetu.Activities.LoginActivity;
import com.example.meetu.Activities.MainActivity;
import com.example.meetu.Entities.Convert;
import com.example.meetu.Entities.User;
import com.example.meetu.Layouts.InformationActivity;
import com.example.meetu.Tools.GlideCircleTransform;
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

    private static final int CHANGE_INFORMATION=111;
    private static final int MAIN_ACTIVITY=160;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String ip=LoginActivity.ip;
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
        ivBgImage=view.findViewById(R.id.image_bg_mine);
        ivHeadImage=view.findViewById(R.id.image_head_mine);
        ibInfo=view.findViewById(R.id.ib_info);

        final String username=BodyActivity.key_username;
        setImage(username);
        setCount(username);
        return view;
    }
    //需要开线程或者非动态
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final String username=BodyActivity.key_username;
        //添加点击事件，进入修改信息页面
        ibInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), InformationActivity.class);
                intent.putExtra("username",username);
                startActivityForResult(intent, CHANGE_INFORMATION);
            }
        });
        //跳转到我的主页
        tvPersonalSpace.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("isPersonalSpace",true);
                startActivityForResult(intent, MAIN_ACTIVITY);
            }
        });
        //退出
        tvSetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog=new AlertDialog.Builder(getActivity())
                        .setTitle("是否退出？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(getActivity(),LoginActivity.class));
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        }).create();
                alertDialog.show();
            }
        });
    }
    //设置关注听众数量
    private void setCount(String username) {
//<<<<<<< Updated upstream
//        String url="http://"+ip+":8080/get-number?username="+username;
//=======
        String url="/get-number?username="+username;
//
//>>>>>>> Stashed changes
        OkHttpUtils instance=OkHttpUtils.getInstance();
        instance.doGet(url, new OkHttpUtils.OkHttpCallBackLinener() {
            @Override
            public void failure(Exception e) {
            }
            @Override
            public void success(String json) {
                try {
                    JSONObject jsonObject=new JSONObject(json);
                    String attention=jsonObject.getInt("follower")+"\n关注";
                    String audience=jsonObject.getInt("audience")+"\n听众";

                    tvAttention.setText(attention);
                    tvFollow.setText(audience);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    //设置图片
    private void setImage(String username){

        String url="/get-information?username="+username;

        OkHttpUtils instance=OkHttpUtils.getInstance();
        instance.doGet(url, new OkHttpUtils.OkHttpCallBackLinener() {
            @Override
            public void failure(Exception e) {
                Toast.makeText(getContext(),"此人不存在",Toast.LENGTH_LONG).show();
            }
            @Override
            public void success(String json) {
                Log.e("!!!!", json );
                user= Convert.getUserFromStr(json);
                setView();
            }
        });
    }

    //从修改信息页面返回
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case CHANGE_INFORMATION:
                if(resultCode==112){
//                    setView();
                }
                break;

            case MAIN_ACTIVITY:
                if(resultCode==110){
                    onResume();
                }
                break;
        }
    }

    private boolean isFirstLoading=true;
    @Override
    public void onResume() {
        super.onResume();
        if(!isFirstLoading){
            setView();
        }
        isFirstLoading=false;
    }

    //设置的是初始化图片
    private void setView(){
        tvName.setText(user.getUsername());
        String bgUrl=user.getBackground_url();
        String hdUrl=user.getHead_url();
//<<<<<<< Updated upstream
//
//        Glide.get(getContext()).clearMemory();
//        Glide.get(getContext()).clearDiskCache();
        if(bgUrl!=null){
            Glide.with(getContext()).load(bgUrl).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(ivBgImage);
        }
        if(hdUrl!=null){
            Glide.with(getContext()).load(hdUrl).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).transform(new GlideCircleTransform(getContext())).into(ivHeadImage);
        }
    }
}