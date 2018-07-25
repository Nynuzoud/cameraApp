package com.example.sergeykuchin.adorablecameraapp.view.camera.camera2helper

import android.app.Activity
import com.example.sergeykuchin.adorablecameraapp.other.views.AutoFitTextureView
import com.example.sergeykuchin.adorablecameraapp.viewmodel.camera.FlashStatus

interface Camera2Helper {

    var activity: Activity?
    var textureView: AutoFitTextureView?
    var cameraLensFacingDirection: Int
    var flash: FlashStatus
    var errorListener: Camera2HelperErrorListener?
    var actionListener: Camera2HelperActionListener?

    fun switchCam(): Int
    fun createFile()
    fun takePicture()
    fun onResume()
    fun onPause()
}