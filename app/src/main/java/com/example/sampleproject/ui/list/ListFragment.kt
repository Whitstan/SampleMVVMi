package com.example.sampleproject.ui.list

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.sampleproject.R
import com.example.sampleproject.databinding.FragmentListBinding
import com.example.sampleproject.ui.BaseFragment
import com.example.sampleproject.util.launchAndRepeatWithViewLifecycle
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

const val NAV_ARG_ID = "id"

class ListFragment : BaseFragment<ListViewModel, FragmentListBinding>(R.layout.fragment_list) {

    private lateinit var adapter: ListItemAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.recyclerView

        adapter = ListItemAdapter (
            onItemClick = { item ->
                findNavController().navigate(
                    R.id.action_listFragment_to_detailsFragment,
                    bundleOf(NAV_ARG_ID to item.id)
                )
            }
        )

        recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.todos.collect {
                adapter.submitList(it)
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchData()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect {
                binding.swipeRefreshLayout.isRefreshing = it
            }
        }

        observeActions()
    }

    private fun observeActions() {
        launchAndRepeatWithViewLifecycle {
            viewModel.actions.collect { action ->
                when (action) {
                    is ListViewModel.Action.ShowSnackBar -> {
                        Snackbar.make(requireView(), action.message, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}