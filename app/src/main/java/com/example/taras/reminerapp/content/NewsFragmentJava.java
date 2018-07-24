package com.example.taras.reminerapp.content;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.taras.reminerapp.R;
import com.example.taras.reminerapp.databinding.FragmentContentBinding;
import com.example.taras.reminerapp.db.model.News;
import com.example.taras.reminerapp.db.model.Remind;

import java.util.ArrayList;

/**
 * Created by Taras Koloshmatin on 19.07.2018
 */
public class NewsFragmentJava extends Fragment implements OnRemindClickListener {

    private FragmentContentBinding mBinding;
    private ContentAdapter mAdapter;

    public static NewsFragmentJava newInstance() {
        return new NewsFragmentJava();
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
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setHasFixedSize(true);
        rv.setAdapter(mAdapter);

        ArrayList<News> list = new ArrayList<>();
        list.add(new News("Title 1", "Description 1"));
        list.add(new News("Title 2", "Description 2"));
        list.add(new News("Title 3", "Description 3"));
        list.add(new News("Title 4", "Description 4"));
        list.add(new News("Title 5", "Description 5"));
        list.add(new News("Title 6", "Description 6"));

        mAdapter.setList(list);
    }

    @Override
    public void onModelClick(Remind model) {
        Toast.makeText(getContext(), model.title, Toast.LENGTH_LONG).show();
    }
}