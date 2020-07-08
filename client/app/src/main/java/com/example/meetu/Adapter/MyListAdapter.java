package com.example.meetu.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.meetu.FocusClass.FocusData;
import com.example.meetu.R;
import com.example.meetu.Tools.GlideCircleTransform;

import java.util.List;

public class MyListAdapter extends BaseAdapter {


    List<FocusData> mfocusList;
    Context mContext;


    public MyListAdapter(Context context, List<FocusData> focusDataList) {
        this.mfocusList = focusDataList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mfocusList.size();
    }

    @Override
    public Object getItem(int i) {
        return mfocusList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView==null){
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =layoutInflater.inflate(R.layout.focus_item,null);
            viewHolder.nick = (TextView) convertView.findViewById(R.id.item_nick);
            viewHolder.head=(ImageView)convertView.findViewById(R.id.item_image);


            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.nick.setText(mfocusList.get(position).getNick());
        //viewHolder.head.setImageBitmap(mfocusList.get(position).getBitmap());

        Glide.with(mContext)
                .load(mfocusList.get(position).getheadUrl())
                .skipMemoryCache(true)
                 .diskCacheStrategy(DiskCacheStrategy.NONE)

                .transform(new GlideCircleTransform(mContext))

                .into((ImageView)convertView.findViewById(R.id.item_image));


        //viewHolder.head.setImageResource(R.drawable.head_icon);




        return convertView;
    }


    public class ViewHolder{
        public TextView nick;
        public ImageView head;

    }
}


