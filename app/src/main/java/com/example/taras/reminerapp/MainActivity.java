package com.example.taras.reminerapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.taras.reminerapp.databinding.ActivityMainBinding;
import com.example.taras.reminerapp.db.Constants;
import com.example.taras.reminerapp.navigation.MainRemindFragment;
import com.example.taras.reminerapp.navigation.SearchFragment;
import com.example.taras.reminerapp.reminds.my.DialogCreateRemind;
import com.example.taras.reminerapp.reminds.stars.StarsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setTitle("Reminder");

        //TODO: IDEAS: 1)make reminds to star like favorite; 2) make on main activity 3 square vertical buttons: "My Reminds", "Content Reminds", "Calendar Reminds - Coming Soon";
        //TODO:

        setSupportActionBar(mBinding.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mBinding.drawerLayout,
                mBinding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mBinding.navView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, MainRemindFragment.newInstance())
                .commit();
    }

    private Fragment setNotificationTab(PageAdapter adapter, int position) {
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
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search: {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, SearchFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
            }
            break;
            case R.id.action_create_remind: {
                DialogCreateRemind dialog = DialogCreateRemind.newInstance(Constants.SERVER_DEFAULT);
                dialog.show(getSupportFragmentManager(), "create-remind-dialog");
            }
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_search: {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, SearchFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
            }
            break;
            case R.id.nav_star: {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, StarsFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
            }
            case R.id.logout: {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
            break;
        }
        mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}