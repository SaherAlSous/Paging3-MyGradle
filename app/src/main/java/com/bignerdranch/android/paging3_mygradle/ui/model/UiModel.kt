package com.bignerdranch.android.paging3_mygradle.ui.model

import com.bignerdranch.android.paging3_mygradle.data.local.entity.TaskEntity

sealed class UiModel { //video 53

    data class TaskItem(
        val task: TaskEntity
    ) : UiModel()

    data class SeparatorItem(
        val status: String
    ) : UiModel()


}