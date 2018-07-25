package com.example.taras.reminerapp.content

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.databinding.library.baseAdapters.BR
import com.example.taras.reminerapp.R
import com.example.taras.reminerapp.databinding.ItemNewsBinding
import com.example.taras.reminerapp.db.model.News

/**
 * Created by Taras Koloshmatin on 24.07.2018
 */
class ContentAdapterK(listener: OnRemindClickListener) : RecyclerView.Adapter<ContentAdapterK.ViewHolder>() {

    private var mList: ArrayList<News> = ArrayList()
    private var mListener: OnRemindClickListener = listener

    fun setList(list: List<News>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val holder: ViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.item_news, parent, false)

        return ViewHolder(holder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position], mListener)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

//    override fun getItemViewType(position: Int): Int {
//        return 0
//    }

    internal interface UpdateViewHolder {
        fun bindViews(news: News)
    }

    class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Any, listener: OnRemindClickListener) {
            binding.setVariable(BR.model, data)
            binding.setVariable(BR.clickListener, listener)
            binding.executePendingBindings()
        }
    }

//    class NewsHolder : ViewHolder() {
//
//    }
}