package com.bignerdranch.android.paging3_mygradle.data.repository.flow

import androidx.paging.PagingData
import com.bignerdranch.android.paging3_mygradle.data.remote.model.TaskPaging
import kotlinx.coroutines.flow.Flow

interface TaskFlowRepository { //video 11

    fun getTaskListPaging(): Flow<PagingData<TaskPaging.Task>>
}