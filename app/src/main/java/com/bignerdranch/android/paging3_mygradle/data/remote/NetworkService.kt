package com.bignerdranch.android.paging3_mygradle.data.remote

import com.bignerdranch.android.paging3_mygradle.data.remote.Endpoint
import com.bignerdranch.android.paging3_mygradle.data.remote.response.TaskResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NetworkService { // video 7 - udemy - paging3

    @Headers(Endpoint.HEADER_ACCEPT)
    @GET(Endpoint.GET_ALL_TASK)
    suspend fun getTaskListFlow(
        @Query("page") pageNumber: Int
    ): TaskResponse

    @Headers(Endpoint.HEADER_ACCEPT)
    @GET(Endpoint.GET_ALL_TASK)
    fun getTaskListRx(@Query("page") pageNumber: Int): Single<TaskResponse>

}