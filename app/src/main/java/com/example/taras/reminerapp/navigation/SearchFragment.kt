package com.example.taras.reminerapp.navigation

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.taras.reminerapp.BaseFragment
import com.example.taras.reminerapp.data.RemindViewModel
import com.example.taras.reminerapp.databinding.FragmentContentBinding
import com.example.taras.reminerapp.db.model.Remind
import com.example.taras.reminerapp.reminds.OnRemindClickListener
import com.example.taras.reminerapp.reminds.RemindAdapter
import timber.log.Timber

/**
 * Created by Taras Koloshmatin on 02.08.2018
 */
class SearchFragment : BaseFragment(), OnRemindClickListener {

    private lateinit var mBinding: FragmentContentBinding
    private lateinit var mAdapter: RemindAdapter
    private lateinit var mRemindViewModel: RemindViewModel

    companion object {
        @JvmStatic
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRemindViewModel = ViewModelProviders.of(activity!!).get(RemindViewModel::class.java)
    }

    override fun onCreateFragmentView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentContentBinding.inflate(inflater, container, false)
        mAdapter = RemindAdapter(this)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle("Search")

        mBinding.toolbar.visibility = View.VISIBLE
        mBinding.swipeRefresh.isRefreshing = false

        val rv: RecyclerView = mBinding.recyclerView
        rv.layoutManager = LinearLayoutManager(activity?.applicationContext) as RecyclerView.LayoutManager
        rv.setHasFixedSize(true)
        rv.adapter = mAdapter

        mBinding.swipeRefresh.setOnRefreshListener { setSearchList() }

        setSearchList()
    }

    private fun setSearchList() {

        mBinding.swipeRefresh.isRefreshing = false
    }

    override fun onModelClick(model: Remind?) {
        Timber.d(model.toString())
    }

    override fun onStarClick(model: Remind?, position: Int) {
        Timber.d(model.toString())
    }
}