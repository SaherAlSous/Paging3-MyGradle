package com.bignerdranch.android.paging3_mygradle.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.paging3_mygradle.R
import com.bignerdranch.android.paging3_mygradle.data.local.entity.TaskEntity
import com.bignerdranch.android.paging3_mygradle.databinding.TaskListViewBinding

class TaskItemViewHolder( //video 54
    private val binding : TaskListViewBinding
) : RecyclerView.ViewHolder(binding.root){

    fun onBind( data: TaskEntity){
        binding.lbTaskId.text = data.taskId.toString()
        binding.lbUserId.text = data.userId
        binding.lbTitle.text = data.title
        binding.lbBody.text = data.body
        binding.lbNote.text = data.note
        binding.lbStatus.text = data.status
    }

    companion object{
        fun create(parent: ViewGroup): TaskItemViewHolder{
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.task_list_view, parent,false)
            val binding = TaskListViewBinding.bind(view)

            return TaskItemViewHolder(binding)
        }
    }
}