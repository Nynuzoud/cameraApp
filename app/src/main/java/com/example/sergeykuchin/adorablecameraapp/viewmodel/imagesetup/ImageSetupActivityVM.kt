package com.example.sergeykuchin.adorablecameraapp.viewmodel.imagesetup

import android.arch.lifecycle.ViewModel
import android.graphics.Bitmap
import android.net.Uri
import com.example.sergeykuchin.adorablecameraapp.R
import com.example.sergeykuchin.adorablecameraapp.other.databinding.RecyclerConfiguration
import com.example.sergeykuchin.adorablecameraapp.other.utils.Utils
import com.example.sergeykuchin.adorablecameraapp.view.imagesetup.adapter.ImageFilter
import javax.inject.Inject

class ImageSetupActivityVM @Inject constructor(val utils: Utils): ViewModel() {

    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////GETTERS/SETTERS///////////////////////////
    ///////////////////////////////////////////////////////////////////////////

    private var _fileUri: Uri? = null

    fun setFileUri(fileUri: String?) {
        _fileUri = Uri.parse(fileUri)
    }

    fun getFileUri(): Uri? = _fileUri

    private var _filterRecyclerConfiguration = RecyclerConfiguration()
    var filterRecyclerConfiguration: RecyclerConfiguration
        get() = _filterRecyclerConfiguration
        set(value) {
            _filterRecyclerConfiguration = value
        }

    private var _filterList: List<ImageFilter>? = null
    var filterList: List<ImageFilter>?
        get() = _filterList
        set(value) {
            _filterList = value
        }

    private var _rotatedBitmap: Bitmap? = null
    var rotatedBitmap: Bitmap?
        get() = _rotatedBitmap
        set(value) {
            _rotatedBitmap = value
        }

    private var _minifiedBitmap: Bitmap? = null
    var minifiedBitmap: Bitmap?
        get() = _minifiedBitmap
        set(value) {
            _minifiedBitmap = minifyBitmap(value)
        }

    private fun minifyBitmap(bitmap: Bitmap?): Bitmap? {
        if (bitmap == null) return null

        return Bitmap.createScaledBitmap(bitmap,
                utils.convertDpToPx(R.dimen.filter_item_width).toInt(),
                utils.convertDpToPx(R.dimen.filter_item_height).toInt(),
                false)
    }
}