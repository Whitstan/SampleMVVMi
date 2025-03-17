package com.example.sampleproject.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.sampleproject.BR
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment<ViewModel : Any, Binding : ViewDataBinding>(private val contentViewId: Int) : DaggerFragment(){

    @Inject
    lateinit var viewModel: ViewModel

    private var _binding: Binding? = null
    val binding: Binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            contentViewId,
            null,
            false
        )

        binding.run {
            lifecycleOwner = viewLifecycleOwner
            setVariable(BR.viewModel, viewModel)
            executePendingBindings()
        }

        return binding.root
    }

    override fun onDestroyView() {
        _binding?.unbind()
        _binding = null

        super.onDestroyView()
    }
}