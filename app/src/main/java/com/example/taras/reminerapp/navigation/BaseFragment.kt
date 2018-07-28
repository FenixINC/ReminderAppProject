package com.example.taras.reminerapp.navigation

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.*
import com.example.taras.reminerapp.R
import timber.log.Timber

/**
 * Created by Taras Koloshmatin on 28.07.2018
 */
abstract class BaseFragment : Fragment() {

    protected lateinit var mToolbar: Toolbar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = onCreateFragmentView(inflater, container, savedInstanceState)
        mToolbar = view.findViewById(R.id.toolbar)
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(R.drawable.ic_arrow_white)
            mToolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        } else {
            Timber.w("Toolbar is NULL")
        }
        return view
    }

    abstract fun onCreateFragmentView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View

    private fun isToolbar(): Boolean {
        return mToolbar != null
    }

    protected fun setTitle(title: CharSequence) {
        if (isToolbar()) {
            mToolbar.title = title
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)
        activity?.menuInflater?.inflate(R.menu.menu, menu)
        mToolbar.inflateMenu(R.menu.menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
            }
            R.id.action_create_remind -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }
}