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
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.paging3_mygradle.MyApplication
import com.bignerdranch.android.paging3_mygradle.data.repository.flow.TaskFlowRepositoryImpl
import com.bignerdranch.android.paging3_mygradle.data.repository.paging.TaskFlowPagingSource
import com.richarddewan.paging3_todo.databinding.FragmentFlowPagingSourceBinding
import com.bignerdranch.android.paging3_mygradle.ui.adapter.TaskPagingDataAdapter
import com.bignerdranch.android.paging3_mygradle.ui.flow.viewmodel.FlowViewMode
import com.bignerdranch.android.paging3_mygradle.utils.ViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import com.richarddewan.paging3_todo.R
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

        //Load State Listener
        pagingDataAdapter.addLoadStateListener { loadState ->

            //show Progress bar when the load state is loading
        binding.pbFlowPagineSource.isVisible =
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

    private fun showErrorSnackBar(msg: String){
        Snackbar.make(requireView(), msg, Snackbar.LENGTH_INDEFINITE).apply {
            setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.purple_200))
            setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_700))
            setActionTextColor(ContextCompat.getColor(requireContext(), R.color.purple_500))
            setAction(R.string.close) {
                dismiss()
            }
            anchorView = binding.pbFlowPagineSource
        }.show()
    }
}
