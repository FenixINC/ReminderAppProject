package com.example.taras.reminerapp.content;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taras.reminerapp.R;
import com.example.taras.reminerapp.databinding.FragmentContentBinding;

/**
 * Created by Taras Koloshmatin on 19.07.2018
 */
public class VideoFragmentJava extends Fragment {

    private FragmentContentBinding mBinding;

    public static VideoFragmentJava newInstance() {
        return new VideoFragmentJava();
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

    }
}
