package com.example.sergeykuchin.adorablecameraapp.helpers.camera

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CameraHelperImpl @Inject constructor(private val context: Context) {

    companion object {

        val REQUEST_TAKE_IMAGE_FROM_CAMERA = 1001
        val REQUEST_TAKE_IMAGE_FROM_GALLERY = 1002
    }

    fun takePictureFromCamera(activity: Activity): File? {

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(context.packageManager) != null) {

            val file = createPictureWithUniqueName()
            val photoURI = FileProvider.getUriForFile(context, "com.example.sergeykuchin.adorablecameraapp.ImageFileProvider", file)

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

            activity.startActivityForResult(takePictureIntent, REQUEST_TAKE_IMAGE_FROM_CAMERA)

            return file
        }

        return null
    }

    fun createPictureWithUniqueName(): File {

        val timeStamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
        val pictureFile = "CAMERA_APP_$timeStamp"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(pictureFile, ".jpg", storageDir)
    }
}