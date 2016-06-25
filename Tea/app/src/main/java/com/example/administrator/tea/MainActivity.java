package com.example.administrator.tea;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.administrator.tea.Fragment.FirstFragment;
import com.example.administrator.tea.jeans.TabInfo;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    long exittime=0;
    private TabLayout tab;
    private ViewPager pager;
    private List<Fragment> list;
    private Toolbar toolbar;
    //private String[] title ={"头条","百科","资讯","经营","数据"};
    private TabInfo[] tabs = new TabInfo[]{
            new TabInfo("头条", 1),
            new TabInfo("百科", 2),
            new TabInfo("资讯", 3),
            new TabInfo("经营", 4),
            new TabInfo("数据", 5),

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tab = (TabLayout) findViewById(R.id.tab);

        pager = (ViewPager) findViewById(R.id.pager);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_logo);
        toolbar.setTitle("茶百科");
        toolbar.inflateMenu(R.menu.mainactivity_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MainActivity.this,MenuActivity.class);
                startActivity(intent);
                return false;
            }
        });
        // initFragment();
        MyadapterMenu adapter= new MyadapterMenu(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tab.setupWithViewPager(pager);
        tab.setTabMode(TabLayout.MODE_SCROLLABLE);


    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


         if(keyCode==KeyEvent.KEYCODE_BACK){
             if((System.currentTimeMillis()-exittime)>3000){
                 Toast.makeText(MainActivity.this,"再按一次退出", Toast.LENGTH_SHORT).show();
                 exittime = System.currentTimeMillis();
             }else {
                 finish();
                 System.exit(0);

             }

         }


        return true;
    }
    public class MyadapterMenu extends FragmentPagerAdapter{

        public MyadapterMenu(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            FirstFragment firstFragment = new FirstFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("id",tabs[position].getId());
            firstFragment.setArguments(bundle);

            return firstFragment;
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position].getName();
        }
    }
}
