package com.bignerdranch.android.paging3_mygradle.ui.rx

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bignerdranch.android.paging3_mygradle.databinding.FragmentRxPagingSourceBinding


/*
created by Richard Dewan 11/04/2021
*/

class RxPagingSourceFragment: Fragment() {
    private lateinit var binding: FragmentRxPagingSourceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRxPagingSourceBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}