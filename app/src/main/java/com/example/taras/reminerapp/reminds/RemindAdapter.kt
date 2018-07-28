package com.example.taras.reminerapp.reminds

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.databinding.library.baseAdapters.BR
import com.example.taras.reminerapp.R
import com.example.taras.reminerapp.db.model.Remind
import timber.log.Timber

/**
 * Created by Taras Koloshmatin on 24.07.2018
 */
class RemindAdapter(listener: OnRemindClickListener) : RecyclerView.Adapter<RemindAdapter.ViewHolder>() {

    private var mList: ArrayList<Remind> = ArrayList()
    private var mListener: OnRemindClickListener = listener

    fun setList(list: List<Remind>) {
        if (list == null) {
            Timber.d("Null list")
            return
        }
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Remind {
        return mList[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val holder: ViewDataBinding = when (viewType) {
            R.layout.item_remind -> DataBindingUtil.inflate(inflater, R.layout.item_remind, parent, false)
            else -> {
                Timber.d("Unknown content holder: $viewType")
                DataBindingUtil.inflate(inflater, R.layout.item_empty, parent, false)
            }

        }
        return ViewHolder(holder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), mListener)

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (mList[position]) {
            is Remind -> R.layout.item_remind
            else -> {
                Timber.d("Unknown model: ${mList[position]}")
                R.layout.item_empty
            }
        }
    }

    class ViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Any, listener: OnRemindClickListener) {
            binding.setVariable(BR.model, data)
            binding.setVariable(BR.clickListener, listener)
//            binding.executePendingBindings()
        }
    }

//    class NewsHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(data: Any, listener: OnRemindClickListener) {
//            binding.setVariable(BR.model, data)
//            binding.setVariable(BR.clickListener, listener)
//        }
//    }
}