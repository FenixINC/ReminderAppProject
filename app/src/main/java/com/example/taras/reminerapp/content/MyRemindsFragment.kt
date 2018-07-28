package com.example.taras.reminerapp.content

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.taras.reminerapp.navigation.BaseFragment
import com.example.taras.reminerapp.PageAdapter
import com.example.taras.reminerapp.databinding.FragmentPagerBinding

/**
 * Created by Taras Koloshmatin on 28.07.2018
 */
class MyRemindsFragment : BaseFragment() {

    lateinit var mBinding: FragmentPagerBinding


    companion object {
        @JvmStatic
        fun newInstance(): MyRemindsFragment {
            return MyRemindsFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateFragmentView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentPagerBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle("My Reminds")

        var adapter = PageAdapter(childFragmentManager)
        adapter
                .add("News", NewsFragment.newInstance())
                .add("Events", EventFragment.newInstance())
                .add("Video", VideoFragmentJava.newInstance());
        mBinding.viewPager.adapter = adapter
        mBinding.viewPager.offscreenPageLimit = 3
        mBinding.tabs.tabMode = TabLayout.MODE_FIXED
        mBinding.tabs.tabGravity = TabLayout.GRAVITY_FILL
        mBinding.tabs.setupWithViewPager(mBinding.viewPager)

    }
}