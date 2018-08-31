package com.example.taras.reminerapp.reminds

import android.arch.lifecycle.*
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.taras.reminerapp.R
import com.example.taras.reminerapp.data.RemindViewModel
import com.example.taras.reminerapp.databinding.FragmentContentBinding
import com.example.taras.reminerapp.db.AppDatabase
import com.example.taras.reminerapp.db.Constants
import com.example.taras.reminerapp.db.model.Remind
import com.example.taras.reminerapp.db.service.RemindService
import com.example.taras.reminerapp.db.service.ServiceGenerator
import okhttp3.ResponseBody
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.alert
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.lang.ref.WeakReference

/**
 * Created by Taras Koloshmatin on 28.07.2018
 */
class MyRemindsFragment : Fragment(), OnRemindClickListener, LifecycleObserver {

    private lateinit var mBinding: FragmentContentBinding
    private lateinit var mAdapter: RemindAdapter
    private lateinit var mRemindViewModel: RemindViewModel

    private lateinit var mDialog: DialogInterface

    companion object {
        @JvmStatic
        fun newInstance(): MyRemindsFragment {
            return MyRemindsFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRemindViewModel = ViewModelProviders.of(activity!!).get(RemindViewModel::class.java)
        mRemindViewModel.restoreState(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentContentBinding.inflate(inflater, container, false)
        mAdapter = RemindAdapter(this)
        lifecycle.addObserver(this)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.showBtnCreate = true
        mBinding.swipeRefresh.isRefreshing = true

        val rv: RecyclerView = mBinding.recyclerView
        rv.layoutManager = LinearLayoutManager(activity?.applicationContext) as RecyclerView.LayoutManager?
        rv.setHasFixedSize(true)
        rv.adapter = mAdapter

        mBinding.btnCreate.onClick {
            val dialog = DialogCreateRemind.newInstance(Constants.SERVER_DEFAULT)
            dialog.setTargetFragment(this@MyRemindsFragment, 1)
            dialog.show(fragmentManager, "create-remind-dialog")
        }

        mBinding.swipeRefresh.setOnRefreshListener {
            refreshUserRemindsTask()
        }
//        refreshUserRemindsTask()
        setUserRemindList()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mRemindViewModel.saveState(outState)
    }

    override fun onDestroyView() {
        lifecycle.removeObserver(this)
        super.onDestroyView()
    }

    override fun onModelClick(model: Remind?) {
        Timber.d("Clicked model: ${model?.toString()}")
    }

    override fun onStarClick(model: Remind?, position: Int) {
        Timber.d(model.toString())
    }

    override fun onDotsClick(model: Remind, position: Int) {
        Timber.d(model.toString())
        dotsAlertDialog(model)
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
                if (list != null && list.isNotEmpty()) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == 1) {
            val isRefresh = data?.getBooleanExtra("is_refresh", false)
            if (isRefresh != null && isRefresh) {
                refreshUserRemindsTask()
            }
        }
    }

    private fun dotsAlertDialog(remind: Remind) {
        mDialog = alert {
            customView {
                linearLayout {
                    orientation = LinearLayout.VERTICAL
                    padding = dip(10)

                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL

                        imageView {
                            background = ResourcesCompat.getDrawable(resources, R.drawable.icon_star_off, null)
                        }.lparams(
                                width = dip(32),
                                height = dip(32)
                        )
                        textView {
                            text = "Star"
                            textSize = sp(8).toFloat()
                            textColor = ResourcesCompat.getColor(resources, R.color.text, null)
                            gravity
                        }.lparams(
                                width = matchParent,
                                height = wrapContent
                        ) {
                            marginStart = dip(5)
                        }

                        onClick {

                        }
                    }

                    view {
                        backgroundColor = ResourcesCompat.getColor(resources, R.color.grey, null)
                    }.lparams(
                            width = matchParent,
                            height = dip(1)
                    ) {
                        topMargin = dip(10)
                    }

                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL

                        imageView {
                            background = ResourcesCompat.getDrawable(resources, R.drawable.ic_update, null)
                        }.lparams(
                                width = dip(32),
                                height = dip(32)
                        )
                        textView {
                            text = "Update"
                            textSize = sp(8).toFloat()
                            textColor = ResourcesCompat.getColor(resources, R.color.text, null)
                        }.lparams(
                                width = matchParent,
                                height = wrapContent
                        ) {
                            marginStart = dip(5)
                        }

                        onClick {

                        }
                    }.lparams {
                        topMargin = dip(10)
                    }

                    view {
                        backgroundColor = ResourcesCompat.getColor(resources, R.color.grey, null)
                    }.lparams(
                            width = matchParent,
                            height = dip(1)
                    ) {
                        topMargin = dip(10)
                    }

                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL

                        imageView {
                            background = ResourcesCompat.getDrawable(resources, R.drawable.ic_delete, null)
                        }.lparams(
                                width = dip(32),
                                height = dip(32)
                        )
                        textView {
                            text = "Delete"
                            textSize = sp(8).toFloat()
                            textColor = ResourcesCompat.getColor(resources, R.color.text, null)
                        }.lparams(
                                width = matchParent,
                                height = wrapContent
                        ) {
                            marginStart = dip(5)
                        }

                        onClick {
                            val call: Call<ResponseBody> = ServiceGenerator.createService(RemindService::class.java).delete(remind)
                            call.enqueue(object : Callback<ResponseBody> {
                                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                    if (response.isSuccessful) {
                                        Timber.d("Delete remind response successful.")
                                        doAsync {
                                            AppDatabase.getInstance().remindDao().delete(remind)
                                            uiThread {
                                                mAdapter.deleteRemind(remind, mAdapter.getList())
                                                alert(response.body()?.string()?.replace("\"", "").toString()) {
                                                    okButton {
                                                        mDialog.dismiss()
                                                    }
                                                }.show()
                                            }
                                        }
                                    } else {
                                        alert("Error during deleting remind!") {
                                            okButton {
                                                mDialog.dismiss()
                                            }
                                        }.show()
                                    }
                                }

                                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                                    Timber.w("Cannot delete remind! ${t?.message}")
                                    alert("Server unavailable!") {
                                        okButton {
                                            mDialog.dismiss()
                                        }
                                    }.show()
                                }

                            })
                        }
                    }.lparams {
                        topMargin = dip(10)
                    }
                }
            }
        }.show()
    }
}