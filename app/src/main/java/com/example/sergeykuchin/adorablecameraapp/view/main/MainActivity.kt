package com.example.sergeykuchin.adorablecameraapp.view.main

import android.arch.lifecycle.ViewModel
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.example.sergeykuchin.adorablecameraapp.R
import com.example.sergeykuchin.adorablecameraapp.databinding.ActivityMainBinding
import com.example.sergeykuchin.adorablecameraapp.view.CommonActivity
import com.example.sergeykuchin.adorablecameraapp.view.navigation.NavigationController
import javax.inject.Inject

class MainActivity : CommonActivity<ViewModel, ActivityMainBinding>() {

    @Inject
    lateinit var navigationController: NavigationController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (savedInstanceState == null) {
            navigationController.openCameraScreen()
        }
    }

    fun getBinding(): ActivityMainBinding = binding

    fun enableActionBar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    fun disableActionBar() {
        setSupportActionBar(null)
    }

    override fun onSupportNavigateUp(): Boolean {
        navigationController.openCameraScreen()
        return super.onSupportNavigateUp()
    }
}
