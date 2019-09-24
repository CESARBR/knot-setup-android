package br.org.cesar.knot_setup_app.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


import java.util.ArrayList;

import br.org.cesar.knot_setup_app.R;
import br.org.cesar.knot_setup_app.model.Tab;

public abstract class BaseActivity extends AppCompatActivity {

    private PageAdapter pageAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TextView headerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header);

        headerTitle = findViewById(R.id.list_title);

        pageAdapter = new PageAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.container);

        tabLayout = findViewById(R.id.tabs);
    }

    public void setupHeader(String headerTitle, ArrayList<Tab> tabs) {
        this.headerTitle.setText(headerTitle);

        for(Tab tab:tabs) {
            pageAdapter.addList(tab.getFragment(),tab.getTabName());
        }

        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void setTabBackground(int color) {
        tabLayout.setBackgroundColor(color);
    }

    public void setTabIndicator(int color){
        tabLayout.setSelectedTabIndicatorColor(color);
    }
}
