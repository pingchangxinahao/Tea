package com.example.administrator.tea;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.example.administrator.httplib.HttpHelper;
import com.example.administrator.httplib.Request;
import com.example.administrator.httplib.StringRequest;
import com.example.administrator.tea.jeans.DetailInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class DetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView content,detil_title,detil_keywords,detil_year,detil_hour;
    private List<DetailInfo> detailinfo;
    private  Bundle bundle1;
    private long detail_id;
    private String title,keywords,time,year,hour;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_logo);
        //toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("广州茶派");
        toolbar.inflateMenu(R.menu.menu_toolbar);
        ShareSDK.initSDK(this);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id==R.id.share){
                    //ShareSDK.initSDK(this);
                    OnekeyShare oks = new OnekeyShare();
                    oks.show(DetailActivity.this);
                }
                return false;
            }
        });
        content = (TextView) findViewById(R.id.content);
        detil_title = (TextView) findViewById(R.id.title);
        detil_keywords = (TextView) findViewById(R.id.keyword);
        detil_year = (TextView) findViewById(R.id.year);
        detil_hour = (TextView) findViewById(R.id.second);
        Intent intent = getIntent();
        bundle1 = intent.getExtras();
        detail_id = bundle1.getLong("detail_id");
        title = bundle1.getString("title");
        keywords = bundle1.getString("keywords");
        time = bundle1.getString("time");
        int index = time.indexOf(":");
        year = time.substring(0,index);
        hour = time.substring(index+1);



        getDataFromNetwork(detail_id);
    }

    private void getDataFromNetwork(long id){
        String url = "http://www.tngou.net/api/info/show?id="+id;

        StringRequest req = new StringRequest(url, Request.Method.GET, new Request.Callback<String>() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    final String message = jsonObject.optString("message");
                    final Spanned spanned=  Html.fromHtml(message);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            detil_title.setText(title);
                            detil_keywords.setText(keywords);
                            detil_year.setText(year);
                            detil_hour.setText(hour);
                            content.setText(spanned);
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }


        });

        HttpHelper.addRequest(req);
    }



   /* protected void onDestroy() {

        ShareSDK.stopSDK(this);
    }*/
}
