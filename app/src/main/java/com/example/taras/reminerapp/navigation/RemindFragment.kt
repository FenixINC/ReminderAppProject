package com.example.taras.reminerapp.navigation

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.taras.reminerapp.R
import com.example.taras.reminerapp.reminds.MyRemindsFragment
import com.example.taras.reminerapp.databinding.FragmentRemindBinding

/**
 * Created by Taras Koloshmatin on 28.07.2018
 */
class RemindFragment : Fragment() {

    private lateinit var mBinding: FragmentRemindBinding


    companion object {
        @JvmStatic
        fun newInstance(): RemindFragment {
            return RemindFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentRemindBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.myReminds.setOnClickListener { openFragment(MyRemindsFragment.newInstance()) }
        mBinding.contentReminds.setOnClickListener { Toast.makeText(context, "Not yet implemented", Toast.LENGTH_SHORT).show() }
        mBinding.calendarReminds.setOnClickListener { Toast.makeText(context, "Not yet implemented", Toast.LENGTH_SHORT).show() }
    }

    private fun openFragment(fragment: Fragment) {
        fragmentManager?.beginTransaction()
                ?.replace(R.id.frame_container, fragment)
                ?.addToBackStack(null)
                ?.commit()
    }
}