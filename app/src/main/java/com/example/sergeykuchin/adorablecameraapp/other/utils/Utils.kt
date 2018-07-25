package com.example.sergeykuchin.adorablecameraapp.other.utils

import android.graphics.Bitmap

interface Utils {

    fun decodeSampledBitmapFromResource(bitmap: Bitmap, reqWidthRes: Int, reqHeightRes: Int): Bitmap?
}