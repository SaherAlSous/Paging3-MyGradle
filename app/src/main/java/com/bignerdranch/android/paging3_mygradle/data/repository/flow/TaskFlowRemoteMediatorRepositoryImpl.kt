package com.bignerdranch.android.paging3_mygradle.data.repository.flow

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bignerdranch.android.paging3_mygradle.data.local.db.DataBaseService
import com.bignerdranch.android.paging3_mygradle.data.local.entity.TaskEntity
import com.bignerdranch.android.paging3_mygradle.data.repository.paging.TaskFlowRemoteMediator
import kotlinx.coroutines.flow.Flow

class TaskFlowRemoteMediatorRepositoryImpl @ExperimentalPagingApi constructor(
    private val dataBaseService: DataBaseService,
    private val taskFlowRemoteMediator: TaskFlowRemoteMediator
) : TaskFlowRemoteMediatorRepository {
    @ExperimentalPagingApi
    override fun getTaskList(): Flow<PagingData<TaskEntity>> {
        return Pager(
            config = defaultPagingConfig(),
            remoteMediator = taskFlowRemoteMediator,
            pagingSourceFactory = {dataBaseService.taskFlowDao().getTasks()},
        ).flow
    }
    private fun defaultPagingConfig() : PagingConfig {
        return PagingConfig(
            pageSize = 10,
            prefetchDistance = 20,
            enablePlaceholders = false,
            initialLoadSize = 30,
            maxSize = 50
        )
    }
}