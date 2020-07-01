package com.example.meetu.Entities;

import org.json.JSONException;
import org.json.JSONObject;

public class Convert {

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
