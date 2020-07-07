package com.example.meetu.Activities;

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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.meetu.Adapter.MyListAdapter;
import com.example.meetu.FocusClass.AnalyseJson;
import com.example.meetu.FocusClass.FocusData;
import com.example.meetu.FocusClass.Httprequest;
import com.example.meetu.FocusClass.IP;
import com.example.meetu.R;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;


public class FocusListActivity extends AppCompatActivity {



    public Button button;
    public android.widget.ListView listView;
    public SearchView searchView;
    public android.widget.TextView tv_nick,tv_gender,tv_addr;
    public Button btn_focus,btn_watch;
    public ImageButton return_iconButton;
    public ImageView image_head,image_background;
    public ImageView image_show;

    Httprequest httprequest=new Httprequest();
    //httprequest.handler=handler;将Httprequest的handler与下面的handler关联在一起

    Bitmap bitmap;
    /*

    上面的nick[]应该向后端请求，读数据库，读出图片和名称，然后将数据放到nick[]中

     */

   Handler handler=new Handler(){
       @Override
       public void handleMessage(@androidx.annotation.NonNull Message msg) {

           super.handleMessage(msg);
           int w = msg.what;
           String getMsg= msg.obj.toString();
           AnalyseJson analyseJson=new AnalyseJson();

           tv_nick=findViewById(R.id.tv_nick_show);
           tv_gender=findViewById(R.id.tv_gender_show);
           tv_addr=findViewById(R.id.tv_addr_show);

           image_head=findViewById(R.id.image_person_head);


           if (w ==0){
               //get到服务器来的图片+string（昵称）,图片以字符串形式传过来
               //w=0,显示在listview中的关注列表中
               //这里不做处理，直接在listview的FocusPeopleShow修改

               JSONArray jsonArray=JSONArray.parseArray(getMsg);
               JSONObject jsonObject;

               String attention;
               Bitmap head;
               int size=jsonArray.size();
//               for(int i=0;i<size;i++){
//                   //先得到对应的JSONObject
//                   //再得到object中的attention head
//                   jsonObject=jsonArray.getJSONObject(i);
//                   attention=jsonObject.getString("attention");
//
//                   head=analyseJson.getHead(jsonObject);
//
//                   nick[i]=attention;
////                   bitmap[i]=head;
//                   //将head转换为图片，并在imageview中显示出来
//                //   Log.d("Tag","=====handler====="+nick[i]);
//
//               }


           }
           if (w==1){
               //json字符串转换为JSONObject对象
               JSONObject jsonObject=JSONObject.parseObject(getMsg);
               //解析JSONObject对象


               //get到服务器的图片 name gender address ，
               //w=1，显示在个人资料的iMAgeview和TextView中
               //解析message
               Bitmap head,background;
               String headUrl,backgroundUrl;
               String username,gender,address;
               username=analyseJson.getUsername(jsonObject);
               gender=analyseJson.getGender(jsonObject);
               address=analyseJson.getAddress(jsonObject);
               headUrl=analyseJson.getHeadUrl(jsonObject);
               backgroundUrl=analyseJson.getBackgroundUrl(jsonObject);

               //setContentView(R.layout.peopledata);
               //设置个人资料
               //image_head.setImageBitmap(head);
               //image_background.setImageBitmap(background);
               tv_nick.setText(username);
               tv_gender.setText(gender);
               tv_addr.setText(address);

           }

       }


   };


    java.util.List<FocusData> focusdatas;

//    IP ip=new IP();
//
//    String url=ip.ip;
//    String url_getAttention=url+"get-attentions?username=";

    String url_getAttention="http://10.234.184.71:8080/get-attentions?username=";




    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_attention);

        searchView=findViewById(R.id.search);
        listView=findViewById(R.id.listView_focus);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {

                //用Intent进行跳转
                Intent intent=new Intent(FocusListActivity.this,PersonalDataShowActivity.class);
                intent.putExtra("Name",focusdatas.get(position).nick);

                //1：表示已经关注,表示从listview点进去到个人资料界面
                intent.putExtra("flag","1");
                startActivity(intent);


            }
        });
        new ListViewAsyncTask().execute();
        Search();
    }



    //搜索框searchview
    public void Search(){
        searchView=findViewById(R.id.search);

        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

           //最终提交时触发
            @Override
            public boolean onQueryTextSubmit(String query) {
                //要判断一下，判断是否有这个用户
                //用Intent进行跳转
                Intent intent=new Intent(FocusListActivity.this,PersonalDataShowActivity.class);
                //0：表示从搜索框到个人资料界面
                intent.putExtra("flag","0");
                intent.putExtra("Name",query);
                startActivity(intent);
                return false;
            }

            //搜索框中文本改变时触发
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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
            String result = request(url_getAttention,"lby");
            //返回Json数据
            return result;
        }

        private String request(String url_data, String name ) {
            try {
                //网络请求数据
                String responsedata = null;
                java.net.URL url = new java.net.URL(url_data+name);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(6000);
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    java.io.InputStream in = conn.getInputStream();
                    byte[] b = new byte[1024 * 512];
                    int len;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    while((len = in.read(b))>-1){
                        baos.write(b,0,len);
                    }
                    responsedata = baos.toString();
                    android.util.Log.e("TAG", responsedata);

                }else {
                    Toast.makeText(getApplicationContext(), "连接网络错误", Toast.LENGTH_SHORT).show();
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
            JSONArray jsonArray= JSONArray.parseArray(responsedata);
            JSONObject jsonObject;



            String attention,head_url;
            Bitmap head;
            int size=jsonArray.size();
            java.net.URL url = null;
            for(int i = 0;i<size;i++){
                FocusData focusdata = new FocusData();

                jsonObject=jsonArray.getJSONObject(i);
                attention=jsonObject.getString("attention");
                head_url=jsonObject.getString("head");
                /*
               根据head_url取得图片，转化为bitmap格式
                 */
                focusdata.setNick(attention);
                //focusdata.setBitmap(head);

                focusdata.setheadUrl(head_url);

                focusdatas.add(focusdata);
            }
            //绑定数据
            listView.setAdapter(new MyListAdapter(FocusListActivity.this,focusdatas));
        }
    }





















}