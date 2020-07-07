package com.example.meetu.Layouts;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.WrapperListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.meetu.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RemarkUnfold extends Dialog{
    //View view;

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
        //view = View.inflate(getContext(), R.layout.view_all_remarks, null);
        setContentView(R.layout.view_all_remarks);
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

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        this.dismiss();
        return false;
    }

    public void Hide() {
        this.dismiss();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
            if(jsonArray.length() != 0) {
                findViewById(R.id.no_remark).setVisibility(View.GONE);
            }
            linearLayout.setDividerDrawable(getContext().getDrawable(R.drawable.divider));
            linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);

            for(int i=0; i<jsonArray.length(); i++) {
                String username = jsonArray.getJSONObject(i).getString("username");
                String content = jsonArray.getJSONObject(i).getString("remark");
                TextView textView = new TextView(getContext());
                textView.setTextSize(18);
                textView.setText(username+"："+content);
                textView.setPadding(40,20,40,20);

                linearLayout.addView(textView);

//                if(i != jsonArray.length()-1){
//                    View divider = new View(getContext());
//                    divider.setBackground(getContext().getDrawable(R.color.colorBlack));
//
//                    LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 1);
//                    textView.setLayoutParams(lp2);
//
//                    linearLayout.addView(divider);
//                }
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }

}
