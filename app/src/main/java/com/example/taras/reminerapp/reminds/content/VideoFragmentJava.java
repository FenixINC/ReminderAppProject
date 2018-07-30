package com.example.taras.reminerapp.reminds.content;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taras.reminerapp.R;
import com.example.taras.reminerapp.databinding.FragmentContentBinding;
import com.example.taras.reminerapp.db.AppDatabase;
import com.example.taras.reminerapp.db.Constants;
import com.example.taras.reminerapp.db.model.Remind;
import com.example.taras.reminerapp.db.service.RemindService;
import com.example.taras.reminerapp.db.service.ServiceGenerator;
import com.example.taras.reminerapp.reminds.OnRemindClickListener;
import com.example.taras.reminerapp.reminds.RemindAdapter;
import com.example.taras.reminerapp.reminds.RemindDetails;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Taras Koloshmatin on 19.07.2018
 */
public class VideoFragmentJava extends Fragment implements OnRemindClickListener {

    private FragmentContentBinding mBinding;
    private RemindAdapter mAdapter;

    public static VideoFragmentJava newInstance() {
        return new VideoFragmentJava();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentContentBinding.inflate(inflater, container, false);
        mAdapter = new RemindAdapter(this);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.toolbar.setVisibility(View.GONE);

        RecyclerView rv = mBinding.recyclerView;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setHasFixedSize(true);
        rv.setAdapter(mAdapter);

        mBinding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshVideosTask.requestTask(VideoFragmentJava.this);
            }
        });

        GetVideosTask.requestTask(VideoFragmentJava.this);
    }


    @Override
    public void onModelClick(Remind model) {
        Timber.d("Clicked model: %s", model.toString());
//        getFragmentManager()
//                .beginTransaction()
//                .replace(R.id.frame_container, RemindDetails.newInstance(model))
//                .commit();
    }


    private void setList(List<Remind> list) {
        mBinding.swipeRefresh.setRefreshing(false);
        mAdapter.setList(list);
    }

    private static class GetVideosTask extends AsyncTask<Void, Void, List<Remind>> {

        private WeakReference<VideoFragmentJava> mWeakref;

        private GetVideosTask(VideoFragmentJava fragmentJava) {
            mWeakref = new WeakReference<>(fragmentJava);
        }

        public static void requestTask(VideoFragmentJava fragmentJava) {
            new GetVideosTask(fragmentJava).execute();
        }

        @Override
        protected List<Remind> doInBackground(Void... voids) {
            return AppDatabase.getInstance().remindDao().getListByType(Constants.TYPE_VIDEO);
        }

        @Override
        protected void onPostExecute(List<Remind> list) {
            if (mWeakref.get() != null && mWeakref.get().isVisible()) {
                mWeakref.get().setList(list);
            }
        }
    }

    private static class RefreshVideosTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<VideoFragmentJava> mWeakref;
        private List<Remind> mList = new ArrayList<>();


        private RefreshVideosTask(VideoFragmentJava fragment) {
            mWeakref = new WeakReference<>(fragment);
        }

        static void requestTask(VideoFragmentJava fragment) {
            new RefreshVideosTask(fragment).execute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Call<List<Remind>> call = ServiceGenerator.SERVICE.createService(RemindService.class)
                    .getListByType(Constants.TYPE_VIDEO);
            try {
                Response<List<Remind>> response = call.execute();
                if (response.isSuccessful()) {
                    List<Remind> list = response.body();
                    if (list != null) {
                        AppDatabase.getInstance().remindDao().deleteByType(Constants.TYPE_VIDEO);
                        AppDatabase.getInstance().remindDao().insert(list);
                        mList.addAll(list);
                    }
                }
            } catch (IOException e) {
                Timber.e("Failed load reminds! " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            VideoFragmentJava fragment = mWeakref.get();
            if (fragment != null && fragment.isVisible()) {
                fragment.setList(mList);
            }
        }
    }
}
