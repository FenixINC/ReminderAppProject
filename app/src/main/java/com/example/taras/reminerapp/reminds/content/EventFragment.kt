package com.example.taras.reminerapp.reminds.content

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
import com.example.taras.reminerapp.db.model.Remind
import com.example.taras.reminerapp.reminds.RemindAdapter
import com.example.taras.reminerapp.reminds.OnRemindClickListener
import timber.log.Timber

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

//        var list = listOf(
//                Remind("Event 1", "Description 1"),
//                Event("Event 2", "Description 2"),
//                Event("Event 3", "Description 3"),
//                Event("Event 4", "Description 4"),
//                Event("Event 5", "Description 5"),
//                Event("Event 6", "Description 6"))

//        mAdapter.setList(list)
    }

    override fun onModelClick(model: Remind?) {
        Timber.d("Clicked model: $model")
        Toast.makeText(context, model?.title, Toast.LENGTH_LONG).show()
    }
}