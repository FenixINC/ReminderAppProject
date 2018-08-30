package com.example.taras.reminerapp.navigation

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.taras.reminerapp.OnPageListener
import com.example.taras.reminerapp.databinding.FragmentRemindMainBinding
import com.example.taras.reminerapp.reminds.UserRemindsFragment
import com.example.taras.reminerapp.search.SearchFragment
import org.jetbrains.anko.support.v4.toast

/**
 * Created by Taras Koloshmatin on 28.07.2018
 */
class MainRemindFragment : Fragment() {

    private lateinit var mBinding: FragmentRemindMainBinding

    companion object {
        @JvmStatic
        fun newInstance(): MainRemindFragment {
            return MainRemindFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentRemindMainBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.reminds.setOnClickListener { openFragment(UserRemindsFragment.newInstance()) }
        mBinding.calendarReminds.setOnClickListener { toast("Not yet implemented") }
        mBinding.search.setOnClickListener { openFragment(SearchFragment.newInstance()) }
    }

    private fun openFragment(fragment: Fragment) {
        if (activity is OnPageListener) {
            (activity as OnPageListener).onPageNavigation(fragment)
        }
    }
}