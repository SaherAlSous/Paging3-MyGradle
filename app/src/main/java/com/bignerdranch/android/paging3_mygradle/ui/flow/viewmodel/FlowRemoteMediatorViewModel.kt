package com.bignerdranch.android.paging3_mygradle.ui.flow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.bignerdranch.android.paging3_mygradle.data.local.entity.TaskEntity
import com.bignerdranch.android.paging3_mygradle.data.repository.flow.TaskFlowRemoteMediatorRepositoryImpl
import com.bignerdranch.android.paging3_mygradle.ui.model.UiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FlowRemoteMediatorViewModel(
    private val repository: TaskFlowRemoteMediatorRepositoryImpl
) : ViewModel(){

    @ExperimentalPagingApi
    fun getTaskList(): Flow<PagingData<TaskEntity>>{
        return repository.getTaskList()
            .cachedIn(viewModelScope)
    }

    @ExperimentalPagingApi
    fun getTaskListUiModel(): Flow<PagingData<UiModel>>{
        return repository.getTaskList()
            .map {
                it.map {
                    UiModel.TaskItem(it)
                }
            }
            .map {
                it.insertSeparators { before, after ->
                    if (after == null){
                        //we are at the end of the list
                        return@insertSeparators null
                    }

                    val alpha = after.task.status.trim().take(1)
                    if (before == null){
                        //we are the the beggining of the list
                        return@insertSeparators UiModel
                            .SeparatorItem("$alpha: ${after.task.status}")
                    }
                    if (after.task.status == "STARTED"
                        || after.task.status == "PENDING"
                        || after.task.status == "started"
                        || after.task.status == "COMPLETED"
                        || after.task.status == "bbb1"){
                        UiModel.SeparatorItem("$alpha: ${after.task.status}")
                    } else{
                        null
                    }
                }
            }
            .cachedIn(viewModelScope)
    }
}