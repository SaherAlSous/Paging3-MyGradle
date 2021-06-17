package com.bignerdranch.android.paging3_mygradle.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bignerdranch.android.paging3_mygradle.data.local.db.flowdao.TaskFlowDao
import com.bignerdranch.android.paging3_mygradle.data.local.db.flowdao.TaskKeyFlowDao
import com.bignerdranch.android.paging3_mygradle.data.local.entity.TaskEntity
import com.bignerdranch.android.paging3_mygradle.data.local.entity.TaskKeyEntity
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(
    entities = [TaskEntity::class, TaskKeyEntity::class],
    version = 1,
    exportSchema = false)
abstract class DataBaseService: RoomDatabase() {

    abstract fun taskFlowDao(): TaskFlowDao
    abstract fun taskKeyFlowDao(): TaskKeyFlowDao

companion object{
    @Volatile
    private var INSTANCE: DataBaseService? = null

    @InternalCoroutinesApi
    fun getInstance(context: Context) : DataBaseService =
        INSTANCE?: synchronized(this){
            INSTANCE?: buildDatabase(context).also {
                INSTANCE = it
            }
        }

    private fun buildDatabase(context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            DataBaseService::class.java,
            "paging.db")
            .build()
    }
}