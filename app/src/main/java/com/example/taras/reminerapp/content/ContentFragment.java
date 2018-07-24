package com.example.taras.reminerapp.content;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taras.reminerapp.PageAdapter;
import com.example.taras.reminerapp.R;
import com.example.taras.reminerapp.databinding.FragmentContentBinding;

/**
 * Created by Taras Koloshmatin on 20.07.2018
 */
public class ContentFragment extends Fragment {

    private FragmentContentBinding mBinding;

    public static ContentFragment newInstance() {
        return new ContentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_content, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        PageAdapter adapter = new PageAdapter(getChildFragmentManager());
//        adapter
//                .add("News", NewsFragmentJava.newInstance())
//                .add("Events", EventFragmentJava.newInstance())
//                .add("Video", VideoFragmentJava.newInstance());
//        mBinding.viewPager.setAdapter(adapter);
//        mBinding.tabs.setupWithViewPager(mBinding.viewPager);
    }
}
