package com.bignerdranch.android.paging3_mygradle.data.repository.flow


import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bignerdranch.android.paging3_mygradle.data.remote.model.TaskEntity
import com.bignerdranch.android.paging3_mygradle.data.repository.paging.TaskFlowPagingSource
import kotlinx.coroutines.flow.Flow

class TaskFlowRepositoryImpl( //video 11
    private val pagingSource: TaskFlowPagingSource
) : TaskFlowRepository {

    override fun getTaskListPaging(): Flow<PagingData<TaskEntity.Task>> {
        return Pager(
            defaultPagingConfig(),
            pagingSourceFactory = { pagingSource }
        ).flow
    }

    private fun defaultPagingConfig() : PagingConfig{
        return PagingConfig(
            pageSize = 10,
            prefetchDistance = 10,
            enablePlaceholders = false,
            initialLoadSize = 30,
            maxSize = 50
        )
    }

}