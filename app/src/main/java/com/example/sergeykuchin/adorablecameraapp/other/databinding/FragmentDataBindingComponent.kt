package com.example.sergeykuchin.adorablecameraapp.other.databinding

import android.databinding.DataBindingComponent
import android.support.v4.app.Fragment

/**
 * A Data Binding Component implementation for fragments.
 */
class FragmentDataBindingComponent(fragment: Fragment) : DataBindingComponent {

    private var adapter: FragmentBindingAdapters? = null

    init {
        adapter = FragmentBindingAdapters(fragment)
    }

    fun getFragmentBindingAdapters(): FragmentBindingAdapters? = adapter
}