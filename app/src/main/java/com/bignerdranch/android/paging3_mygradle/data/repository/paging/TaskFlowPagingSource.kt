package com.bignerdranch.android.paging3_mygradle.data.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bignerdranch.android.paging3_mygradle.data.remote.NetworkService
import com.bignerdranch.android.paging3_mygradle.data.remote.model.TaskEntity
import com.bignerdranch.android.paging3_mygradle.data.remote.model.TaskResponseMapper
import com.bignerdranch.android.paging3_mygradle.data.remote.response.TaskResponse
import java.lang.Exception

class TaskFlowPagingSource( //video 10
    private val networkService: NetworkService
    ) : PagingSource<Int, TaskEntity.Task>(), TaskResponseMapper<TaskResponse, TaskEntity> {
    override fun getRefreshKey(state: PagingState<Int, TaskEntity.Task>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TaskEntity.Task> {
        val currentPage = params.key ?: 1
        return try {

            networkService.getTaskListFlow(currentPage)
                .run {
                val data = mapFromResponse(this)
                    return LoadResult.Page(
                        data = data.tasks,
                        prevKey = if (currentPage == 1) null else currentPage -1,
                        nextKey = if (data.endOfPage) null else currentPage +1
                    )
            }

        }catch (e: Exception){
            LoadResult.Error(e)
        }
    }

    override fun mapFromResponse(response: TaskResponse): TaskEntity {
        return with(response){
            TaskEntity(
                totalPage = lastPage,
                currentPage = currentPage,
                tasks = task.map {
                    TaskEntity.Task(
                        id = it.id,
                        userId = it.userId,
                        title = it.title,
                        body = it.body,
                        note = it.note,
                        status = it.status,
                        createdAt = it.createdAt,
                        updatedAt = it.updatedAt
                    )
                }
            )
        }
    }
}