package com.esys.mviinitialproject.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.esys.mviinitialproject.databinding.ItemMainBinding
import com.esys.mviinitialproject.ui.base.BaseRecyclerAdapter

class MainAdapter : BaseRecyclerAdapter<MainAdapter.ViewHolder, String>() {

    class ViewHolder(private val _itemBinding: ItemMainBinding) :
        RecyclerView.ViewHolder(_itemBinding.root) {
        val root = _itemBinding.root
        fun bind(item: String) {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
        holder.root.setOnClickListener {
            getEventListener().onItemClick(getItem(position))
        }
    }
}