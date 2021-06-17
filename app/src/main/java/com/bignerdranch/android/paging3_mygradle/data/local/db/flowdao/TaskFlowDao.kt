package com.bignerdranch.android.paging3_mygradle.data.local.db.flowdao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bignerdranch.android.paging3_mygradle.data.local.entity.TaskEntity


@Dao
interface TaskFlowDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(tasks : List<TaskEntity>)

    @Query("SELECT * FROM tasks ORDER BY id ASC")
    fun getTasks(): PagingSource<Int, TaskEntity>

    @Query("DELETE FROM tasks")
    suspend fun clearTasks()


}