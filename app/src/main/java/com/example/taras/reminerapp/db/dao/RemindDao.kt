package com.example.taras.reminerapp.db.dao

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.example.taras.reminerapp.db.model.Remind

/**
 * Created by Taras Koloshmatin on 25.07.2018
 */
@Dao
interface RemindDao {

    @Insert(onConflict = REPLACE)
    fun insert(obj: Remind)

    @Insert(onConflict = REPLACE)
    fun insert(obj: List<Remind>)

    @Update
    fun update(obj: Remind)

    @Delete
    fun delete(obj: Remind)

    @Query("DELETE FROM tblRemind")
    fun delete()

    // Queries:
    @Query("SELECT * FROM tblRemind")
    fun getList(): List<Remind>

    @Query("SELECT * FROM tblRemind WHERE type_remind = :typeRemind")
    fun getListByType(typeRemind: String): List<Remind>
}