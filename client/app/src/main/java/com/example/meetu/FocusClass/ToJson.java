package com.example.meetu.FocusClass;

import com.alibaba.fastjson.JSONObject;

//转成一个JSONObject对象
public class ToJson {

    String username;
    String targetname;
    JSONObject jsonObject=new JSONObject(true);


    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public void setMyName(String username) {
        this.username = username;
    }

    public void setTargetName(String targetname) {
        this.targetname = targetname;
    }



    public JSONObject setJsonObject(String username, String targetname, int flag)  {

        jsonObject.put("username",username);
        jsonObject.put("targetname",targetname);
        jsonObject.put("flag",flag);
        return jsonObject;

    }


}
