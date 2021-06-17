package com.bignerdranch.android.paging3_mygradle.data.local.db.flowdao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bignerdranch.android.paging3_mygradle.data.local.entity.TaskKeyEntity

@Dao
interface TaskKeyFlowDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(keys: List<TaskKeyEntity>)

    @Query("SELECT * FROM task_keys WHERE taskId =:taskId")
    suspend fun getTaskKeyByTaskId(taskId: Int): TaskKeyEntity

    @Query("DELETE FROM task_keys")
    suspend fun clearKeys()
}