package fr.teamrenaissance.julien.teamrenaissance;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.teamrenaissance.julien.teamrenaissance.utils.MyFragmentAdapter;

public class Accueil extends AppCompatActivity {
    public static final String TAG = "AccueilActivity";

    public static final String []sTitle = new String[]{"Emprunter","Mes Prêts","Profil"};
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        initView();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText(sTitle[0]));
        tabLayout.addTab(tabLayout.newTab().setText(sTitle[1]));
        tabLayout.addTab(tabLayout.newTab().setText(sTitle[2]));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.i(TAG,"onTabSelected:"+tab.getText());
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
                Log.i(TAG,"select page:"+position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

}
