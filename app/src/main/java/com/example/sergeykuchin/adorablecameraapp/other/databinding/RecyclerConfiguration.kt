package com.example.sergeykuchin.adorablecameraapp.other.databinding

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.support.v7.widget.RecyclerView
import com.example.sergeykuchin.adorablecameraapp.BR

class RecyclerConfiguration : BaseObservable() {

    private var _layoutManager: RecyclerView.LayoutManager? = null
    var layoutManager: RecyclerView.LayoutManager?
        @Bindable
        get() = _layoutManager
        set(value) {
            _layoutManager = value
            notifyPropertyChanged(BR.layoutManager)
        }

    private var _itemAnimator: RecyclerView.ItemAnimator? = null
    var itemAnimator: RecyclerView.ItemAnimator?
        @Bindable
        get() = _itemAnimator
        set(value) {
            _itemAnimator = value
            notifyPropertyChanged(BR.layoutManager)
        }

    private var _adapter: RecyclerView.Adapter<*>? = null
    var adapter: RecyclerView.Adapter<*>?
        @Bindable
        get() = _adapter
        set(value) {
            _adapter = value
            notifyPropertyChanged(BR.layoutManager)
        }
}