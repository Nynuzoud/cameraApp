package com.example.sergeykuchin.adorablecameraapp.view.imagesetup.adapter

import android.support.v7.widget.RecyclerView
import com.example.sergeykuchin.adorablecameraapp.databinding.ItemFilterBinding

class FilterViewHolder(val binding: ItemFilterBinding,
                       val listener: FilterAdapterListener?) : RecyclerView.ViewHolder(binding.root) {

    fun bind(imageFilter: ImageFilter?) {
        if (imageFilter == null) return

        binding.filterImage.setImageBitmap(imageFilter.bitmap)
        binding.filterName.setText(imageFilter.nameRes)

        binding.root.setOnClickListener { listener?.onClick(imageFilter) }
    }
}