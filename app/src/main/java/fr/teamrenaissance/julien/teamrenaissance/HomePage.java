package fr.teamrenaissance.julien.teamrenaissance;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.teamrenaissance.julien.teamrenaissance.utils.MyFragmentAdapter;
import fr.teamrenaissance.julien.teamrenaissance.utils.PersistentCookieStore;

public class HomePage extends AppCompatActivity {
    public static final String TAG = "HomeActivity";

    public static final String []sTitle = new String[]{"Accueil","Emprunter","Mes Prêts","Profil"};
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        initView();

        PersistentCookieStore cookieStore = new PersistentCookieStore(this);

        //TODO
      /*  String[] cookieArray = getIntent().getStringArrayExtra("cookieArray");
        for(int i=0;i<cookieArray.length;i++)
        {
            HttpCookie cookie = new HttpCookie(cookieArray[i].split("=")[0], cookieArray[i].split("=")[1]);
            cookieStore.add(URI.create(""), cookie);
        } */


        //In the latest versions of Android when using the compat library for toolbar,in order to get the menu items
        // to display in the toolbar you must do the following:
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void onResume() {
        int id = getIntent().getIntExtra("userloginflag", 0);
        if (id == 4) {
            viewPager.setCurrentItem(4);
        }
        super.onResume();
    }

    //DOUBLE CLICK TO QUIT
    long SystemTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(System.currentTimeMillis()- SystemTime < 1000 && SystemTime != 0){
                Intent intent = new Intent();
                intent.setAction("ExitApp");
                intent.setClass(this,Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }else{
                SystemTime = System.currentTimeMillis();
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText(sTitle[0]));
        tabLayout.addTab(tabLayout.newTab().setText(sTitle[1]));
        tabLayout.addTab(tabLayout.newTab().setText(sTitle[2]));
        tabLayout.addTab(tabLayout.newTab().setText(sTitle[3]));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(Accueil.newInstance());
        fragments.add(Emprunter.newInstance());
        fragments.add(Mesprets.newInstance());
        fragments.add(Profil.newInstance());

        MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager(),fragments, Arrays.asList(sTitle));
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
