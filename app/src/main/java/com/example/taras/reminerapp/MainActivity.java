package com.example.taras.reminerapp;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
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

import com.example.taras.reminerapp.navigation.RemindFragment;
import com.example.taras.reminerapp.databinding.ActivityMainBinding;
import com.example.taras.reminerapp.db.AppDatabase;
import com.example.taras.reminerapp.db.model.Remind;

import java.lang.ref.WeakReference;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setTitle("Reminder");

        AppDatabase.getInstance();

        //TODO: IDEAS: 1)make reminds to star like favorite; 2) make on main activity 3 square vertical buttons: "My Reminds", "Content Reminds", "Calendar Reminds - Coming Soon";
        //TODO:

        setSupportActionBar(mBinding.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mBinding.drawerLayout,
                mBinding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mBinding.navView.setNavigationItemSelectedListener(this);

//        PageAdapter adapter = new PageAdapter(getSupportFragmentManager());
//        adapter
//                .add("News", NewsFragment.newInstance())
//                .add("Events", EventFragment.newInstance())
//                .add("Video", VideoFragmentJava.newInstance());
//        mBinding.viewPager.setAdapter(adapter);
//        mBinding.tabs.setupWithViewPager(mBinding.viewPager);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, RemindFragment.newInstance())
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
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Global search
                break;
            case R.id.action_create_remind:
                // CreateRemindActivity
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

    public static class Task extends AsyncTask<Void, Void, List<Remind>> {

        private WeakReference<OnRemindListener> mListener;

        public interface OnRemindListener {
            void onGetReminds(List<Remind> list);
        }

        private Task(OnRemindListener listener) {
            mListener = new WeakReference<>(listener);
        }

        public static void requestTask(OnRemindListener listener) {
            new Task(listener).execute();
        }

        @Override
        protected List<Remind> doInBackground(Void... voids) {

            return null;
        }

        @Override
        protected void onPostExecute(List<Remind> list) {
            if (mListener != null) {
                OnRemindListener listener = mListener.get();
                if (listener != null) {
                    listener.onGetReminds(list);
                }
            }
        }
    }
}