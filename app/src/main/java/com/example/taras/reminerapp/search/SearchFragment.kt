package com.example.taras.reminerapp.search

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.taras.reminerapp.BaseFragment
import com.example.taras.reminerapp.databinding.FragmentSearchBinding
import com.example.taras.reminerapp.db.AppDatabase
import com.example.taras.reminerapp.db.model.Remind
import com.example.taras.reminerapp.reminds.OnRemindClickListener
import com.example.taras.reminerapp.reminds.RemindAdapter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.uiThread
import timber.log.Timber

/**
 * Created by Taras Koloshmatin on 02.08.2018
 */
class SearchFragment : BaseFragment(), OnRemindClickListener {

    private lateinit var mBinding: FragmentSearchBinding
    private lateinit var mAdapter: RemindAdapter

    companion object {
        @JvmStatic
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateFragmentView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentSearchBinding.inflate(inflater, container, false)
        mAdapter = RemindAdapter(this)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle("Search")

        val rv: RecyclerView = mBinding.recyclerView
        rv.layoutManager = LinearLayoutManager(activity?.applicationContext) as RecyclerView.LayoutManager
        rv.setHasFixedSize(true)
        rv.adapter = mAdapter

        mBinding.btnClear.onClick { mBinding.editSearch.setText("") }
        mBinding.btnSearch.onClick {
            val text: String = mBinding.editSearch.text.toString()
            if (text.length > 2) {
                setSearchList(text)
                if (mAdapter.itemCount == 0) {
                    mBinding.emptyText.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setSearchList(search: String) {
        var list: List<Remind>
        doAsync {
            list = AppDatabase.getInstance().remindDao().getSearchList(search, search)
            uiThread {
                mAdapter.setList(list)
            }
        }
    }

    override fun onModelClick(model: Remind?) {
        Timber.d(model.toString())
    }

    override fun onStarClick(model: Remind?, position: Int) {
        Timber.d(model.toString())
    }

    override fun onDotsClick(model: Remind?, position: Int) {
        Timber.d(model.toString())
    }
}