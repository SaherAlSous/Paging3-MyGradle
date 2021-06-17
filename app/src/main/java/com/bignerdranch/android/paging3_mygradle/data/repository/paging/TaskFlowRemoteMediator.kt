package com.bignerdranch.android.paging3_mygradle.data.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.bignerdranch.android.paging3_mygradle.data.local.db.DataBaseService
import com.bignerdranch.android.paging3_mygradle.data.local.entity.EntityMapper
import com.bignerdranch.android.paging3_mygradle.data.local.entity.TaskKeyEntity
import com.bignerdranch.android.paging3_mygradle.data.remote.NetworkService
import com.bignerdranch.android.paging3_mygradle.data.local.entity.TaskEntity
import com.bignerdranch.android.paging3_mygradle.data.remote.model.TaskPaging
import com.bignerdranch.android.paging3_mygradle.data.remote.model.TaskResponseMapper
import com.bignerdranch.android.paging3_mygradle.data.remote.response.TaskResponse
import java.io.InvalidObjectException
import java.lang.Exception

@ExperimentalPagingApi
class TaskFlowRemoteMediator(
    private val networkService: NetworkService,
    private val dataBaseService: DataBaseService
) : RemoteMediator<Int, TaskEntity>(),
TaskResponseMapper<TaskResponse, TaskPaging>,
EntityMapper<TaskPaging.Task, TaskEntity>{

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, TaskEntity>
    ): MediatorResult {

        val page = when(loadType){
            LoadType.REFRESH -> {
                val keys = getKeyForTheClosestToCurrentItemPosition(state)
                keys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val keys = getKeyForTheFirstItem(state)
                    ?: return MediatorResult.Error(InvalidObjectException("First item is empty."))

                val previousKey =keys.previousKey
                    ?: return MediatorResult.Success(endOfPaginationReached = true)

                previousKey

            }
            LoadType.APPEND -> {
                val keys = getKeyForTheLastItem(state)
                    ?: return MediatorResult.Error(InvalidObjectException("Last item is empty."))

                val nextKey = keys.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = true)

                nextKey
            }
        }

        try {
            //fetch the data from API
            val response = networkService.getTaskListFlow(pageNumber = page)
            val data = mapFromResponse(response)

            dataBaseService.withTransaction {
                if (loadType == LoadType.REFRESH){
                    dataBaseService.taskFlowDao().clearTasks()
                    dataBaseService.taskKeyFlowDao().clearKeys()
                }

                val previousKey = if (page == 1) null else page -1
                val nextKey = if (data.endOfPage) null else page +1
                val keys = data.tasks.map {
                    TaskKeyEntity(taskId = it.id.toLong(), previousKey, nextKey)
                }

                //insert to local db
                dataBaseService.taskKeyFlowDao().insertMany(keys)
                dataBaseService.taskFlowDao().insertMany(mapToEntity(data.tasks))
            }
            return MediatorResult.Success(endOfPaginationReached = data.endOfPage)
        }catch (e: Exception){
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getKeyForTheFirstItem(state: PagingState<Int, TaskEntity>):TaskKeyEntity? {
        return state.pages.firstOrNull(){
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { taskEntity ->
            dataBaseService.taskKeyFlowDao().getTaskKeyByTaskId(taskEntity.taskId.toInt())
        }
    }

    private suspend fun getKeyForTheLastItem(state: PagingState<Int, TaskEntity>):TaskKeyEntity? {
        return state.pages.lastOrNull(){
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { taskEntity ->
            dataBaseService.taskKeyFlowDao().getTaskKeyByTaskId(taskEntity.taskId.toInt())
        }
    }

    private suspend fun getKeyForTheClosestToCurrentItemPosition(state: PagingState<Int, TaskEntity>):TaskKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.taskId?.let { it ->
                dataBaseService.taskKeyFlowDao().getTaskKeyByTaskId(it.toInt())
            }
        }
    }

    override fun mapFromResponse(response: TaskResponse): TaskPaging {
        return with(response){
            TaskPaging(
                totalPage = lastPage,
                currentPage = currentPage,
                tasks = task.map {
                    TaskPaging.Task(
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
    override fun mapToEntity(model: List<TaskPaging.Task>): List<TaskEntity> {
        return model.map {
            TaskEntity(
                taskId = it.id.toLong(),
                userId = it.userId,
                title = it.title,
                body = it.body,
                note = it.note,
                status = it.status
            )
        }
    }
}