package com.bignerdranch.android.paging3_mygradle.data.remote.model

interface TaskResponseMapper<Response, Model> { //video 9

    abstract fun mapFromResponse(response: Response) : Model
}