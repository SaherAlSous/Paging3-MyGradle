package com.bignerdranch.android.paging3_mygradle.ui.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class TaskLoadStateAdapter(
    private val retry:() -> Unit
) : LoadStateAdapter<LoadStateViewHolder>() {
    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.onBind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder.create(parent, retry)
    }
}