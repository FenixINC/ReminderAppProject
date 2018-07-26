package com.example.taras.reminerapp;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.taras.reminerapp.content.EventFragment;
import com.example.taras.reminerapp.content.NewsFragment;
import com.example.taras.reminerapp.content.VideoFragmentJava;
import com.example.taras.reminerapp.databinding.ActivityMainBinding;
import com.example.taras.reminerapp.db.AppDatabase;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppDatabase.getInstance();

        setTitle("Reminder");
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBinding.fab.setOnClickListener(view -> {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

            // start AddRemindActivity:
        });


        // Create Intent in Kotlin:
//        val intent = Intent(this@MainActivity, CustomersActivity::class.java)
//        startActivity(intent)


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mBinding.drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mBinding.navView.setNavigationItemSelectedListener(this);

        PageAdapter adapter = new PageAdapter(getSupportFragmentManager());
        adapter
                .add("News", NewsFragment.newInstance())
                .add("Events", EventFragment.newInstance())
                .add("Video", VideoFragmentJava.newInstance());
        mBinding.viewPager.setAdapter(adapter);
        mBinding.tabs.setupWithViewPager(mBinding.viewPager);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, NewsFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    private Fragment getNotificationTab(PageAdapter adapter, int position) {
//        mBinding.viewPager.setCurrentItem(position);
        return adapter.getItem(position);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                //
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
//            setNotificationTab(2);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        mBinding.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}
