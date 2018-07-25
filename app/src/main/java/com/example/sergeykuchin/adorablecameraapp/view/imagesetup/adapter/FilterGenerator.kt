package com.example.sergeykuchin.adorablecameraapp.view.imagesetup.adapter

import android.graphics.Bitmap
import android.graphics.Color
import com.example.sergeykuchin.adorablecameraapp.R

class FilterGenerator {

    //Actually better to use picasso-transformations here because it is more efficient.
    //But it is a test task, so I did this primitive filters

    fun generateFilters(bitmap: Bitmap?): List<ImageFilter>? {
        if (bitmap == null) return null

        val filters = ArrayList<ImageFilter>()

        filters.add(ImageFilter(
                id = FilterGeneratorTypes.IMAGE_FILTER_ID_NORMAL.id,
                nameRes = R.string.normal,
                bitmap = bitmap
        ))

        filters.add(ImageFilter(
                id = FilterGeneratorTypes.IMAGE_FILTER_ID_GREYSCALE.id,
                nameRes = R.string.greyscale,
                bitmap = applyGreyscaleEffect(bitmap)
        ))

        filters.add(ImageFilter(
                id = FilterGeneratorTypes.IMAGE_FILTER_ID_RED.id,
                nameRes = R.string.red,
                bitmap = applyColorFilterEffect(bitmap, 255.0, 0.0, 0.0)
        ))

        filters.add(ImageFilter(
                id = FilterGeneratorTypes.IMAGE_FILTER_ID_GREEN.id,
                nameRes = R.string.green,
                bitmap = applyColorFilterEffect(bitmap, 0.0, 255.0, 0.0)
        ))

        filters.add(ImageFilter(
                id = FilterGeneratorTypes.IMAGE_FILTER_ID_BLUE.id,
                nameRes = R.string.blue,
                bitmap = applyColorFilterEffect(bitmap, 0.0, 0.0, 255.0)
        ))


        return filters
    }

    fun applyGreyscaleEffect(src: Bitmap?): Bitmap? {
        if (src == null) return null
        // constant factors
        val GS_RED = 0.299
        val GS_GREEN = 0.587
        val GS_BLUE = 0.114

        // create output bitmap
        val bmOut = Bitmap.createBitmap(src.width, src.height, src.config)
        // pixel information
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int

        // get image size
        val width = src.width
        val height = src.height

        // scan through every single pixel
        for (x in 0 until width) {
            for (y in 0 until height) {
                // get one pixel color
                pixel = src.getPixel(x, y)
                // retrieve color of all channels
                A = Color.alpha(pixel)
                R = Color.red(pixel)
                G = Color.green(pixel)
                B = Color.blue(pixel)
                // take conversion up to one single value
                B = (GS_RED * R + GS_GREEN * G + GS_BLUE * B).toInt()
                G = B
                R = G
                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }

        // return final image
        return bmOut
    }

    fun applyColorFilterEffect(src: Bitmap?, red: Double, green: Double, blue: Double): Bitmap? {
        if (src == null) return null

        // image size
        val width = src.width
        val height = src.height
        // create output bitmap
        val bmOut = Bitmap.createBitmap(width, height, src.config)
        // color information
        var A: Int
        var R: Int
        var G: Int
        var B: Int
        var pixel: Int

        // scan through all pixels
        for (x in 0 until width) {
            for (y in 0 until height) {
                // get pixel color
                pixel = src.getPixel(x, y)
                // apply filtering on each channel R, G, B
                A = Color.alpha(pixel)
                R = (Color.red(pixel) * red).toInt()
                G = (Color.green(pixel) * green).toInt()
                B = (Color.blue(pixel) * blue).toInt()
                // set new color pixel to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B))
            }
        }

        // return final image
        return bmOut
    }
}