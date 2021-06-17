package com.bignerdranch.android.paging3_mygradle.ui.flow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.paging3_mygradle.MyApplication
import com.bignerdranch.android.paging3_mygradle.R
import com.bignerdranch.android.paging3_mygradle.data.repository.flow.TaskFlowRemoteMediatorRepositoryImpl
import com.bignerdranch.android.paging3_mygradle.data.repository.paging.TaskFlowRemoteMediator
import com.bignerdranch.android.paging3_mygradle.databinding.FragmentFlowRemoteMediatorBinding
import com.bignerdranch.android.paging3_mygradle.ui.adapter.TaskRemoteMediatorDataAdapter
import com.bignerdranch.android.paging3_mygradle.ui.flow.viewmodel.FlowRemoteMediatorViewModel
import com.bignerdranch.android.paging3_mygradle.utils.ViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


/*
created by Richard Dewan 11/04/2021
*/

class FlowRemoteMediatorFragment: Fragment() {
    private lateinit var binding: FragmentFlowRemoteMediatorBinding
    private lateinit var viewModel: FlowRemoteMediatorViewModel //video 36
    private lateinit var repository: TaskFlowRemoteMediatorRepositoryImpl
    @ExperimentalPagingApi
    private lateinit var remoteMediator: TaskFlowRemoteMediator
    private lateinit var remoteMediatorAdapter : TaskRemoteMediatorDataAdapter
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(requireContext())
    }

    @ExperimentalPagingApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFlowRemoteMediatorBinding.inflate(layoutInflater)

        //network service
        val networkService = (requireActivity().application as MyApplication).networkService
        val databaseService = (requireActivity().application as MyApplication).databaseService

        //remote mediator
        remoteMediator = TaskFlowRemoteMediator(networkService, databaseService)


        //repository
        repository = TaskFlowRemoteMediatorRepositoryImpl(databaseService,remoteMediator)

        //viewModel
        viewModel = ViewModelProvider(this, ViewModelProviderFactory(FlowRemoteMediatorViewModel::class)
        {
            FlowRemoteMediatorViewModel(repository)
        }).get(FlowRemoteMediatorViewModel::class.java)

        return binding.root
    }

    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setting up adapter
        remoteMediatorAdapter = TaskRemoteMediatorDataAdapter()


        //setting recycler view
        binding.rvFlowRemoteMediator.apply {
            adapter = remoteMediatorAdapter
            layoutManager = linearLayoutManager
        }

        //Load State Listener
        remoteMediatorAdapter.addLoadStateListener { loadState ->

            //show Progress bar when the load state is loading
            binding.pbFlowRemoteMediator.isVisible =
                loadState.source.refresh is LoadState.Loading

            //LoadState for the error and show the message on the UI
            val errorState = loadState.source.refresh as? LoadState.Error //PagingSource loadState
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.source.append as? LoadState.Error
                ?: loadState.refresh as? LoadState.Error //Remote Mediator LoadState
                ?: loadState.append as? LoadState.Error
                ?:loadState.prepend as? LoadState.Error

            errorState?.let {
                showErrorSnackBar(it.error.message.toString())
            }
        }

        //observer the live data
        observers()
    }


    @ExperimentalPagingApi
    private fun observers(){
        lifecycleScope.launch {
            viewModel.getTaskList()
                .collectLatest {
                    remoteMediatorAdapter.submitData(lifecycle,it)
                }
        }
    }

    private fun showErrorSnackBar(msg: String){
        Snackbar.make(requireView(), msg, Snackbar.LENGTH_INDEFINITE).apply {
            setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.purple_200))
            setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_700))
            setActionTextColor(ContextCompat.getColor(requireContext(), R.color.purple_500))
            setAction(R.string.close) {
                dismiss()
            }
            anchorView = binding.pbFlowRemoteMediator
        }.show()
    }
}