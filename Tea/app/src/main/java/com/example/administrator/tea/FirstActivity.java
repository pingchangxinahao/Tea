package com.example.administrator.tea;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class FirstActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private boolean bool =true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        preferences = getSharedPreferences("pre",MODE_PRIVATE);
        bool= preferences.getBoolean("key",true);
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent();
                if (bool) {
                    intent.setClass(FirstActivity.this, ViewpagerActivity.class);
                    bool =false;
                }else {
                    intent.setClass(FirstActivity.this,MainActivity.class);
                }
                startActivity(intent);

                finish();
            }
        };
        timer.schedule(task,1000);


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("key", bool);
        editor.commit();

    }
    private void isFirst() {
        preferences = getSharedPreferences("pre",MODE_PRIVATE);
        bool= preferences.getBoolean("key",true);
        if (bool==false){
            Intent intent = new Intent();
            intent.setClass(this,MainActivity.class);
            startActivity(intent);
        }

    }
}
