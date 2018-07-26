package com.example.sergeykuchin.adorablecameraapp.view.imagesetup

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Handler
import android.os.HandlerThread
import android.provider.MediaStore
import android.support.media.ExifInterface
import android.view.View
import com.example.sergeykuchin.adorablecameraapp.other.utils.Utils
import com.example.sergeykuchin.adorablecameraapp.view.imagesetup.adapter.FilterGenerator
import com.example.sergeykuchin.adorablecameraapp.view.imagesetup.adapter.FilterGeneratorTypes
import com.example.sergeykuchin.adorablecameraapp.view.imagesetup.adapter.ImageFilter
import com.example.sergeykuchin.adorablecameraapp.viewmodel.imagesetup.ImageSetupFragmentVM
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.FileOutputStream
import java.io.InputStream

class ImageSetupPresenterImpl(val view: ImageSetupFragmentView,
                              val utils: Utils,
                              val filterGenerator: FilterGenerator,
                              val contentResolver: ContentResolver,
                              val viewModel: ImageSetupFragmentVM) : ImageSetupPresenter {

    private fun getBitmapFromCache(id: Int) {

        val bitmap: Bitmap?
        if (id == FilterGeneratorTypes.IMAGE_FILTER_ID_NORMAL.id) {
            bitmap = viewModel.rotatedBitmap
        } else {

            val uri = viewModel.cachedBitmapUrisMap[id]
            if (uri == null) {
                bitmap = applyFilter(id)
                cacheBitmap(id, bitmap)
            } else {
                bitmap = getBitmapFromUri(uri)
            }
        }

        view.setMainImageBitmap(bitmap)
    }

    override fun setImageToImageView() {

        val inputStream = contentResolver.openInputStream(viewModel.getFileUri())

        Observable.just(inputStream)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<InputStream>() {
                    override fun onComplete() {
                        Timber.d("onComplete")
                        viewModel.loadingVisibility.onNext(View.GONE)
                    }

                    override fun onNext(t: InputStream) {
                        Timber.d("onNext")
                        val exifInterface = ExifInterface(inputStream)
                        val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

                        val bitmap = getBitmapFromUri(viewModel.getFileUri())

                        val rotatedBitmap = when (orientation) {
                            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90F)
                            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180F)
                            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270F)
                            else -> bitmap
                        }

                        view.setMainImageBitmap(rotatedBitmap)
                        viewModel.rotatedBitmap = rotatedBitmap
                        viewModel.setMinifiedBitmap(rotatedBitmap)

                        view.generateFilters()
                    }

                    override fun onError(e: Throwable) {
                        Timber.d("onError")
                        viewModel.loadingVisibility.onNext(View.GONE)
                    }
                })


    }

    override fun onFilterClick(imageFilter: ImageFilter) {
        Observable.just(imageFilter.id)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Int>() {
                    override fun onComplete() {
                        Timber.d("onComplete")
                        viewModel.loadingVisibility.onNext(View.GONE)
                    }

                    override fun onNext(id: Int) {
                        Timber.d("onNext")
                        getBitmapFromCache(id)
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

    private fun cacheBitmap(id: Int, bitmap: Bitmap?) {

        if (bitmap == null) return

        val file = utils.createPictureWithUniqueName()


        val handlerThread = HandlerThread("BitmapCaching")
        handlerThread.start()

        val backgroundHandler = Handler(handlerThread.looper)
        backgroundHandler.post {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
        }

        view.addCachedBitmapUri(id, Uri.fromFile(file))
    }


    private fun applyFilter(id: Int): Bitmap? {

        return when (id) {
            FilterGeneratorTypes.IMAGE_FILTER_ID_NORMAL.id -> viewModel.rotatedBitmap
            FilterGeneratorTypes.IMAGE_FILTER_ID_GREYSCALE.id -> filterGenerator.applyGreyscaleEffect(viewModel.rotatedBitmap)
            FilterGeneratorTypes.IMAGE_FILTER_ID_RED.id -> {
                filterGenerator.applyColorFilterEffect(
                        viewModel.rotatedBitmap,
                        255.0,
                        0.0,
                        0.0)
            }
            FilterGeneratorTypes.IMAGE_FILTER_ID_GREEN.id -> {
                filterGenerator.applyColorFilterEffect(
                        viewModel.rotatedBitmap,
                        0.0,
                        255.0,
                        0.0)
            }
            FilterGeneratorTypes.IMAGE_FILTER_ID_BLUE.id -> {
                filterGenerator.applyColorFilterEffect(
                        viewModel.rotatedBitmap,
                        0.0,
                        0.0,
                        255.0)
            } else -> null
        }
    }

    private fun getBitmapFromUri(fileUri: Uri?): Bitmap? {

        return MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
    }
}