package com.example.taras.reminerapp.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.taras.reminerapp.db.AppDatabase
import com.example.taras.reminerapp.db.Constants
import com.example.taras.reminerapp.db.model.Remind

/**
 * Created by Taras Koloshmatin on 31.07.2018
 */
class RemindViewModel : ViewModel() {

    fun getRemindList(): LiveData<List<Remind>> {
        return AppDatabase.getInstance().remindDao().getList()
    }

    fun getUserRemindList(): LiveData<List<Remind>> {
        return AppDatabase.getInstance().remindDao().getListByType(Constants.TYPE_USER_REMIND)
    }

    fun getNewsList(): LiveData<List<Remind>> {
        return AppDatabase.getInstance().remindDao().getListByType(Constants.TYPE_NEWS)
    }

    fun getEventList(): LiveData<List<Remind>> {
        return AppDatabase.getInstance().remindDao().getListByType(Constants.TYPE_EVENT)
    }

    fun getVideoList(): LiveData<List<Remind>> {
        return AppDatabase.getInstance().remindDao().getListByType(Constants.TYPE_VIDEO)
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