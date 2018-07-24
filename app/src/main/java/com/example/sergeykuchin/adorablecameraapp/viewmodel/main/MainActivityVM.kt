package com.example.sergeykuchin.adorablecameraapp.viewmodel.main

import android.arch.lifecycle.ViewModel
import com.example.sergeykuchin.adorablecameraapp.helpers.camera.CameraHelperImpl
import javax.inject.Inject

class MainActivityVM @Inject constructor(private val cameraHelperImpl: CameraHelperImpl) : ViewModel() {


//    fun openCam(activity: Activity) {
//
//        //cameraHelperImpl.takePictureFromCamera(activity)
//        //TODO
//        CameraActivity.start(activity)
//    }
//
//    fun checkPermissions(activity: Activity) {
//        if (PermissionManager.mayRequestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
//                        PermissionCode.COMMON_GROUP_REQUEST_CODE)) {
//            openCam(activity)
//        }
//    }
}