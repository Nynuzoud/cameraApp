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
import android.view.View
import com.example.sergeykuchin.adorablecameraapp.R
import com.example.sergeykuchin.adorablecameraapp.databinding.ActivityImageSetupBinding
import com.example.sergeykuchin.adorablecameraapp.other.extensions.simpleSubscribe
import com.example.sergeykuchin.adorablecameraapp.view.CommonActivity
import com.example.sergeykuchin.adorablecameraapp.view.imagesetup.adapter.*
import com.example.sergeykuchin.adorablecameraapp.viewmodel.imagesetup.ImageSetupActivityVM
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.InputStream


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

        disposable.add(viewModel.loadingVisibility.simpleSubscribe(
                onNext = {
                    binding.loading.visibility = it
                }))

        viewModel.setFileUri(intent.getStringExtra(FILE_URI))


        initFilterRecycler()

        setImageToImageView()

        binding.viewModel = viewModel
    }

    private fun setImageToImageView() {

        val inputStream = contentResolver.openInputStream(viewModel.getFileUri())

        Observable.just(inputStream)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
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
                    viewModel.setMinifiedBitmap(rotatedBitmap)

                    generateFilters()
                }.subscribeWith(object : DisposableObserver<InputStream>() {
                    override fun onComplete() {
                        Timber.d("onComplete")
                        viewModel.loadingVisibility.onNext(View.GONE)
                    }

                    override fun onNext(t: InputStream) {
                        Timber.d("onNext")
                    }

                    override fun onError(e: Throwable) {
                        Timber.d("onError")
                        viewModel.loadingVisibility.onNext(View.GONE)
                    }
                })


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
            viewModel.filterList = filterGenerator.generateFilters(viewModel.getMinifiedBitmap())
        }
        (viewModel.filterRecyclerConfiguration.adapter as FilterAdapter).data = viewModel.filterList
    }

    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }

    private inner class FilterListener : FilterAdapterListener {

        override fun onClick(imageFilter: ImageFilter) {
            Timber.d("onClick")
            viewModel.loadingVisibility.onNext(View.VISIBLE)
            Observable.just(imageFilter.id)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableObserver<Int>() {
                        override fun onComplete() {
                            Timber.d("onComplete")
                            viewModel.loadingVisibility.onNext(View.GONE)
                        }

                        override fun onNext(t: Int) {
                            Timber.d("onNext")
                            applyFilter(imageFilter.id)
                        }

                        override fun onError(e: Throwable) {
                            Timber.d("onError")
                            viewModel.loadingVisibility.onNext(View.GONE)
                        }
                    })
        }

        private fun applyFilter(id: Int) {
            when (id) {
                FilterGeneratorTypes.IMAGE_FILTER_ID_NORMAL.id -> binding.image.setImageBitmap(viewModel.rotatedBitmap)
                FilterGeneratorTypes.IMAGE_FILTER_ID_GREYSCALE.id -> binding.image.setImageBitmap(filterGenerator.applyGreyscaleEffect(viewModel.rotatedBitmap))
                FilterGeneratorTypes.IMAGE_FILTER_ID_RED.id -> {
                    binding.image.setImageBitmap(filterGenerator.applyColorFilterEffect(
                            viewModel.rotatedBitmap,
                            255.0,
                            0.0,
                            0.0))
                }
                FilterGeneratorTypes.IMAGE_FILTER_ID_GREEN.id -> {
                    binding.image.setImageBitmap(filterGenerator.applyColorFilterEffect(
                            viewModel.rotatedBitmap,
                            0.0,
                            255.0,
                            0.0))
                }
                FilterGeneratorTypes.IMAGE_FILTER_ID_BLUE.id -> {
                    binding.image.setImageBitmap(filterGenerator.applyColorFilterEffect(
                            viewModel.rotatedBitmap,
                            0.0,
                            0.0,
                            255.0))
                }
            }
        }
    }
}