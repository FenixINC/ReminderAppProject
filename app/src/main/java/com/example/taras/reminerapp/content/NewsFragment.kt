package com.example.taras.reminerapp.content

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.taras.reminerapp.R
import com.example.taras.reminerapp.databinding.FragmentContentBinding
import com.example.taras.reminerapp.db.model.News
import com.example.taras.reminerapp.db.model.Remind
import timber.log.Timber

/**
 * Created by Taras Koloshmatin on 24.07.2018
 */
class NewsFragment : Fragment(), OnRemindClickListener {

    private lateinit var mBinding: FragmentContentBinding
    private lateinit var mAdapter: ContentAdapter

    companion object {
        @JvmStatic
        fun newInstance(): NewsFragment {
            val args: Bundle = Bundle()
//            args.putParcelableArrayList(ARG_CAUGHT, someList)
            val fragment = NewsFragment()
//            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_content, container, false)
        mAdapter = ContentAdapter(this)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv: RecyclerView = mBinding.recyclerView
        rv.layoutManager = LinearLayoutManager(activity!!) as RecyclerView.LayoutManager?
        rv.setHasFixedSize(true)
        rv.adapter = mAdapter

        var list: ArrayList<News> = ArrayList()
        list.add(News("Title 1", "Description 1"))
        list.add(News("Title 2", "Description 2"))
        list.add(News("Title 3", "Description 3"))
        list.add(News("Title 4", "Description 4"))
        list.add(News("Title 5", "Description 5"))
        list.add(News("Title 6", "Description 6"))

        mAdapter.setList(list)
    }

    override fun onModelClick(model: Remind?) {
        Timber.d("Clicked model: $model")
        Toast.makeText(context, model?.title, Toast.LENGTH_LONG).show()
    }
}