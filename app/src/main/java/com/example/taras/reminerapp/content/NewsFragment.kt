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
import com.example.taras.reminerapp.db.AppDatabase
import com.example.taras.reminerapp.db.Constants
import com.example.taras.reminerapp.db.model.Remind
import com.example.taras.reminerapp.db.service.RemindService
import com.example.taras.reminerapp.db.service.ServiceGenerator
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Response
import timber.log.Timber
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_content, container, false)
        mAdapter = ContentAdapter(this)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv: RecyclerView = mBinding.recyclerView
        rv.layoutManager = LinearLayoutManager(activity?.applicationContext) as RecyclerView.LayoutManager?
        rv.setHasFixedSize(true)
        rv.adapter = mAdapter

        getNewsTask(this)
    }

    override fun onModelClick(model: Remind?) {
        Timber.d("Clicked model: $model")
        Toast.makeText(context, model?.title, Toast.LENGTH_LONG).show()
    }


    private fun getNewsTask(fragment: NewsFragment) {
        doAsync {
            val database: AppDatabase = AppDatabase.getInstance()
            val list: List<Remind> = database.remindDao().getListByType(Constants.TYPE_NEWS)
            uiThread {
                mAdapter.setList(list)
            }
        }
    }

    private fun refreshNewsTask(fragment: NewsFragment) {
        doAsync {
            val weakReference: WeakReference<NewsFragment> = WeakReference(fragment)
            var list: List<Remind> = ArrayList<Remind>()

            val frag: NewsFragment = weakReference.get()!!
            if (frag != null && frag.isVisible) {
                //TODO: create here and on a server getNewsList by TYPE_REMIND!
                val response: Response<List<Remind>> = ServiceGenerator.createService(RemindService::class.java).getList().execute()

                if (response.isSuccessful) {
                    list = response.body()!!
                    AppDatabase.getInstance().remindDao().delete()
                } else {
                    Timber.d("Error loading reminds!", response.code())
                }

                uiThread {
                    mAdapter.setList(list)
                }
            }
        }
    }
}