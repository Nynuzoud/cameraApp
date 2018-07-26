package com.example.sergeykuchin.adorablecameraapp.view.imagesetup

import android.graphics.Bitmap
import android.net.Uri

interface ImageSetupFragmentView {

    fun addCachedBitmapUri(id: Int, fileUri: Uri?)

    fun setMainImageBitmap(bitmap: Bitmap?)

    fun generateFilters()
}