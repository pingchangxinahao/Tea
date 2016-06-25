package com.example.administrator.tea.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.administrator.httplib.HttpHelper;
import com.example.administrator.httplib.Request;
import com.example.administrator.httplib.StringRequest;
import com.example.administrator.tea.DetailActivity;
import com.example.administrator.tea.R;
import com.example.administrator.tea.adapter.InfoListAdapter;
import com.example.administrator.tea.jeans.Info;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by Administrator on 2016/6/10 0010.
 */
public class FirstFragment extends Fragment implements ViewPager.OnPageChangeListener{
    private ViewPager viewPager;
    private ListView listView;
    private LinearLayout linearLayout ;
    private RelativeLayout relativeLayout;
    private int class_Id;
    private PtrClassicFrameLayout mRefreshView;
    private int[] images = {R.mipmap.ic123,R.mipmap.ic124,R.mipmap.ic125};
    private InfoListAdapter infoadapter;
    private List<Info> listinfo;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int position = msg.arg1;
            viewPager.setCurrentItem(position);
        }
    };
    public FirstFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.first_fragment,null);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        viewPager.addOnPageChangeListener(this);
        listView = (ListView) view.findViewById(R.id.listview);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.relative);

        linearLayout = (LinearLayout) view.findViewById(R.id.linear);
        intitView(view);
        class_Id =getArguments().getInt("id");
        if (class_Id!=1){
            viewPager.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
        }
       //LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
       /* View headerView = inflater.inflate(R.layout.fragmetn_viewpage,null);
        listView.addHeaderView(headerView);*/
        MyAdapter adapter= new MyAdapter(getActivity());
        viewPager.setAdapter(adapter);
        init();

        viewPager.setCurrentItem(Integer.MAX_VALUE / 2);
        new Thread(){
            @Override
            public void run() {

                for(int i=Integer.MAX_VALUE/2;i<Integer.MAX_VALUE;i++){
                    Message message = new Message();
                    try {
                        Thread.sleep(3000);
                        message.arg1=i;
                        handler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();
       // listView.addHeaderView(viewPager);
        if (bundle != null){
            Parcelable[] ps =  bundle.getParcelableArray("cache");
            Info[] ins = (Info[]) bundle.getParcelableArray("cache");
            if (ins != null && ins.length != 0){
                infos = Arrays.asList(ins);
                infoadapter = new InfoListAdapter(infos);
                listView.setAdapter(infoadapter);
            }else {
                getDataFromNetwork();
            }
        }else {
            getDataFromNetwork();
        }
        listViewListener();


        return view;
    }

    private void listViewListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                Info info = listinfo.get(position);
                System.out.println(info.toString()+"---------------");
                long detil_id =  info.getId();
                String title = info.getTitle();
                String keywords = info.getKeywords();
                String time = info.getTime();
                Bundle bundle = new Bundle();
                bundle.putLong("detail_id",detil_id);
                bundle.putString("title",title);
                bundle.putString("keywords",keywords);
                bundle.putString("time",time);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }

    private void intitView(View view) {
        mRefreshView = (PtrClassicFrameLayout) view.findViewById(R.id.rotate_header_list_view_frame);
        mRefreshView.setResistance(1.7f);
        mRefreshView.setRatioOfHeaderHeightToRefresh(1.2f);
        mRefreshView.setDurationToClose(200);
        mRefreshView.setDurationToCloseHeader(1000);
        // default is false
        mRefreshView.setPullToRefresh(true);
        // default is true
        mRefreshView.setKeepHeaderWhenRefresh(true);
        mRefreshView.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getDataFromNetwork();
            }
        });
    }


    private List<Info> infos = new ArrayList<>();

    private void getDataFromNetwork(){
        String url = "http://www.tngou.net/api/info/list?id="+class_Id;

        StringRequest req = new StringRequest(url, Request.Method.GET, new Request.Callback<String>() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    listinfo = parseJson2List(jsonObject);
                    if (listinfo != null){
                        infos.clear();
                        infos.addAll(listinfo);

                        if (infoadapter == null){
                            infoadapter = new InfoListAdapter(infos);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    listView.setAdapter(infoadapter);
                                }
                            });
                        }else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    infoadapter.notifyDataSetChanged();
                                }
                            });

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshView.refreshComplete();
                    }
                });
            }


        });

        HttpHelper.addRequest(req);
    }


    private List<Info> parseJson2List(JSONObject jsonObject) throws JSONException {

        if (jsonObject == null)return  null;
        JSONArray array = jsonObject.getJSONArray("tngou");
        if (array== null ||array.length() ==0)return null;

        List<Info> list = new ArrayList<>();
        int len = array.length();
        JSONObject obj = null;
        Info info =null;
        for (int i = 0; i <len ; i++) {
            obj = array.getJSONObject(i);
            info = new Info();
            info.setDescription(obj.optString("description"));
            info.setRcount(obj.optInt("rcount"));
            long time = obj.getLong("time");
            String str = new SimpleDateFormat("yyyyMMdd:hhmmss").format(time);
            info.setTime(str);
            info.setImg(obj.optString("img"));
            info.setId(obj.optInt("id"));
            info.setTitle(obj.optString("title"));
            info.setKeywords(obj.optString("keywords"));
            list.add(info);
        }

        return list;
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (infos == null || infos.size() == 0) return;
        Info[] parce = new Info[infos.size()];
        infos.toArray(parce);
        outState.putParcelableArray("cache", parce);
    }

    private void init() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.rightMargin =5;
        for(int i=0;i<images.length;i++) {
            ImageView imageview = new ImageView(getActivity());
            imageview.setImageResource(R.drawable.first_pager_selector);
            if(i==0){
                imageview.setEnabled(true);
            }else {
                imageview.setEnabled(false);
            }
            imageview.setLayoutParams(params);
            linearLayout.addView(imageview);
        }


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i=0;i<images.length;i++){
            ImageView imageView = (ImageView) linearLayout.getChildAt(i);
            if(i==position%images.length){
                imageView.setEnabled(true);
            }else {
                imageView.setEnabled(false);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class MyAdapter extends PagerAdapter{
        private   Context context;
        public MyAdapter(Context context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(images[position%images.length]);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }

}
