package com.example.sergeykuchin.adorablecameraapp.view.camera

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.example.sergeykuchin.adorablecameraapp.R
import com.example.sergeykuchin.adorablecameraapp.databinding.ActivityCameraBinding
import com.example.sergeykuchin.adorablecameraapp.view.CommonActivity
import com.example.sergeykuchin.adorablecameraapp.viewmodel.camera.CameraActivityVM

class CameraActivity : CommonActivity<CameraActivityVM, ActivityCameraBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_camera)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CameraActivityVM::class.java)
    }
}