package com.example.meetu.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.meetu.Activities.BodyActivity;
import com.example.meetu.Activities.FocusListActivity;
import com.example.meetu.Activities.LoginActivity;
import com.example.meetu.Activities.PersonalDataShowActivity;
import com.example.meetu.Adapter.AttentionAdapter;
import com.example.meetu.FocusClass.AnalyseJson;
import com.example.meetu.FocusClass.FocusData;
import com.example.meetu.FocusClass.Httprequest;
import com.example.meetu.Layouts.InformationActivity;
import com.example.meetu.R;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;

import android.widget.TextView;

import android.widget.TabHost;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.meetu.Adapter.MyListAdapter;
import com.example.meetu.FocusClass.AnalyseJson;
import com.example.meetu.FocusClass.FocusData;
import com.example.meetu.FocusClass.Httprequest;
import com.example.meetu.R;
import com.example.meetu.Tools.GlideCircleTransform;
import com.google.android.material.tabs.TabLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AttentionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttentionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Button button;

    public SearchView searchView;
    public android.widget.TextView tv_nick, tv_gender, tv_addr;
    public Button btn_focus, btn_watch;
    public ImageButton return_iconButton;
    public ImageView image_head, image_background;
    public ImageView image_show;
    private static int PERSONAL_DATA=225;
    public LinearLayout pop_up_box;
    public ImageView search_image;
    public TextView search_name;
    public static String ip= LoginActivity.ip;
    Httprequest httprequest = new Httprequest();
    //httprequest.handler=handler;将Httprequest的handler与下面的handler关联在一起

    Bitmap bitmap;
    /*

    上面的nick[]应该向后端请求，读数据库，读出图片和名称，然后将数据放到nick[]中

     */

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@androidx.annotation.NonNull Message msg) {

            super.handleMessage(msg);
            int w = msg.what;
            String getMsg = msg.obj.toString();
            AnalyseJson analyseJson = new AnalyseJson();
            if (w==2){
                //json字符串转换为JSONObject对象
                Log.d("TAG","执行到了这里");
                JSONObject jsonObject=JSONObject.parseObject(getMsg);
                String headUrl;
                String username;
                username=analyseJson.getUsername(jsonObject);
                headUrl=analyseJson.getHeadUrl(jsonObject);
                Glide.with(getActivity()).load(headUrl)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .transform(new GlideCircleTransform(getActivity()))
                        .into(search_image);
                search_name.setText(username);
            }

        }


    };






    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private List<Fragment> mFragments;
    private List<String> mTitles;
    private AttentionAdapter mAdapter;

    public static List<String> list_attentionName=new ArrayList<>();

    public AttentionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AttentionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AttentionFragment newInstance(String param1, String param2) {
        AttentionFragment fragment = new AttentionFragment();
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



    /*
    控件初始化
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_attention, container, false);
        tv_nick = view.findViewById(R.id.tv_nick_show);
        tv_gender = view.findViewById(R.id.tv_gender_show);
        tv_addr = view.findViewById(R.id.tv_addr_show);
        image_head = view.findViewById(R.id.image_person_head);
        searchView = view.findViewById(R.id.search);
        searchView = view.findViewById(R.id.search);
        pop_up_box=view.findViewById(R.id.pop_up_box);
        search_image=view.findViewById(R.id.search_image);
        search_name=view.findViewById(R.id.search_name);

        initView(view);
        initData(view);
        setData();

        return view;
    }

    private  void  initData(View view){
        mTitles=new ArrayList<>();
        mTitles.add("关注");
        mTitles.add("听众");
        mFragments=new ArrayList<>();
        for(int i=0;i<mTitles.size();i++){
            if(i==0){
                mFragments.add(new ListAttentionFragment());
            }

            if(i==1){
                mFragments.add(new ListFansFragment());

            }
        }

        mAdapter=new AttentionAdapter(getChildFragmentManager(),mFragments,mTitles);
        mAdapter.notifyDataSetChanged();
    }

    private void initView(View view){
        mViewPager=(ViewPager)view.findViewById(R.id.vp_attentionpage_show);
        mTabLayout=view.findViewById(R.id.tl_attention_navigation);
    }

    private void setData(){
        mViewPager.setAdapter(mAdapter);
        //设置Viewpager和Tablayout进行联动
        mTabLayout.setupWithViewPager(mViewPager);

    }





    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Search();
            }
        });
        new ListAttentionFragment.ListViewAsyncTask();

    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==225){
            Log.e("返回intent","我返回到了爸爸那里");
            new ListAttentionFragment.ListViewAsyncTask().execute();
        }
    }




    //搜索框searchview
    public void Search() {
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //最终提交时触发
            @Override
            public boolean onQueryTextSubmit(String query) {
                //要判断一下，判断是否有这个用户
                //用Intent进行跳转
                Intent intent = new Intent(getContext(), PersonalDataShowActivity.class);
                //0：表示从搜索框到个人资料界面
                intent.putExtra("flag", "0");
                intent.putExtra("Name", query);
                startActivityForResult(intent,PERSONAL_DATA);
                return false;
            }

            //搜索框中文本改变时触发
            @Override
            public boolean onQueryTextChange(String newText) {
                if(searchView.getQuery().toString()!=""){
                    httprequest.handler = handler;
                    try {
                        pop_up_box.setVisibility(View.VISIBLE);
                        httprequest.getRequest("http://" + ip + ":8080/get-information", "username=" + searchView.getQuery().toString(), 2);
                        search_name.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view){
                                Intent intent = new Intent(getActivity(), PersonalDataShowActivity.class);
                                intent.putExtra("Name", searchView.getQuery().toString());
                                //1：表示已经关注,表示从listview点进去到个人资料界面
                                intent.putExtra("flag", "0");
                                startActivityForResult(intent, PERSONAL_DATA);
                                searchView.clearFocus();
                                int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
                                EditText  textView = (EditText ) searchView.findViewById(id);
                                textView.setText("");
                                pop_up_box.setVisibility(View.GONE);

                            }
                        });
                        System.out.println(searchView.getQuery().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }

        });
    }
    public static class ListAttentionFragment extends Fragment {
        static List<FocusData> focusdatas;
        static android.widget.ListView listView;
        static String  myName= BodyActivity.key_username;
        static String  url_getAttention= "http://"+AttentionFragment.ip+":8080/get-attentions?username=";



        public ListAttentionFragment() {
        }

        public ListAttentionFragment newInstance(String param1, String param2) {
            ListAttentionFragment fragment = new ListAttentionFragment();
            Bundle args = new Bundle();
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }



        /*
        控件初始化
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            setHasOptionsMenu(true);


            View view = inflater.inflate(R.layout.view_attention, container, false);

            listView = view.findViewById(R.id.listView_focus);
            return view;
        }




        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), PersonalDataShowActivity.class);
                    intent.putExtra("Name", focusdatas.get(position).nick);
                    //1：表示已经关注,表示从listview点进去到个人资料界面
                    intent.putExtra("flag", "1");
                    startActivityForResult(intent, 105);
                }
            });
            Log.e("#########","我真的执行到了这里");
            new ListAttentionFragment.ListViewAsyncTask().execute();
            //Search();
        }


        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(resultCode==225){
                Log.e("返回intent","我返回到了儿子那里");
                new ListAttentionFragment.ListViewAsyncTask().execute();
            }
        }



        //自定义AsyncTask类，三个参数，第一个不定量入参，第二个：进度 第三个：结果
        public static class ListViewAsyncTask extends AsyncTask<Void, Void, String> {

            //异步加载执行前所作的，可以用来Loading
            protected void onPreExecute() {
                super.onPreExecute();
                //Loading
            }

            //开启另一个线程，用于后台异步加载的工作
            protected String doInBackground(Void... voids) {

                Log.e("#######","对的，你没看错，我正在async里面");
                String result = request(url_getAttention, myName);
                //返回Json数据
                return result;
            }

            private String request(String url_data, String name) {
                try {
                    //网络请求数据
                    String responsedata = null;
                    java.net.URL url = new java.net.URL(url_data + name);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(6000);
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        java.io.InputStream in = conn.getInputStream();
                        byte[] b = new byte[1024 * 512];
                        int len;
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        while ((len = in.read(b)) > -1) {
                            baos.write(b, 0, len);
                        }
                        responsedata = baos.toString();
                        android.util.Log.e("TAG", responsedata);

                    } else {
                        Log.e("网络","连接错误");
                    }
                    return responsedata;
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(String responsedata) {
                super.onPostExecute(responsedata);
                //Loading结束，处理数据
                //由于数据中有Json数据 所以需要一个ArrayList来存储
                focusdatas = new ArrayList<>();
                JSONArray jsonArray = JSONArray.parseArray(responsedata);
                JSONObject jsonObject;
                String attention, head_url;
                Bitmap head;
                int size = jsonArray.size();
                java.net.URL url = null;
                list_attentionName=new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    FocusData focusdata = new FocusData();

                    jsonObject = jsonArray.getJSONObject(i);
                    attention = jsonObject.getString("attention");
                    head_url = jsonObject.getString("head");

                    focusdata.setNick(attention);

                    list_attentionName.add(attention);

                    focusdata.setheadUrl(head_url);

                    focusdatas.add(focusdata);
                }
                //绑定数据
                listView.setAdapter(new MyListAdapter(listView.getContext(), focusdatas));
            }
        }

    }

}