package com.example.sampleproject.ui.details

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sampleproject.R
import com.example.sampleproject.databinding.FragmentDetailsBinding
import com.example.sampleproject.ui.BaseFragment
import com.example.sampleproject.util.launchAndRepeatWithViewLifecycle

class DetailsFragment : BaseFragment<DetailsViewModel, FragmentDetailsBinding>(R.layout.fragment_details){
    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.apply {
            setArguments(args.id)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeActions()
    }

    private fun observeActions() {
        launchAndRepeatWithViewLifecycle {
            viewModel.actions.collect { action ->
                when (action) {
                    is DetailsViewModel.Action.NavigateBack -> {
                        findNavController().navigateUp()
                    }
                }
            }
        }
    }
}