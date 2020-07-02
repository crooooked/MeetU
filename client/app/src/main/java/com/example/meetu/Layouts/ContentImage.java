package com.example.meetu.Layouts;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.meetu.R;

import java.util.ArrayList;

public class ContentImage extends GridView {
    Context context;
    int imageNumber;

    public ContentImage(Context context, int imageNumber) {
        super(context);
        this.context = context;
        this.imageNumber = imageNumber;
    }

    //加载网格
    public void initGrid() {
        //设置列数
        if(imageNumber == 1) //1张图片：按图片大小显示，高度和宽度均有上限
            setNumColumns(1);
        else if(imageNumber == 2 || imageNumber == 3) //2-3张图片：横向显示，图片均分屏幕宽度，尺寸固定（小于或大于这个尺寸，则被强行拉到这个尺寸）
            setNumColumns(imageNumber);
        else if(imageNumber == 4) //4张图片：2*2显示，图片大小相等，尺寸固定
            setNumColumns(2);
        else if(imageNumber >= 5 && imageNumber <= 9) //5-9张图片：3列，高度是2或3
            setNumColumns(3);
        else { //9张图片以上：3*3显示，最后一张图片添加蒙版，例如"+18"表示剩余18张图未显示

        }

        ContentImageItemAdapter adapter = new ContentImageItemAdapter(context, imageNumber);
        this.setAdapter(adapter);

    }

    //在已创建好的网格中加载图片
    public void showImages() {

    }

    //查看大图
    public void checkBiggerImage(View view) {

    }
}

class ContentImageItemAdapter extends BaseAdapter {
    ArrayList<Integer> images;
    int count = 0;
    Context context;

    public ContentImageItemAdapter(Context context, int imageNumber) {
        super();
        this.context = context;
        count = imageNumber;
    }

    @Override
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setImages(ArrayList<Integer> images) {
        this.images.clear();
        count = images.size();
        for(int i=0; i<count; i++) {
            this.images.add(images.get(i));
        }
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //设置图片源
        View view = LayoutInflater.from(context).inflate(R.layout.content_image_item, null);
        final ImageView imageView = view.findViewById(R.id.image_item);
        if(images != null && images.size() >= position)
            imageView.setImageResource(images.get(position));

        //图片显示时，设置图片宽高为正方形
        final ImageView mv = imageView;
        final ViewTreeObserver observer = mv.getViewTreeObserver();
        final ViewTreeObserver.OnPreDrawListener preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                int width = mv.getMeasuredWidth();
                int height = mv.getMeasuredHeight();

                android.view.ViewGroup.LayoutParams lp = mv.getLayoutParams();
                lp.height = width;
                lp.width = width - imageView.getPaddingLeft() - imageView.getPaddingRight();
                mv.setLayoutParams(lp);

                final ViewTreeObserver vto1 = mv.getViewTreeObserver();
                vto1.removeOnPreDrawListener(this); //调用一次之后移除，不影响性能
                return true;
            }
        };
        observer.addOnPreDrawListener(preDrawListener);

        return view;
    }

}