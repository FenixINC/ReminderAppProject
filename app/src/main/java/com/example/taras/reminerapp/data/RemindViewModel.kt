package com.example.taras.reminerapp.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import com.example.taras.reminerapp.db.AppDatabase
import com.example.taras.reminerapp.db.Constants
import com.example.taras.reminerapp.db.model.Remind
import org.jetbrains.anko.doAsync

/**
 * Created by Taras Koloshmatin on 31.07.2018
 */
class RemindViewModel : ViewModel() {

    private val KEY = "key"

    private var remind = Remind("TITLE", "20.20.20", "DESCRIPTION", "type_view_model")

    private var mRemindList: LiveData<List<Remind>> = AppDatabase.getInstance().remindDao().getList()
    private var mUserRemindList: LiveData<List<Remind>> = AppDatabase.getInstance().remindDao().getListByType(Constants.TYPE_USER_REMIND)
    private var mNewsList: LiveData<List<Remind>> = AppDatabase.getInstance().remindDao().getListByType(Constants.TYPE_NEWS)
    private var mEventList: LiveData<List<Remind>> = AppDatabase.getInstance().remindDao().getListByType(Constants.TYPE_EVENT)
    private var mVideoList: LiveData<List<Remind>> = AppDatabase.getInstance().remindDao().getListByType(Constants.TYPE_VIDEO)

    private var mStarList: MutableLiveData<List<Remind>> = MutableLiveData()

    fun getRemindList(): LiveData<List<Remind>> {
        return mRemindList
    }

    fun getUserRemindList(): LiveData<List<Remind>> {
        return mUserRemindList
    }

    fun getNewsList(): LiveData<List<Remind>> {
        return mNewsList
    }

    fun getEventList(): LiveData<List<Remind>> {
        return mEventList
    }

    fun getVideoList(): LiveData<List<Remind>> {
        return mVideoList
    }


    fun saveState(outState: Bundle) {
        outState.putParcelable(KEY, remind)
    }

    fun restoreState(bundle: Bundle?) {
        bundle?.let {
            remind = bundle.getParcelable(KEY)
            doAsync {
                AppDatabase.getInstance().remindDao().insert(remind)
            }
        }
    }


    fun setStar(remind: Remind) {
        var list: List<Remind> = listOf(remind)
        mStarList.value = list
    }

    fun getStarList(): LiveData<List<Remind>> {
        return mStarList
    }


    class Factory : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RemindViewModel() as T
        }
    }
    /*
        usage:
        mBinding.model = ViewModelProviders.of(this@NewsFragment, Factory()).get(RemindViewModel::class.java)

     */


    // for different ViewModels:
    protected inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(aClass: Class<T>): T = f() as T
    }
    /*
        usage:
        mBinding.model = ViewModelProviders.of(this@NewsFragment, viewModelFactory { ReminderViewModel() }).get(RemindViewModel::class.java)
     */
}