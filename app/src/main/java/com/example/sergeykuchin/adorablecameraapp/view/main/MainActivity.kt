package com.example.sergeykuchin.adorablecameraapp.view.main

import android.Manifest
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.example.sergeykuchin.adorablecameraapp.R
import com.example.sergeykuchin.adorablecameraapp.databinding.ActivityMainBinding
import com.example.sergeykuchin.adorablecameraapp.other.extensions.showSnackBarError
import com.example.sergeykuchin.adorablecameraapp.other.permissions.PermissionCode
import com.example.sergeykuchin.adorablecameraapp.other.permissions.PermissionManager
import com.example.sergeykuchin.adorablecameraapp.view.CommonActivity
import com.example.sergeykuchin.adorablecameraapp.view.camera.CameraActivity
import com.example.sergeykuchin.adorablecameraapp.viewmodel.main.MainActivityVM

class MainActivity : CommonActivity<MainActivityVM, ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainActivityVM::class.java)


    }

    private fun openCam() {
        CameraActivity.start(this)
    }

    fun checkPermissions(view: View? = null) {
        if (PermissionManager.mayRequestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                        PermissionCode.COMMON_GROUP_REQUEST_CODE)) {
            openCam()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.filter { it == 0 }.size == permissions.size) {
            openCam()
        } else {
            binding.root.showSnackBarError(R.string.permission_error, R.string.ok, {checkPermissions()})
        }
    }
}
