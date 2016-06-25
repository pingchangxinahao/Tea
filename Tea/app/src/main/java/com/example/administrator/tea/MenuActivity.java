package com.example.administrator.tea;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.tea.jeans.VersionActivity;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView text_collect,text_history,text_version,text_idear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findViewById(R.id.collect).setOnClickListener(this);
        findViewById(R.id.history).setOnClickListener(this);
        findViewById(R.id.version).setOnClickListener(this);
        findViewById(R.id.idear).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = new Intent();
        switch (id){
            case R.id.celect:

                break;
            case R.id.history:
                intent.setClass(MenuActivity.this,HistoryActivity.class);
                break;
            case R.id.version:
                intent.setClass(MenuActivity.this, VersionActivity.class);
                startActivity(intent);
                break;
            case R.id.idear:
                intent.setClass(MenuActivity.this,IdearActivity.class);
                startActivity(intent);

                break;
        }
    }
}
