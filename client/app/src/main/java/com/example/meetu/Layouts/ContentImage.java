package com.example.meetu.Layouts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
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
    ContentImageItemAdapter adapter;

    public ContentImage(Context context) {
        super(context);
        this.context = context;
    }

    public ContentImage(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
    }

    //加载网格
    public void initGrid(int imageNumber) {
        this.imageNumber = imageNumber;
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

        //不加载图片，显示灰色背景
        ContentImageItemAdapter adapter = new ContentImageItemAdapter(context, imageNumber, null);
        this.adapter = adapter;
        this.setAdapter(adapter);
    }

    //在已创建好的网格中加载图片
    public void showImages(ArrayList<Bitmap> images) {
        adapter.setImages(images);
    }

    //查看大图
    public void checkBiggerImage(View view) {

    }
}

class ContentImageItemAdapter extends BaseAdapter {
    ArrayList<Bitmap> images;
    ArrayList<ImageView> imageViews;
    int count = 0;
    Context context;

    public ContentImageItemAdapter(Context context, int imageNumber, ArrayList<Bitmap> images) {
        super();
        this.context = context;
        count = imageNumber;
        this.images = new ArrayList<Bitmap>();
        imageViews = new ArrayList<ImageView>();
        if(images != null)
            setImages(images);
    }

    @Override
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setImages(ArrayList<Bitmap> images) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.content_image_item, parent, false);
        final ImageView imageView = view.findViewById(R.id.image_item);
        imageViews.add(imageView);
        if(images != null && images.size() > position)
            imageView.setImageBitmap(images.get(position));


//        //图片显示时，设置图片宽高为正方形
//        final ImageView mv = imageView;
//        final ViewTreeObserver observer = mv.getViewTreeObserver();
//
//        final ViewTreeObserver.OnPreDrawListener preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
//            public boolean onPreDraw() {
////                try {
////                    Thread.sleep(100);
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
//
//                mv.setMaxHeight(360);
//
//                int width = mv.getMeasuredWidth();
//                Log.i("w", ""+imageViews.get(0).getMeasuredWidth());
//                Log.i("h", ""+imageViews.get(0).getMeasuredHeight());
//                Log.i("w2", ""+imageViews.get(0).getLayoutParams().width);
//                Log.i("h2", ""+imageViews.get(0).getLayoutParams().height);
//
//                android.view.ViewGroup.LayoutParams lp = mv.getLayoutParams();
//                lp.height = width - imageView.getPaddingTop() - imageView.getPaddingBottom();
//                lp.width = width - imageView.getPaddingLeft() - imageView.getPaddingRight();
//                mv.setLayoutParams(lp);
//
//                //final ViewTreeObserver vto1 = mv.getViewTreeObserver();
//                //vto1.removeOnPreDrawListener(this); //调用一次之后移除，不影响性能
//                return true;
//            }
//        };
//        observer.addOnPreDrawListener(preDrawListener);

        //设置图片的点击事件

        return view;
    }

}