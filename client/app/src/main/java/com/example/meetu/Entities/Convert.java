package com.example.meetu.Entities;

//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
import com.alibaba.fastjson.*;

public class Convert {

    public static String getStrContentFromContent(Content content){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("uid",content.getUid());
            jsonObject.put("content",content.getContent());
            JSONArray jsonArray=new JSONArray();
            if(content.getImage_urls()!=null) {
                for (String url : content.getImage_urls()) {
                    JSONObject jsonObject1=new JSONObject();
                    jsonObject1.put("image", url);
                    jsonArray.add(jsonObject1);
                }
            }
            jsonObject.put("images",jsonArray);
            jsonObject.put("repost",content.getRepost());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

//    public static
    public static User getUserFromStr(String str){
        User user=new User(-1);
        try {
            JSONObject jsonObject=JSONObject.parseObject(str);
//            JSONObject jsonObject=new JSONObject(str);
            user.setUid(jsonObject.getInteger("uid"));
            user.setInfo(jsonObject.getString("username"),
                    jsonObject.getString("password"),
                    jsonObject.getString("address"),
                    jsonObject.getString("gender"),
                    jsonObject.getString("head"),
                    jsonObject.getString("background"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static String strJsonObject(String key,String value){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String strJsonObject(String []keys,String []values){
        JSONObject jsonObject=new JSONObject();
        try {
            for(int i=0;i<keys.length;i++){
                jsonObject.put(keys[i],values[i]);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String strRegLog(String username,String password){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("username",username);
            jsonObject.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String strPersonalInfo(String username,String address,String gender,String head,String background){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("username",username);
            jsonObject.put("address",address);
            jsonObject.put("gender",gender);
            jsonObject.put("head",head);
            jsonObject.put("background",background);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
