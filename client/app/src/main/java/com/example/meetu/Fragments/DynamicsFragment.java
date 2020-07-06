package com.example.meetu.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.meetu.Entities.Content;
import com.example.meetu.Layouts.ContentCard;
import com.example.meetu.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DynamicsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DynamicsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DynamicsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DynamicsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DynamicsFragment newInstance(String param1, String param2) {
        DynamicsFragment fragment = new DynamicsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_dynamics, container, false);
        LinearLayout layout = view.findViewById(R.id.linear_layout_space);

        //测试用的数据
        Bitmap head = BitmapFactory.decodeResource(getResources(), R.mipmap.sample_head);
        //9张图
        ArrayList<Bitmap> images = new ArrayList<>();
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image1));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image2));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image3));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image4));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image5));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image6));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image7));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image8));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.sample_image9));
        //评论
        String[] remark_content = new String[] {"好的！", "知道了"};
        String[] remark_username = new String[] {"小A", "小B"};


        //测试无图片+无转发的状态
        Content content1 = new Content(head, null);
        layout.addView(new ContentCard(getContext(), content1));

        //测试有图片+无转发的状态
        Content content2 = new Content(head, null);
        content2.setImages(images);
        ContentCard contentCard2 = new ContentCard(getContext(), content2);
        layout.addView(contentCard2);

        //测试无图片+有转发的状态
        Content content3 = new Content(head, null);
        content3.setRepost(123);
        content3.setRepostContent(content1);
        layout.addView(new ContentCard(getContext(), content3));

        //测试有图片+有评论的状态
        Content content4 = new Content(head, null);
        content4.setImages(images);
        content4.setRemarks_content(remark_content);
        content4.setRemarks_username(remark_username);
        ContentCard contentCard4 = new ContentCard(getContext(), content4);
        layout.addView(contentCard4);


//        //测试从网络获取content
//        Content content4 = new Content(this, 111);
//        layout.addView(new ContentCard(this, content4));

        return view;


    }

}