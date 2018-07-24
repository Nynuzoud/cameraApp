package com.example.sergeykuchin.adorablecameraapp.view.camera

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.hardware.camera2.CameraCharacteristics
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.example.sergeykuchin.adorablecameraapp.R
import com.example.sergeykuchin.adorablecameraapp.databinding.ActivityCameraBinding
import com.example.sergeykuchin.adorablecameraapp.other.extensions.simpleSubscribe
import com.example.sergeykuchin.adorablecameraapp.view.CommonActivity
import com.example.sergeykuchin.adorablecameraapp.viewmodel.camera.CameraActivityVM
import javax.inject.Inject

class CameraActivity : CommonActivity<CameraActivityVM, ActivityCameraBinding>() {

    @Inject
    lateinit var camera2Helper: Camera2Helper

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

        viewModel.flashResource.simpleSubscribe({
            binding.flash.setImageDrawable(ContextCompat.getDrawable(this, it))
            camera2Helper.flash = viewModel.flashStatus
        })

        camera2Helper.activity = this
        camera2Helper.binding = binding

        initCamera()
    }

    private fun initCamera() {

        camera2Helper.createFile()
    }

    fun takePicture(view: View) {
        camera2Helper.takePicture()
    }

    fun switchCam(view: View) {
        when (camera2Helper.switchCam()) {
            CameraCharacteristics.LENS_FACING_BACK -> binding.flash.visibility = View.VISIBLE
            else -> binding.flash.visibility = View.GONE
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
}