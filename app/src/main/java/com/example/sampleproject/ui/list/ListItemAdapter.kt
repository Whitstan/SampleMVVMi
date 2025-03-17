package com.example.sampleproject.ui.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.example.sampleproject.R
import com.example.sampleproject.data.local.Todo
import com.example.sampleproject.databinding.ListItemBinding

class ListItemAdapter(
    private val onItemClick: (Todo) -> Unit
) : ListAdapter<Todo, ListItemAdapter.ItemViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: Todo) {
            binding.textId.text = String.format(binding.root.context.getString(R.string.text_list_item_id), todo.id)
            binding.textTitle.text = String.format(binding.root.context.getString(R.string.text_list_item_title), todo.title)
            binding.card.setCardBackgroundColor(
                ContextCompat.getColor(binding.root.context,
                    if (todo.isCompleted) R.color.completedBackground
                    else R.color.notCompletedBackground
                )
            )
            binding.root.setOnClickListener { onItemClick(todo) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Todo, newItem: Todo) = oldItem == newItem
    }
}