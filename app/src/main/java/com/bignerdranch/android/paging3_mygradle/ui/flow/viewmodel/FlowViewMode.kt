package com.bignerdranch.android.paging3_mygradle.ui.flow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bignerdranch.android.paging3_mygradle.data.remote.model.TaskEntity
import com.bignerdranch.android.paging3_mygradle.data.repository.flow.TaskFlowRepositoryImpl
import kotlinx.coroutines.flow.Flow

class FlowViewMode( //video 12
    private val repositoryImpl: TaskFlowRepositoryImpl
) : ViewModel() {

    fun getTaskListPaging() : Flow<PagingData<TaskEntity.Task>> =
        repositoryImpl.getTaskListPaging()
            .cachedIn(viewModelScope)

}