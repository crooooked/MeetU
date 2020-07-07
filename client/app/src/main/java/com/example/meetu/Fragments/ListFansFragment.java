package com.example.meetu.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.meetu.Activities.BodyActivity;
import com.example.meetu.Activities.PersonalDataShowActivity;
import com.example.meetu.Adapter.MyListAdapter;
import com.example.meetu.FocusClass.AnalyseJson;
import com.example.meetu.FocusClass.FocusData;
import com.example.meetu.FocusClass.Httprequest;
import com.example.meetu.R;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFansFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFansFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public android.widget.ListView listView;
    public SearchView searchView;
    public android.widget.TextView tv_nick, tv_gender, tv_addr;

    public ImageView image_head;

    private static int PERSONAL_DATA=105;

    String ip="10.236.66.58";
    String url_getAudiences="http://"+ip+":8080/get-audiences?username=";
    String myName= BodyActivity.key_username;
    List<FocusData> focusdatas;

    public ListFansFragment() {
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
    public static ListFansFragment newInstance(String param1, String param2) {
        ListFansFragment fragment = new ListFansFragment();
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


        View view = inflater.inflate(R.layout.view_fans, container, false);
        tv_nick = view.findViewById(R.id.tv_nick_show);
        tv_gender = view.findViewById(R.id.tv_gender_show);
        tv_addr = view.findViewById(R.id.tv_addr_show);
        image_head = view.findViewById(R.id.image_person_head);
        searchView = view.findViewById(R.id.search);
        listView = view.findViewById(R.id.listView_fans);
        searchView = view.findViewById(R.id.search);

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
                startActivityForResult(intent, PERSONAL_DATA);
            }
        });
        new ListViewAsyncTask().execute();
        //Search();
    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==105){
            new ListViewAsyncTask().execute();
//            assert data != null;
        }
    }





    //自定义AsyncTask类，三个参数，第一个不定量入参，第二个：进度 第三个：结果
    public class ListViewAsyncTask extends AsyncTask<Void, Void, String> {

        //异步加载执行前所作的，可以用来Loading
        protected void onPreExecute() {
            super.onPreExecute();
            //Loading
        }

        //开启另一个线程，用于后台异步加载的工作
        protected String doInBackground(Void... voids) {

            String result = request(url_getAudiences, myName);
            Log.d("Tag","获取听众列表====="+result);
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
                    Toast.makeText(getContext(), "连接网络错误", Toast.LENGTH_SHORT).show();
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
            for (int i = 0; i < size; i++) {
                FocusData focusdata = new FocusData();

                jsonObject = jsonArray.getJSONObject(i);
                attention = jsonObject.getString("audience");
                head_url = jsonObject.getString("head");

                focusdata.setNick(attention);


                focusdata.setheadUrl(head_url);

                focusdatas.add(focusdata);
            }
            //绑定数据
            listView.setAdapter(new MyListAdapter(getContext(), focusdatas));
        }
    }
}