package com.example.sergeykuchin.adorablecameraapp.view.camera

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sergeykuchin.adorablecameraapp.R
import com.example.sergeykuchin.adorablecameraapp.databinding.FragmentCameraBinding
import com.example.sergeykuchin.adorablecameraapp.other.databinding.FragmentDataBindingComponent
import com.example.sergeykuchin.adorablecameraapp.other.extensions.showSnackBarError
import com.example.sergeykuchin.adorablecameraapp.other.extensions.simpleSubscribe
import com.example.sergeykuchin.adorablecameraapp.other.permissions.PermissionCode
import com.example.sergeykuchin.adorablecameraapp.other.permissions.PermissionManager
import com.example.sergeykuchin.adorablecameraapp.view.CommonFragment
import com.example.sergeykuchin.adorablecameraapp.view.camera.camera2helper.Camera2Helper
import com.example.sergeykuchin.adorablecameraapp.view.camera.camera2helper.Camera2HelperActionListener
import com.example.sergeykuchin.adorablecameraapp.view.camera.camera2helper.Camera2HelperErrorListener
import com.example.sergeykuchin.adorablecameraapp.view.main.MainActivity
import com.example.sergeykuchin.adorablecameraapp.viewmodel.camera.CameraFragmentVM
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class CameraFragment : CommonFragment<CameraFragmentVM, FragmentCameraBinding>() {

    @Inject
    lateinit var camera2Helper: Camera2Helper

    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    private val camera2HelperErrorListener: Camera2HelperErrorListener = CameraErrorListener()
    private val camera2HelperActionListener: Camera2HelperActionListener = CameraActionListener()

    private val GET_FILE_REQUEST_CODE = 42

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_camera, container, false, dataBindingComponent)

        binding.takePictureWhiteBackground.setOnClickListener { takePicture() }
        binding.openGallery.setOnClickListener { openGallery() }
        binding.switchCam.setOnClickListener { switchCam() }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CameraFragmentVM::class.java)

        binding.viewModel = viewModel

        viewModel.flashResource.simpleSubscribe(
                onNext = {
                    binding.flash.setImageDrawable(ContextCompat.getDrawable(context!!, it))
                    camera2Helper.flash = viewModel.flashStatus
                })

        viewModel.flashVisibility.simpleSubscribe(
                onNext = {
                    binding.flash.visibility = it
                })

        camera2Helper.activity = activity
        camera2Helper.textureView = binding.texture
        camera2Helper.actionListener = camera2HelperActionListener
        camera2Helper.errorListener = camera2HelperErrorListener

        if (checkPermissions()) initCamera()
    }

    private fun checkPermissions(): Boolean {
        if (PermissionManager.mayRequestPermissions(
                        fragment = this,
                        permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                        requestCode = PermissionCode.COMMON_GROUP_REQUEST_CODE,
                        snackBarView = (activity as MainActivity).getBinding().root,
                        resId = R.string.permission_error)) {
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.filter { it == 0 }.size == permissions.size) {
            initCamera()
        } else {
            binding.root.showSnackBarError(R.string.permission_error, R.string.ok) {
                if (checkPermissions()) {
                    initCamera()
                }
            }
        }
    }

    private fun initCamera() {

        camera2Helper.createFile()
    }

    private fun takePicture() {
        camera2Helper.takePicture()
    }

    private fun switchCam() {
        viewModel.switchFlashVisibility(camera2Helper.switchCam())
    }

    private fun openGallery() {
        var intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (intent.resolveActivity(activity?.packageManager) != null) {
            startActivityForResult(Intent.createChooser(intent, "Select Image"), GET_FILE_REQUEST_CODE)
        } else {
            intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            if (intent.resolveActivity(activity?.packageManager) != null) {
                startActivityForResult(Intent.createChooser(intent, "Select Image"), GET_FILE_REQUEST_CODE)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (checkPermissions()) {
            camera2Helper.onResume()
        }
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
            navigationController.openFiltersScreen(data?.data?.toString())
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
            if (file == null) return
            navigationController.openFiltersScreen(Uri.fromFile(file).toString())
        }
    }
}