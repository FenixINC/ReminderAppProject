package com.example.taras.reminerapp.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import com.example.taras.reminerapp.App
import com.example.taras.reminerapp.db.dao.RemindDao
import com.example.taras.reminerapp.db.model.Remind

/**
 * Created by Taras Koloshmatin on 24.07.2018
 */

@Database(entities = [
    (Remind::class)
//    (News::class),
//    (Event::class),
//    (Video::class)
], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun remindDao(): RemindDao


    companion object INSTANCE {
        private var sInstance: AppDatabase? = null

        @Synchronized
        private fun newInstance(): AppDatabase {
            if (sInstance == null) {
                sInstance = Room.databaseBuilder(App.getInstance(), AppDatabase::class.java, "Reminder-Database")
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return sInstance!!
        }

        @JvmStatic
        fun getInstance(): AppDatabase {
            if (sInstance == null) {
                newInstance()
            }
            return sInstance!!
        }
    }
}