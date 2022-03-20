package com.chocomiruku.homework7

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chocomiruku.homework7.databinding.ListItemBinding

class ModelAdapter :
    ListAdapter<Model, ModelAdapter.ViewHolder>(FactDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fact = getItem(position)
        holder.bind(fact)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: Model) {
            binding.idText.text = binding.idText.text.toString().plus(" " + model.id)
            binding.userIdText.text = binding.userIdText.text.toString().plus(" " + model.userId)
            binding.titleText.text = model.title
            binding.bodyText.text = model.body
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ListItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    class FactDiffCallback :
        DiffUtil.ItemCallback<Model>() {

        override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
            return oldItem == newItem
        }
    }
}