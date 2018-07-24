package com.example.taras.reminerapp.content

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.taras.reminerapp.R
import com.example.taras.reminerapp.databinding.FragmentContentBinding

/**
 * Created by Taras Koloshmatin on 24.07.2018
 */
class EventFragment : Fragment() {

    private lateinit var mBinding: FragmentContentBinding

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
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}