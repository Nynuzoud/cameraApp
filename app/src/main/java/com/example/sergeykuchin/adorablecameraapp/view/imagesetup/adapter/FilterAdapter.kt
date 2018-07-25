package com.example.sergeykuchin.adorablecameraapp.view.imagesetup.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.sergeykuchin.adorablecameraapp.databinding.ItemFilterBinding

class FilterAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var _data: List<ImageFilter>? = ArrayList()
    var data: List<ImageFilter>?
        get() = _data
        set(value) {
            _data = value
            notifyDataSetChanged()
        }

    private var _listener: FilterAdapterListener? = null
    var listener: FilterAdapterListener?
        get() = _listener
        set(value) {
            _listener = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding = ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilterViewHolder(binding, _listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        (holder as FilterViewHolder).bind(_data?.get(position))
    }

    override fun getItemCount(): Int = _data?.size ?: 0

    override fun getItemId(position: Int): Long = _data?.get(position)?.id?.toLong() ?: 0L
}