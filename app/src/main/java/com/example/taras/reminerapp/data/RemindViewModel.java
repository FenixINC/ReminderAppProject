package com.example.taras.reminerapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.taras.reminerapp.App;
import com.example.taras.reminerapp.db.AppDatabase;
import com.example.taras.reminerapp.db.Constants;
import com.example.taras.reminerapp.db.model.Remind;

import java.util.List;

/**
 * Created by Taras Koloshmatin on 30.07.2018
 */
public class RemindViewModel extends ViewModel {

    private final LiveData<List<Remind>> mRemindList;
    private final LiveData<List<Remind>> mUserRemindList;
    private final LiveData<List<Remind>> mNewsList;
    private final LiveData<List<Remind>> mEventList;
    private final LiveData<List<Remind>> mVideoList;

    public RemindViewModel() {
        mRemindList = AppDatabase.getInstance().remindDao().getList();
        mUserRemindList = AppDatabase.getInstance().remindDao().getListByType(Constants.TYPE_USER_REMIND);
        mNewsList = AppDatabase.getInstance().remindDao().getListByType(Constants.TYPE_NEWS);
        mEventList = AppDatabase.getInstance().remindDao().getListByType(Constants.TYPE_EVENT);
        mVideoList = AppDatabase.getInstance().remindDao().getListByType(Constants.TYPE_VIDEO);
    }


    public LiveData<List<Remind>> getRemindList() {
        return mRemindList;
    }

    public LiveData<List<Remind>> getUserRemindList() {
        return mUserRemindList;
    }

    public LiveData<List<Remind>> getNewsList() {
        return mNewsList;
    }

    public LiveData<List<Remind>> getEventList() {
        return mEventList;
    }

    public LiveData<List<Remind>> getVideoList() {
        return mVideoList;
    }

//    public void deleteItem(Remind remind) {
//        new deleteAsyncTask().execute(remind);
//    }
//
//    private static class deleteAsyncTask extends AsyncTask<Remind, Void, Void> {
//        @Override
//        protected Void doInBackground(final Remind... params) {
//            AppDatabase.getInstance().remindDao().delete(params[0]);
//            return null;
//        }
//    }

    public static class RemindViewModelFactory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final App application;

        public RemindViewModelFactory(@NonNull App application) {
            this.application = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new RemindViewModel();
        }
    }
}