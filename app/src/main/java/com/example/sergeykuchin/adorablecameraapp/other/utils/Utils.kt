package com.example.sergeykuchin.adorablecameraapp.other.utils

import android.graphics.Bitmap
import java.io.File

interface Utils {

    fun decodeSampledBitmapFromResource(bitmap: Bitmap, reqWidthRes: Int, reqHeightRes: Int): Bitmap?

    fun createPictureWithUniqueName(): File?
}