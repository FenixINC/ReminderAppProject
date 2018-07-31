package com.example.taras.reminerapp.reminds.my

import android.arch.lifecycle.Observer
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
import com.example.taras.reminerapp.db.AppDatabase
import com.example.taras.reminerapp.db.Constants
import com.example.taras.reminerapp.db.model.Remind
import com.example.taras.reminerapp.db.service.RemindService
import com.example.taras.reminerapp.db.service.ServiceGenerator
import com.example.taras.reminerapp.reminds.OnRemindClickListener
import com.example.taras.reminerapp.reminds.RemindAdapter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.lang.ref.WeakReference

/**
 * Created by Taras Koloshmatin on 28.07.2018
 */
class MyRemindsFragment : BaseFragment(), OnRemindClickListener {

    private lateinit var mBinding: FragmentContentBinding
    private lateinit var mAdapter: RemindAdapter


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
        mBinding = FragmentContentBinding.inflate(inflater, container, false)
        mAdapter = RemindAdapter(this)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle("My Reminds")

        mBinding.toolbar.visibility = View.VISIBLE
        mBinding.swipeRefresh.isRefreshing = true

        val rv: RecyclerView = mBinding.recyclerView
        rv.layoutManager = LinearLayoutManager(activity?.applicationContext) as RecyclerView.LayoutManager?
        rv.setHasFixedSize(true)
        rv.adapter = mAdapter

        mBinding.swipeRefresh.setOnRefreshListener {
            setUserRemindList()
        }
        setUserRemindList()
    }

    override fun onModelClick(model: Remind?) {
        Timber.d("Clicked model: ${model?.toString()}")
    }

    private fun setUserRemindList() {
        val remindViewModel: RemindViewModel = ViewModelProviders.of(this@MyRemindsFragment).get(RemindViewModel::class.java)
        remindViewModel.getUserRemindList().observe(this@MyRemindsFragment, Observer { mAdapter.setList(it as List<Remind>) })
        mBinding.swipeRefresh.isRefreshing = false
    }

    private fun refreshUserRemindsTask() {
        doAsync {
            val weakReference: WeakReference<MyRemindsFragment> = WeakReference(this@MyRemindsFragment)
            var list: List<Remind>? = null

            if (weakReference.get() != null && weakReference.get()!!.isVisible) {
                try {
                    val response: Response<List<Remind>> = ServiceGenerator.createService(RemindService::class.java)
                            .getListByType(Constants.TYPE_USER_REMIND).execute()

                    if (response.isSuccessful) {
                        list = response.body()!!
                        AppDatabase.getInstance().remindDao().deleteByType(Constants.TYPE_USER_REMIND)
                        AppDatabase.getInstance().remindDao().insert(list)
                    } else {
                        Timber.e("Error loading reminds: ${response.code()}")
                    }
                } catch (e: IOException) {
                    Timber.e("Failed load reminds! ${e.message}")
                }
            } else {
                Timber.d("Fragment weakReference null or not visible!")
            }
            uiThread {
                mBinding.swipeRefresh.isRefreshing = false
                if (list != null) {
                    mAdapter.setList(list)
                } else {
                    Timber.d("Null list!")
                }
            }
        }
    }
}