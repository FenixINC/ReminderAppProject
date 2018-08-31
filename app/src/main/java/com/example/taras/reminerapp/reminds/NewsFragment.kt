package com.example.taras.reminerapp.reminds

import android.arch.lifecycle.*
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.taras.reminerapp.data.RemindViewModel
import com.example.taras.reminerapp.databinding.FragmentContentBinding
import com.example.taras.reminerapp.db.AppDatabase
import com.example.taras.reminerapp.db.Constants
import com.example.taras.reminerapp.db.model.Remind
import com.example.taras.reminerapp.db.service.RemindService
import com.example.taras.reminerapp.db.service.ServiceGenerator
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.lang.ref.WeakReference

/**
 * Created by Taras Koloshmatin on 24.07.2018
 */
class NewsFragment : Fragment(), OnRemindClickListener, LifecycleObserver {

    private lateinit var mBinding: FragmentContentBinding
    private lateinit var mAdapter: RemindAdapter
    private lateinit var mRemindViewModel: RemindViewModel

    companion object {
        @JvmStatic
        fun newInstance(): NewsFragment {
            return NewsFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRemindViewModel = ViewModelProviders.of(activity!!).get(RemindViewModel::class.java)
        mRemindViewModel.restoreState(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentContentBinding.inflate(inflater, container, false)
        mAdapter = RemindAdapter(this)
        lifecycle.addObserver(this)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.swipeRefresh.isRefreshing = true

        val rv: RecyclerView = mBinding.recyclerView
        rv.layoutManager = LinearLayoutManager(activity?.applicationContext) as RecyclerView.LayoutManager?
        rv.setHasFixedSize(true)
        rv.adapter = mAdapter

        mBinding.swipeRefresh.setOnRefreshListener {
            refreshNewsTask()
        }
        setNewsList()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mRemindViewModel.saveState(outState)
    }

    override fun onDestroyView() {
        lifecycle.removeObserver(this)
        super.onDestroyView()
    }

    private fun setNewsList() {
//        mRemindViewModel = ViewModelProviders.of(activity!!).get(RemindViewModel::class.java)
        mRemindViewModel.getNewsList().observe(this@NewsFragment, Observer { mAdapter.setList(it as List<Remind>) })
        mBinding.swipeRefresh.isRefreshing = false
    }

    override fun onModelClick(model: Remind?) {
        Timber.d("Clicked model: ${model?.toString()}")
        mRemindViewModel.setStar(model!!)
//        if (model != null) {
//            fragmentManager
//                    ?.beginTransaction()
//                    ?.replace(R.id.content, RemindDetailsFragment.newInstance(model))
//                    ?.commit()
//        }
    }

    override fun onStarClick(model: Remind?, position: Int) {
//        if (model != null) {
//            mRemindViewModel.setStar(model)
//            mAdapter.setStarOn(true, position, model)
//        }
    }

    override fun onDotsClick(model: Remind?, position: Int) {
        Timber.d(model.toString())
    }


    private fun refreshNewsTask() {
        doAsync {
            val weakReference: WeakReference<NewsFragment> = WeakReference(this@NewsFragment)
            var list: List<Remind>? = null

            if (weakReference.get() != null && weakReference.get()!!.isVisible) {
                try {
                    val response: Response<List<Remind>> = ServiceGenerator.createService(RemindService::class.java)
                            .getListByType(Constants.TYPE_NEWS).execute()

                    if (response.isSuccessful) {
                        list = response.body()!!
                        AppDatabase.getInstance().remindDao().deleteByType(Constants.TYPE_NEWS)
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


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun eventOnCreate() {
        Timber.d("ON_CREATE")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun eventOnStart() {
        Timber.d("ON_START")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun eventOnResume() {
        Timber.d("ON_RESUME")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun eventOnPause() {
        Timber.d("ON_PAUSE")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun eventOnStop() {
        Timber.d("ON_STOP")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun eventOnDestroy() {
        Timber.d("ON_DESTROY")
    }
}