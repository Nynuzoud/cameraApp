package com.example.sergeykuchin.adorablecameraapp.view.imagesetup

import com.example.sergeykuchin.adorablecameraapp.view.imagesetup.adapter.ImageFilter

interface ImageSetupPresenter {


    fun setImageToImageView()

    fun onFilterClick(imageFilter: ImageFilter)
}