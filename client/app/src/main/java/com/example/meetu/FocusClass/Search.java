package com.example.meetu.FocusClass;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.meetu.R;

public class Search extends FrameLayout {

    SearchView searchView;
    ListView listView;
    EditText editText;


    public Search(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.search, this);
        searchView=findViewById(R.id.search_view);
        listView=findViewById(R.id.listView);



        //searchView.setIconifiedByDefault(false);
        searchView.setInputType(InputType.TYPE_CLASS_TEXT);
        searchView.setIconifiedByDefault(false);





        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //点击搜索按钮时触发该方法
            //点击最终搜索按钮才会触发
            @Override
            public boolean onQueryTextSubmit(final String query) {
                Log.d("Tag","提交"+searchView.getQuery().toString());




                return false;
            }

            //当搜索内容改变时触发该方法
            //只要搜索内容改变就触发
            @Override
            public boolean onQueryTextChange(String newText) {
               Log.d("Tag","QQQQ:"+searchView.getQuery().toString());
                return false;
            }
        });






    }

    void Init(){


    }






}
