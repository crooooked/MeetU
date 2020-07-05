package com.example.meetu.FocusClass;

import com.alibaba.fastjson.JSONObject;

public class AnalyseJson {

    JSONObject jsonObject;
    String username,gender,address;
    String headUrl,backgroundUrl;

    BitmapAndStringUtils bitmapAndStringUtils=new BitmapAndStringUtils();


    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setHead(String headUrl) {
        this.headUrl = headUrl;
    }

    public void setBackground(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }



    public String getUsername(JSONObject jsonObject)  {
        String username=jsonObject.getString("username");

        return username;
    }

    public String getGender(JSONObject jsonObject)  {
        String gender=jsonObject.getString("gender");
        return gender;
    }

    public String getAddress(JSONObject jsonObject)  {
        String address=jsonObject.getString("address");
        return address;
    }



    public String getHeadUrl(JSONObject jsonObject) {
        String headUrl=jsonObject.getString("head");

        return headUrl;
    }


    public String getBackgroundUrl(JSONObject jsonObject) {
        String backgroundUrl=jsonObject.getString("background");
        return backgroundUrl;
    }

}
