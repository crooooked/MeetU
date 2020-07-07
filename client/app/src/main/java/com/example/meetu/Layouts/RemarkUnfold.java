package com.example.meetu.Layouts;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.meetu.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RemarkUnfold extends Dialog{

    public RemarkUnfold(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 这句代码换掉dialog默认背景，否则dialog的边缘发虚透明而且很宽
        // 总之达不到想要的效果
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View view = View.inflate(getContext(), R.layout.view_all_remarks, null);
        setContentView(view);
        // 这句话起全屏的作用
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        Space space = findViewById(R.id.mask);
        space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hide();
            }
        });

    }

    public void Hide() {
        this.dismiss();
    }

    public void setInfo(String url){
        LinearLayout linearLayout = findViewById(R.id.remark_linear_layout);
        OkHttpClient client = new OkHttpClient();
        Log.i("url", url);
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String str = response.body().string();
            Log.i("remarks", str);

            JSONArray jsonArray = new JSONArray(str);
            for(int i=0; i<jsonArray.length(); i++) {
                String username = jsonArray.getJSONObject(i).getString("username");
                String content = jsonArray.getJSONObject(i).getString("content");
                TextView textView = new TextView(getContext());
                textView.setText(username+"："+content);

                if(i != jsonArray.length()-1){
                    View divider = new View(getContext());
                    linearLayout.addView(divider);
                }
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }

}
