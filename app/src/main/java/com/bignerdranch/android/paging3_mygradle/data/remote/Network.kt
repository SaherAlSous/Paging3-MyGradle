package com.bignerdranch.android.paging3_mygradle.data.remote

import androidx.viewbinding.BuildConfig
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object Network { //video 8

    private const val NETWORK_CALL_TIME_OUT = 60L
    private lateinit var okHttpClient: OkHttpClient

    fun create(baseURL: String, cacheDir: File, cacheSize: Long) : NetworkService {
        okHttpClient = OkHttpClient.Builder()
            .cache(Cache(cacheDir, cacheSize))
            .readTimeout(NETWORK_CALL_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(NETWORK_CALL_TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG){
                    HttpLoggingInterceptor.Level.BODY
                } else{
                    HttpLoggingInterceptor.Level.NONE
                }
            })
            .build()
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(NetworkService::class.java)
    }
}