package com.example.sergeykuchin.adorablecameraapp.view.camera

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.example.sergeykuchin.adorablecameraapp.R
import com.example.sergeykuchin.adorablecameraapp.databinding.ActivityCameraBinding
import com.example.sergeykuchin.adorablecameraapp.other.extensions.showSnackBarError
import com.example.sergeykuchin.adorablecameraapp.other.extensions.simpleSubscribe
import com.example.sergeykuchin.adorablecameraapp.view.CommonActivity
import com.example.sergeykuchin.adorablecameraapp.view.camera.camera2helper.Camera2Helper
import com.example.sergeykuchin.adorablecameraapp.view.camera.camera2helper.Camera2HelperActionListener
import com.example.sergeykuchin.adorablecameraapp.view.camera.camera2helper.Camera2HelperErrorListener
import com.example.sergeykuchin.adorablecameraapp.view.imagesetup.ImageSetupActivity
import com.example.sergeykuchin.adorablecameraapp.viewmodel.camera.CameraActivityVM
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class CameraActivity : CommonActivity<CameraActivityVM, ActivityCameraBinding>() {

    @Inject
    lateinit var camera2Helper: Camera2Helper

    private val camera2HelperErrorListener: Camera2HelperErrorListener = CameraErrorListener()
    private val camera2HelperActionListener: Camera2HelperActionListener = CameraActionListener()

    private val GET_FILE_REQUEST_CODE = 42

    companion object {

        fun start(activity: Activity) {

            val intent = Intent(activity, CameraActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_camera)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CameraActivityVM::class.java)

        binding.viewModel = viewModel

        viewModel.flashResource.simpleSubscribe(
                onNext = {
                    binding.flash.setImageDrawable(ContextCompat.getDrawable(this, it))
                    camera2Helper.flash = viewModel.flashStatus
                })

        viewModel.flashVisibility.simpleSubscribe(
                onNext = {
                    binding.flash.visibility = it
                })

        camera2Helper.activity = this
        camera2Helper.textureView = binding.texture
        camera2Helper.actionListener = camera2HelperActionListener
        camera2Helper.errorListener = camera2HelperErrorListener

        initCamera()
    }

    private fun initCamera() {

        camera2Helper.createFile()
    }

    fun takePicture(view: View) {
        camera2Helper.takePicture()
    }

    fun switchCam(view: View) {
        viewModel.switchFlashVisibility(camera2Helper.switchCam())
    }

    fun openGallery(view: View) {
        var intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(Intent.createChooser(intent, "Select Image"), GET_FILE_REQUEST_CODE)
        } else {
            intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            if (intent.resolveActivity(packageManager) != null) {
                startActivityForResult(Intent.createChooser(intent, "Select Image"), GET_FILE_REQUEST_CODE)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        camera2Helper.onResume()
    }

    override fun onPause() {
        super.onPause()
        camera2Helper.onPause()
    }

    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GET_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            ImageSetupActivity.start(this, data?.data?.toString())
        }
    }

    private inner class CameraErrorListener : Camera2HelperErrorListener {

        override fun errorCallback(e: Exception, message: Int, buttonText: Int, function: () -> Unit) {
            Timber.d("errorCallback ${e.message}")
            binding.root.showSnackBarError(message, buttonText, function)
        }
    }

    private inner class CameraActionListener : Camera2HelperActionListener {

        override fun pictureSaved(file: File?) {
            Timber.d("pictureSaved")
            ImageSetupActivity.start(this@CameraActivity, Uri.fromFile(file).toString())
        }
    }
}