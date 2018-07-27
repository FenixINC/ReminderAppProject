package com.example.taras.reminerapp.content;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
    private ContentAdapter mAdapter;

    public static VideoFragmentJava newInstance() {
        return new VideoFragmentJava();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_content, container, false);
        mAdapter = new ContentAdapter(this);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv = mBinding.recyclerView;
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setHasFixedSize(true);
        rv.setAdapter(mAdapter);

        Task.requestTask(VideoFragmentJava.this);
    }


    @Override
    public void onModelClick(Remind model) {

    }

    // Task:
    private void setList(List<Remind> list) {
        mAdapter.setList(list);
    }

    private static class Task extends AsyncTask<Void, Void, Void> {

        private WeakReference<VideoFragmentJava> mWeakref;
        private List<Remind> mList = new ArrayList<>();


        private Task(VideoFragmentJava fragment) {
            mWeakref = new WeakReference<>(fragment);
        }

        static void requestTask(VideoFragmentJava fragment) {
            new Task(fragment).execute();
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
