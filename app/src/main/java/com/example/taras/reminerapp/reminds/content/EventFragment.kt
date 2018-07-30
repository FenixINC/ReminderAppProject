package com.example.taras.reminerapp.reminds.content

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.taras.reminerapp.R
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

        val rv: RecyclerView = mBinding.recyclerView
        rv.layoutManager = LinearLayoutManager(activity?.applicationContext) as RecyclerView.LayoutManager?
        rv.setHasFixedSize(true)
        rv.adapter = mAdapter

        mBinding.swipeRefresh.setOnRefreshListener {
            refreshEventTask()
        }

        getEventsTask()
    }

    override fun onModelClick(model: Remind?) {
        Timber.d("Clicked model: ${model?.toString()}")
//        if (model != null) {
//            fragmentManager
//                    ?.beginTransaction()
//                    ?.replace(R.id.frame_container, RemindDetails.newInstance(model))
//                    ?.addToBackStack(null)
//                    ?.commit()
//        }
    }

    private fun getEventsTask() {
        doAsync {
            val list: List<Remind> = AppDatabase.getInstance().remindDao().getListByType(Constants.TYPE_EVENT)
            uiThread {
                mAdapter.setList(list)
            }
        }
    }

    private fun refreshEventTask() {
        doAsync {
            val weakReference: WeakReference<EventFragment> = WeakReference(this@EventFragment)
            var list: List<Remind>? = null

            if (weakReference.get() != null && weakReference.get()!!.isVisible) {
                try {
                    val response: Response<List<Remind>> = ServiceGenerator.createService(RemindService::class.java)
                            .getListByType(Constants.TYPE_EVENT).execute()

                    if (response.isSuccessful) {
                        list = response.body()!!
                        AppDatabase.getInstance().remindDao().deleteByType(Constants.TYPE_EVENT)
                        AppDatabase.getInstance().remindDao().insert(list)
                    } else {
                        Timber.e("Error loading reminds: ${response.code()}")
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