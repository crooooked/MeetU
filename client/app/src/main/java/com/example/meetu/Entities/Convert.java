package com.example.meetu.Entities;

import org.json.JSONException;
import org.json.JSONObject;

public class Convert {

//    public static User userObject(String user,String password){
//        User user1;
//        try {
//            JSONObject jsonObject=new JSONObject(user);
//            String uid=jsonObject.getString("uid");
//            String username=jsonObject.getString("username");
//
//            String gender=jsonObject.getString("gender");
//            String address=jsonObject.getString("address");
//            String head=jsonObject.getString("head");
//            String background=jsonObject.getString("background");
//
//            user1=new User(uid.);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//    }
    public static User getUserFromStr(String str){
        User user=new User(-1);
        try {
            JSONObject jsonObject=new JSONObject(str);
            user.setUid(jsonObject.getInt("uid"));
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
