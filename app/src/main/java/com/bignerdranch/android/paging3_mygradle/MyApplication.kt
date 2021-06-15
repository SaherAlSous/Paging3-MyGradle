package com.bignerdranch.android.paging3_mygradle

import android.app.Application
import com.bignerdranch.android.paging3_mygradle.data.remote.Network
import com.bignerdranch.android.paging3_mygradle.data.remote.NetworkService

class MyApplication : Application(){  //video 15

    lateinit var networkService: NetworkService
    private val baseUrl : String ="https://freeapi.rdewan.dev"
    override fun onCreate() {
        super.onCreate()

        networkService = Network.create(baseUrl, cacheDir,1024 * 1024 * 10)
    }
}