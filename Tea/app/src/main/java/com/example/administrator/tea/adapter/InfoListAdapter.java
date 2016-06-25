package com.example.administrator.tea.adapter;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.httplib.BitMapRequest;
import com.example.administrator.httplib.HttpHelper;
import com.example.administrator.httplib.Request;
import com.example.administrator.tea.R;
import com.example.administrator.tea.jeans.Info;

import java.util.List;

/**
 * Created by Administrator on 2016/6/23 0023.
 */
public class InfoListAdapter extends BaseAdapter {
    private List<Info> infoList;

    public InfoListAdapter(List<Info> infoList) {
        this.infoList = infoList;

    }
    @Override
    public int getCount() {
        return infoList == null?0:infoList.size();
    }

    @Override
    public Object getItem(int position) {
        return infoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null){
            convertView = View.inflate(parent.getContext(), R.layout.list_item,null);
            vh = new ViewHolder();
            vh.iv_icon = (ImageView) convertView.findViewById(R.id.item_iv);
            vh.tv_desc = (TextView) convertView.findViewById(R.id.item_tv_desc);
            vh.tv_rcount =(TextView)convertView.findViewById(R.id.item_tv_rc);
            vh.tv_time =(TextView)convertView.findViewById(R.id.item_tv_time);
            convertView.setTag(vh);

        }

        Info info = infoList.get(position);
        vh = (ViewHolder) convertView.getTag();


        vh.tv_time.setText(info.getTime());
        vh.tv_desc.setText(info.getDescription());
        vh.tv_rcount.setText("" + info.getRcount());
        vh.iv_icon.setImageResource(R.mipmap.ic_launcher);
        loadImage(vh.iv_icon,"http://tnfs.tngou.net/image"+info.getImg()+"_100x100");
        return convertView;


    }

    public class ViewHolder{

        public TextView tv_desc;
        public TextView tv_time;
        public TextView tv_rcount;
        public ImageView iv_icon;

    }
    public void loadImage(final ImageView iv, final String url){




        iv.setTag(url);
        BitMapRequest br = new BitMapRequest(url, Request.Method.GET, new Request.Callback< Bitmap>(){
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(final Bitmap response) {
                if (iv != null && response != null && ((String)iv.getTag()).equals(url)){

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            iv.setImageBitmap(response);
                        }
                    });

                }
            }



        } );
        HttpHelper.addRequest(br);
    }
}
