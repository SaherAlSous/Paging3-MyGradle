package com.bignerdranch.android.paging3_mygradle.data.repository.flow

import androidx.paging.PagingData
import com.bignerdranch.android.paging3_mygradle.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow


interface TaskFlowRemoteMediatorRepository {

    fun getTaskList() : Flow<PagingData<TaskEntity>>
}