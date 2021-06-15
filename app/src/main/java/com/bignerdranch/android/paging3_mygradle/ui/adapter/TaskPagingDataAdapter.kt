package com.bignerdranch.android.paging3_mygradle.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.paging3_mygradle.data.remote.model.TaskPaging
import com.richarddewan.paging3_todo.databinding.TaskListViewBinding

class TaskPagingDataAdapter() : PagingDataAdapter<TaskPaging.Task, TaskPagingDataAdapter.TaskViewHolder>(
    diffCallback = diffUtil
){ //video 16


    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        getItem(position)?.let {
            holder.onBind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskListViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TaskViewHolder(binding)
    }


    class TaskViewHolder(val binding: TaskListViewBinding) : RecyclerView.ViewHolder(binding.root){
        fun onBind(data: TaskPaging.Task){
            binding.lbTaskId.text = data.id
            binding.lbUserId.text = data.userId
            binding.lbTitle.text = data.title
            binding.lbBody.text = data.body
            binding.lbNote.text = data.note
            binding.lbNote.text = data.status
        }
    }


    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<TaskPaging.Task>(){
            override fun areItemsTheSame(
                oldItem: TaskPaging.Task,
                newItem: TaskPaging.Task
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: TaskPaging.Task,
                newItem: TaskPaging.Task
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}