package com.example.sampleproject.ui.common

import android.view.View
import androidx.databinding.BindingAdapter

object Bindings {
    @JvmStatic
    @BindingAdapter("backgroundColor")
    fun setBackgroundResource(view: View, resource: Int) {
        view.setBackgroundColor(resource)
    }
}