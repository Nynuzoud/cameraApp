package com.example.sergeykuchin.adorablecameraapp.view.camera.camera2helper

interface Camera2HelperErrorListener {

    fun errorCallback(e: Exception, message: Int, buttonText: Int, function: () -> Unit)
}