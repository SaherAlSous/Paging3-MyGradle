package com.bignerdranch.android.paging3_mygradle.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.paging3_mygradle.R
import com.bignerdranch.android.paging3_mygradle.ui.adapter.viewholder.SeparatorItemViewHolder
import com.bignerdranch.android.paging3_mygradle.ui.adapter.viewholder.TaskItemViewHolder
import com.bignerdranch.android.paging3_mygradle.ui.model.UiModel
import java.lang.UnsupportedOperationException

class TaskUiModelAdapter : PagingDataAdapter<UiModel, RecyclerView.ViewHolder>(
    diffCallback = diffUtil
) {

   companion object{
       val diffUtil = object : DiffUtil.ItemCallback<UiModel>(){
           override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
               return (oldItem is UiModel.TaskItem && newItem is UiModel.TaskItem) &&
                       oldItem.task.id == newItem.task.id ||
                       (oldItem is UiModel.SeparatorItem && newItem is UiModel.SeparatorItem) &&
                       oldItem.status ==  newItem.status
           }

           override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
               return oldItem == newItem
           }

       }
   }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            when(it){
                is UiModel.TaskItem -> (holder as TaskItemViewHolder).onBind(it.task)
                is UiModel.SeparatorItem -> (holder as SeparatorItemViewHolder).onBind(it.status)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.task_list_view){
            TaskItemViewHolder.create(parent)
        } else{
            SeparatorItemViewHolder.create(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is UiModel.TaskItem -> R.layout.task_list_view
            is UiModel.SeparatorItem -> R.layout.separator_view_item
            else -> throw UnsupportedOperationException("Unknown view type")
        }
    }
}