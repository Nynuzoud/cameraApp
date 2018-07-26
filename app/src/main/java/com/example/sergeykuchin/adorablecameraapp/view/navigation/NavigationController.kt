package com.example.sergeykuchin.adorablecameraapp.view.navigation

import android.os.Bundle
import android.support.v4.app.FragmentManager
import com.example.sergeykuchin.adorablecameraapp.R
import com.example.sergeykuchin.adorablecameraapp.view.camera.CameraFragment
import com.example.sergeykuchin.adorablecameraapp.view.imagesetup.ImageSetupFragment
import com.example.sergeykuchin.adorablecameraapp.view.main.MainActivity
import javax.inject.Inject

class NavigationController @Inject constructor(val mainActivity: MainActivity) {

    private var containerId: Int? = null
    private var fragmentManager: FragmentManager? = null

    init {
        containerId = R.id.container
        fragmentManager = mainActivity.supportFragmentManager
    }

    fun openCameraScreen() {

        mainActivity.disableActionBar()

        fragmentManager?.beginTransaction()
                ?.replace(containerId!!, CameraFragment())
                ?.commitAllowingStateLoss()
    }

    fun openFiltersScreen(uriString: String?) {
        val imageSetupFragment = ImageSetupFragment()
        val bundle = Bundle()
        bundle.putString(ImageSetupFragment.FILE_URI, uriString)
        imageSetupFragment.arguments = bundle

        fragmentManager?.beginTransaction()
                ?.replace(containerId!!, imageSetupFragment)
                ?.commitAllowingStateLoss()
    }
}