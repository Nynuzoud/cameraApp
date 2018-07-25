package com.example.sergeykuchin.adorablecameraapp.view.imagesetup

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.media.ExifInterface
import android.support.v7.widget.LinearLayoutManager
import com.example.sergeykuchin.adorablecameraapp.R
import com.example.sergeykuchin.adorablecameraapp.databinding.ActivityImageSetupBinding
import com.example.sergeykuchin.adorablecameraapp.view.CommonActivity
import com.example.sergeykuchin.adorablecameraapp.view.imagesetup.adapter.FilterAdapter
import com.example.sergeykuchin.adorablecameraapp.view.imagesetup.adapter.FilterAdapterListener
import com.example.sergeykuchin.adorablecameraapp.view.imagesetup.adapter.FilterGenerator
import com.example.sergeykuchin.adorablecameraapp.view.imagesetup.adapter.ImageFilter
import com.example.sergeykuchin.adorablecameraapp.viewmodel.imagesetup.ImageSetupActivityVM
import timber.log.Timber


class ImageSetupActivity : CommonActivity<ImageSetupActivityVM, ActivityImageSetupBinding>() {

    companion object {

        private val FILE_URI = "file_uri"

        fun start(activity: Activity, fileUri: String?) {

            val intent = Intent(activity, ImageSetupActivity::class.java)
            intent.putExtra(FILE_URI, fileUri)
            activity.startActivity(intent)
        }
    }

    private val filterGenerator = FilterGenerator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_setup)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ImageSetupActivityVM::class.java)

        viewModel.setFileUri(intent.getStringExtra(FILE_URI))

        setImageToImageView()

        initFilterRecycler()
        generateFilters()

        binding.viewModel = viewModel
    }

    private fun setImageToImageView() {

        val inputStream = contentResolver.openInputStream(viewModel.getFileUri())

        val exifInterface = ExifInterface(inputStream)
        val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

        val bitmap = getBitmapFromUri(viewModel.getFileUri())

        val rotatedBitmap = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90F)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180F)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270F)
            else -> bitmap
        }

        binding.image.setImageBitmap(rotatedBitmap)
        viewModel.rotatedBitmap = rotatedBitmap
        viewModel.minifiedBitmap = rotatedBitmap
    }

    private fun rotateImage(source: Bitmap?, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source?.width ?: 0, source?.height ?: 0,
                matrix, true)
    }

    private fun getBitmapFromUri(fileUri: Uri?): Bitmap? {

        return MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
    }

    private fun initFilterRecycler() {
        viewModel.filterRecyclerConfiguration.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        viewModel.filterRecyclerConfiguration.adapter = FilterAdapter()
        viewModel.filterRecyclerConfiguration.adapter?.setHasStableIds(true)
        (viewModel.filterRecyclerConfiguration.adapter as FilterAdapter).listener = FilterListener()
    }

    private fun generateFilters() {
        if (viewModel.filterList == null) {
            viewModel.filterList = filterGenerator.generateFilters(viewModel.minifiedBitmap)
        }
        (viewModel.filterRecyclerConfiguration.adapter as FilterAdapter).data = viewModel.filterList
    }

    private inner class FilterListener : FilterAdapterListener {

        override fun onClick(imageFilter: ImageFilter) {
            Timber.d("onClick")
        }
    }
}