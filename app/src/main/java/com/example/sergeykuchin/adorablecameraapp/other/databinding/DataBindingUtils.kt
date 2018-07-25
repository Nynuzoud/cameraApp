package com.example.sergeykuchin.adorablecameraapp.other.databinding

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView

@BindingAdapter("app:configuration")
fun configureRecyclerView(recyclerView: RecyclerView, configuration: RecyclerConfiguration?) {
    recyclerView.layoutManager = configuration?.layoutManager
    recyclerView.itemAnimator = configuration?.itemAnimator
    recyclerView.adapter = configuration?.adapter
}