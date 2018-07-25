package com.example.sergeykuchin.adorablecameraapp.other.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.lang.ref.WeakReference


class UtilsImpl(val context: Context): Utils {

    override fun decodeSampledBitmapFromResource(bitmap: Bitmap, reqWidthRes: Int, reqHeightRes: Int): Bitmap? {

        //Better to use picasso here but it will be very simple

        val bos = WeakReference(ByteArrayOutputStream())
        val bis: BufferedInputStream?

        if (bos.get() == null) return null

        bitmap.compress(CompressFormat.JPEG, 0, bos.get())
        val bitmapdata = bos.get()?.toByteArray()
        bis = BufferedInputStream(ByteArrayInputStream(bitmapdata))
        bis.mark(bis.available())

        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()

        // Calculate inSampleSize
        options.outHeight = bitmap.height
        options.outWidth = bitmap.width
        options.inSampleSize = calculateInSampleSize(options, context.resources.getDimensionPixelSize(reqWidthRes), context.resources.getDimensionPixelSize(reqHeightRes))

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        bis.reset()
        val testBitmap2 = BitmapFactory.decodeStream(bis, null, options)
        return testBitmap2
    }

    private fun calculateInSampleSize(
            options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {

        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }
}