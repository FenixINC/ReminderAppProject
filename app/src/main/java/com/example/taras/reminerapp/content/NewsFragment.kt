package com.example.taras.reminerapp.content

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        mBinding = FragmentContentBinding.inflate(inflater, container, false)
        mAdapter = ContentAdapter(this)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv: RecyclerView = mBinding.recyclerView
        rv.layoutManager = LinearLayoutManager(activity?.applicationContext) as RecyclerView.LayoutManager?
        rv.setHasFixedSize(true)
        rv.adapter = mAdapter

        mBinding.swipeRefresh.setOnRefreshListener {
            refreshNewsTask()
        }

        refreshNewsTask()
    }

    override fun onModelClick(model: Remind?) {
        Timber.d("Clicked model: $model")
        Toast.makeText(context, model?.title, Toast.LENGTH_LONG).show()
    }

    private fun getNewsTask() {
        doAsync {
            val database: AppDatabase = AppDatabase.getInstance()
            val list: List<Remind> = database.remindDao().getListByType(Constants.TYPE_NEWS)
            uiThread {
                mAdapter.setList(list)
            }
        }
    }

    private fun refreshNewsTask() {

        doAsync {
            val weakReference: WeakReference<NewsFragment> = WeakReference(this@NewsFragment)
            var list: List<Remind>? = null

            if (weakReference != null && weakReference.get()!!.isVisible) {
                try {
                    val response: Response<List<Remind>> = ServiceGenerator.createService(RemindService::class.java)
                            .getListByType(Constants.TYPE_NEWS).execute()
                    if (response.isSuccessful) {
                        list = response.body()!!
                        AppDatabase.getInstance().remindDao().deleteByType(Constants.TYPE_NEWS)
                        AppDatabase.getInstance().remindDao().insert(list)
                    } else {
                        Timber.d("Error loading reminds: ${response.code()}")
                    }
                } catch (e: IOException) {
                    Timber.e("Failed load reminds! ${e.message}")
                }

            } else {
                Timber.d("WeakReference not or not visible!")
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