package com.bignerdranch.android.paging3_mygradle.ui.flow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bignerdranch.android.paging3_mygradle.data.local.entity.TaskEntity
import com.bignerdranch.android.paging3_mygradle.data.repository.flow.TaskFlowRemoteMediatorRepositoryImpl
import kotlinx.coroutines.flow.Flow

class FlowRemoteMediatorViewModel(
    private val repository: TaskFlowRemoteMediatorRepositoryImpl
) : ViewModel(){

    @ExperimentalPagingApi
    fun getTaskList(): Flow<PagingData<TaskEntity>>{
        return repository.getTaskList()
            .cachedIn(viewModelScope)
    }
}