package com.example.administrator.tea;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class ViewpagerActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPager pager;
    private LinearLayout linear;
    private Myadapter adapter;
    private List<ImageView> list;
    private ImageView imageView;
    private ImageView imageView1 =null;

    boolean isFirst = true;
    private RelativeLayout relativeLayout;

    private int[] picture = {R.mipmap.slide1,R.mipmap.slide2,R.mipmap.slide3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOnPageChangeListener(this);
        linear = (LinearLayout) findViewById(R.id.linear);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        params.setMargins(230,450,0,180);

        imageView1 = new ImageView(this);
        imageView1.setLayoutParams(params);
        imageView1.setImageResource(R.mipmap.tiyan);
        relativeLayout.addView(imageView1);
        imageView1.setVisibility(View.INVISIBLE);
        list = new ArrayList<ImageView>();
        getList();
        adapter = new Myadapter(this);
        pager.setAdapter(adapter);



    }





    public List<ImageView> getList(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.rightMargin=5;
        for (int i = 0;i<picture.length;i++) {
            imageView = new ImageView(this);
            imageView.setLayoutParams(params);
            imageView.setImageResource(R.drawable.pager_selector);
            linear.addView(imageView);
            list.add(imageView);
            if(i==0){
                imageView.setEnabled(false);
            }else {
                imageView.setEnabled(true);
            }


        }
        return list;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      /*  ImageView imageView = (ImageView) relativeLayout.getChildAt(position);
        if(position==list.size()-1){
            imageView.setVisibility(ImageView.VISIBLE);
        }else {
            imageView.setVisibility(ImageView.INVISIBLE);
        }*/
    }

    @Override
    public void onPageSelected(int position) {

        for (int i = 0;i<picture.length;i++){
            imageView = (ImageView) linear.getChildAt(i);
            if (i==position){
                imageView.setEnabled(false);
            }else {
                imageView.setEnabled(true);
            }

        }

        if(position==picture.length-1){


            //imageView1.setTag(imageView1);

            imageView1.setVisibility(View.VISIBLE);
            imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(ViewpagerActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        }else {
            //relativeLayout.addView(imageView1);
            imageView1.setVisibility(View.GONE);
            //imageView1.setVisibility(View.INVISIBLE);

        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class Myadapter extends PagerAdapter{
        private Context context;
        public Myadapter(Context context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return picture.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(context);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);

            view.setImageResource(picture[position]);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }
}
