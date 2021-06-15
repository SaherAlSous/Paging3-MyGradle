package com.bignerdranch.android.paging3_mygradle.ui.flow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.paging3_mygradle.MyApplication
import com.bignerdranch.android.paging3_mygradle.data.repository.flow.TaskFlowRepositoryImpl
import com.bignerdranch.android.paging3_mygradle.data.repository.paging.TaskFlowPagingSource
import com.richarddewan.paging3_todo.databinding.FragmentFlowPagingSourceBinding
import com.bignerdranch.android.paging3_mygradle.ui.adapter.TaskPagingDataAdapter
import com.bignerdranch.android.paging3_mygradle.ui.flow.viewmodel.FlowViewMode
import com.bignerdranch.android.paging3_mygradle.utils.ViewModelProviderFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


/*
created by Richard Dewan 11/04/2021
*/

class FlowPagingSourceFragment: Fragment() { //video 14
    private lateinit var binding: FragmentFlowPagingSourceBinding
    private lateinit var viewModel : FlowViewMode //should be named FlowViewModel but i forgot the L in the file name.
    private lateinit var repository: TaskFlowRepositoryImpl
    private lateinit var pagingDataAdapter: TaskPagingDataAdapter
    private val LinearLayoutManager : LinearLayoutManager by lazy {
        LinearLayoutManager(requireContext())
    }

    /**
     * creating the repository in video 15
     */
    private lateinit var pagingSource: TaskFlowPagingSource


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFlowPagingSourceBinding.inflate(layoutInflater)

        //paging source
        pagingSource = TaskFlowPagingSource(
            (requireActivity().application as MyApplication)
                .networkService) //video 15

        //repository
        repository = TaskFlowRepositoryImpl(pagingSource)

        //ViewModel
        viewModel = ViewModelProvider(this, ViewModelProviderFactory(
            FlowViewMode::class
        ){
            FlowViewMode(repository)
        }).get(FlowViewMode::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Paging Data Adapter
        pagingDataAdapter = TaskPagingDataAdapter()

        //Recycler view
        binding.rvFlowPagingData.apply {
            layoutManager = LinearLayoutManager
            adapter = pagingDataAdapter
        }



        //observe live data from live model
        observers()
    }

    private fun observers(){
        lifecycleScope.launch {
        viewModel.getTaskListPaging()
            .collectLatest {
                pagingDataAdapter.submitData(lifecycle,it)
            }
        }
    }
}