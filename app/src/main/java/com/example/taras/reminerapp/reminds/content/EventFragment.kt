package com.example.taras.reminerapp.reminds.content

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.taras.reminerapp.R
import com.example.taras.reminerapp.data.RemindViewModel
import com.example.taras.reminerapp.databinding.FragmentContentBinding
import com.example.taras.reminerapp.db.model.Remind
import com.example.taras.reminerapp.reminds.OnRemindClickListener
import com.example.taras.reminerapp.reminds.RemindAdapter
import timber.log.Timber

/**
 * Created by Taras Koloshmatin on 24.07.2018
 */
class EventFragment : Fragment(), OnRemindClickListener {

    private lateinit var mBinding: FragmentContentBinding
    private lateinit var mAdapter: RemindAdapter

    companion object Fragment {
        @JvmStatic // writing 'EventFragment.Fragment.newInstance' changes into 'EventFragment.newInstance'
        fun newInstance(): EventFragment {
            val args: Bundle = Bundle()
//            args.putParcelableArrayList(ARG_CAUGHT, someList)
            val fragment = EventFragment()
//            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_content, container, false)
        mAdapter = RemindAdapter(this)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.toolbar.visibility = View.GONE
        mBinding.swipeRefresh.isRefreshing = true

        val rv: RecyclerView = mBinding.recyclerView
        rv.layoutManager = LinearLayoutManager(activity?.applicationContext) as RecyclerView.LayoutManager?
        rv.setHasFixedSize(true)
        rv.adapter = mAdapter

        mBinding.swipeRefresh.setOnRefreshListener {
            setEventList()
        }
        setEventList()
    }

    private fun setEventList() {
        val remindViewModel: RemindViewModel = ViewModelProviders.of(this@EventFragment).get(RemindViewModel::class.java)
        remindViewModel.getEventList().observe(this@EventFragment, Observer { mAdapter.setList(it as List<Remind>) })
        mBinding.swipeRefresh.isRefreshing = false
    }

    override fun onModelClick(model: Remind?) {
        Timber.d("Clicked model: ${model?.toString()}")
    }

    override fun onStarClick(model: Remind?, position: Int) {
        Timber.d(model.toString())
    }
}