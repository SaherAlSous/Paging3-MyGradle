package com.bignerdranch.android.paging3_mygradle.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass

class ViewModelProviderFactory<T: ViewModel>( //video 13
    private val kClass: KClass<T>,
    private val creator: () -> T ) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(kClass.java)){
            return creator() as T
        }
        throw IllegalArgumentException("Unknown Viewmodel Class")
    }
}