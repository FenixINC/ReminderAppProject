package com.example.taras.reminerapp.reminds.stars

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.databinding.library.baseAdapters.BR
import com.example.taras.reminerapp.BaseFragment
import com.example.taras.reminerapp.R
import com.example.taras.reminerapp.data.RemindViewModel
import com.example.taras.reminerapp.databinding.FragmentContentBinding
import com.example.taras.reminerapp.db.model.Remind
import timber.log.Timber

/**
 * Created by Taras Koloshmatin on 01.08.2018
 */
class StarsFragment : BaseFragment() {

    private lateinit var mBinding: FragmentContentBinding
    private lateinit var mAdapter: StarsAdapter
    private lateinit var mRemindViewModel: RemindViewModel

    companion object {
        @JvmStatic
        fun newInstance(): StarsFragment {
            return StarsFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRemindViewModel = ViewModelProviders.of(activity!!).get(RemindViewModel::class.java)
    }

    override fun onCreateFragmentView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentContentBinding.inflate(inflater, container, false)
        mAdapter = StarsAdapter()
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle("Stars Reminds")

        mBinding.toolbar.visibility = View.VISIBLE
        mBinding.swipeRefresh.isRefreshing = true

        val rv: RecyclerView = mBinding.recyclerView
        rv.layoutManager = LinearLayoutManager(activity?.applicationContext) as RecyclerView.LayoutManager
        rv.setHasFixedSize(true)
        rv.adapter = mAdapter

        mBinding.swipeRefresh.setOnRefreshListener { setRemindStarList() }

        setRemindStarList()
    }

    private fun setRemindStarList() {
        mRemindViewModel.getStarList().observe(this@StarsFragment, Observer { mAdapter.setList(it as List<Remind>) })
        mBinding.swipeRefresh.isRefreshing = false
    }


    private class StarsAdapter : RecyclerView.Adapter<StarsAdapter.ViewHolder>() {

        private val mList: ArrayList<Remind> = ArrayList()

        fun setList(list: List<Remind>) {
            if (list == null) {
                Timber.d("Null list")
                return
            }
            mList.clear()
            mList.addAll(list)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StarsAdapter.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val holder: ViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.item_remind_star, parent, false)
            return ViewHolder(holder)
        }

        override fun getItemCount(): Int {
            return mList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(mList[position])
        }

        class ViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(data: Any) {
                binding.setVariable(BR.model, data)
            }
        }
    }
}