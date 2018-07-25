package com.example.sergeykuchin.adorablecameraapp.other.utils

import android.content.Context
import android.util.TypedValue

class UtilsImpl(val context: Context): Utils {

    override fun convertDpToPx(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
    }

    override fun convertDpToPx(dpRes: Int): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, context.resources.getDimension(dpRes), context.resources.displayMetrics)
    }
}