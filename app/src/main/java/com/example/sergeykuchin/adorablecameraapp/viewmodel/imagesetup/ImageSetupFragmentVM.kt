package com.example.sergeykuchin.adorablecameraapp.viewmodel.imagesetup

import android.arch.lifecycle.ViewModel
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import com.example.sergeykuchin.adorablecameraapp.R
import com.example.sergeykuchin.adorablecameraapp.other.databinding.RecyclerConfiguration
import com.example.sergeykuchin.adorablecameraapp.other.utils.Utils
import com.example.sergeykuchin.adorablecameraapp.view.imagesetup.adapter.ImageFilter
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class ImageSetupFragmentVM @Inject constructor(private val utils: Utils): ViewModel() {

    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////GETTERS/SETTERS///////////////////////////
    ///////////////////////////////////////////////////////////////////////////

    private val _loadingVisibility: BehaviorSubject<Int> = BehaviorSubject.createDefault(View.VISIBLE)
    val loadingVisibility: BehaviorSubject<Int>
        get() = _loadingVisibility

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

    fun setMinifiedBitmap(bitmap: Bitmap?) {
        if (bitmap == null) return
        _minifiedBitmap = minifyBitmap(bitmap)
    }

    fun getMinifiedBitmap(): Bitmap? = _minifiedBitmap

    private fun minifyBitmap(bitmap: Bitmap): Bitmap? {

        return utils.decodeSampledBitmapFromResource(
                bitmap = bitmap,
                reqWidthRes = R.dimen.filter_item_width,
                reqHeightRes = R.dimen.filter_item_height
        )
    }


    private val _cachedBitmapUrisMap = HashMap<Int, Uri>()
    val cachedBitmapUrisMap: HashMap<Int, Uri>
        get() = _cachedBitmapUrisMap

    private var _selectedImageId: Int? = null
    var selectedImageId: Int?
        get() = _selectedImageId
        set(value) {
            _selectedImageId = value
        }
}