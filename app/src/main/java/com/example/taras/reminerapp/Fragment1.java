package com.example.taras.reminerapp;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taras.reminerapp.databinding.Fragment1Binding;

/**
 * Created by Taras Koloshmatin on 19.07.2018
 */
public class Fragment1 extends Fragment {

    public static Fragment1 getInstance() {
        return new Fragment1();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Fragment1Binding binding = DataBindingUtil.inflate(inflater, R.layout.fragment1, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
