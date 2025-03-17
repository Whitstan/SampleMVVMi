package com.example.sampleproject.util

import android.app.Application
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import com.example.sampleproject.R
import com.example.sampleproject.data.local.Todo
import com.example.sampleproject.ui.list.ListItemAdapter
import org.robolectric.RuntimeEnvironment
import java.util.WeakHashMap
import kotlin.test.assertEquals

fun <T : Application> application(): T = ApplicationProvider.getApplicationContext() as T

fun getString(@StringRes resId: Int) = RuntimeEnvironment.application.getString(resId)

inline fun <reified T : Fragment> withFragment(args: Bundle? = null, noinline action: (T) -> Unit) =
    FragmentScenario.launchInContainer(T::class.java, args, R.style.Theme_Sampleproject, null)
        .onFragment(action).moveToState(Lifecycle.State.DESTROYED)

private val MEASURED_RECYCLERVIEWS = WeakHashMap<RecyclerView, Boolean>()
fun RecyclerView.measureAndLayoutIfNeeded() {
    if (MEASURED_RECYCLERVIEWS[this] != true) {
        // workaround robolectric recyclerView issue
        measure(0, 0)
        layout(0, 0, 100, 2000)
        MEASURED_RECYCLERVIEWS[this] = true
    }
}

fun RecyclerView.getCell(
    index: Int
): RecyclerView.ViewHolder {
    measureAndLayoutIfNeeded()
    return findViewHolderForAdapterPosition(index)!!
}

fun RecyclerView.assertContains(
    expected: List<Todo>,
    verifyItem: (Todo, ListItemAdapter.ItemViewHolder) -> Unit
) {
    assertEquals(expected.size, adapter!!.itemCount)
    for ((pos, viewModel) in expected.withIndex()) {
        val viewHolder = getCell(pos) as ListItemAdapter.ItemViewHolder
        try {
            verifyItem(viewModel, viewHolder)
        } catch (e: Throwable) {
            println("verifyItem() failed at index: $pos")
            throw e
        }
    }
}