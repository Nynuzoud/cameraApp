package com.example.sergeykuchin.adorablecameraapp.other.permissions

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.View
import java.util.*

class PermissionManager {
    fun mayRequestPermissions(activity: Activity, permission: String,
                              requestCode: Int, snackBarView: View, resId: Int): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        if (activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        if (activity.shouldShowRequestPermissionRationale(permission)) {
            Snackbar.make(snackBarView, resId, Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok)
            { activity.requestPermissions(arrayOf(permission), requestCode) }.setDuration(Snackbar.LENGTH_LONG).show()
        } else {
            activity.requestPermissions(arrayOf(permission), requestCode)
        }
        return false
    }

    fun mayRequestPermissions(activity: Activity, permissions: Array<String>,
                              requestCode: Int, snackBarView: View, resId: Int): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        var count = 0
        for (permission in permissions) {
            if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {

                break
            }
            count++
        }
        if (count == permissions.size) {
            return true
        }
        for (permission in permissions) {
            if (activity.shouldShowRequestPermissionRationale(permission)) {
                Snackbar.make(snackBarView, resId, Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok)
                { activity.requestPermissions(permissions, requestCode) }.setDuration(Snackbar.LENGTH_LONG).show()
            } else {
                activity.requestPermissions(permissions, requestCode)
            }
        }
        return false
    }

    fun mayRequestPermissions(fragment: Fragment, permission: String,
                              requestCode: Int, snackBarView: View, resId: Int): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        if (fragment.context?.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        if (fragment.shouldShowRequestPermissionRationale(permission)) {
            Snackbar.make(snackBarView, resId, Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok)
            { fragment.requestPermissions(arrayOf(permission), requestCode) }.setDuration(Snackbar.LENGTH_LONG).show()
        } else {
            fragment.requestPermissions(arrayOf(permission), requestCode)
        }
        return false
    }

    fun mayRequestPermissions(fragment: Fragment, permissions: Array<String>,
                              requestCode: Int, snackBarView: View, resId: Int): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }

        val permissionsTemp = ArrayList<String>()
        for (permission in permissions) {
            if (fragment.context?.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsTemp.add(permission)
            }
        }
        if (permissionsTemp.isEmpty()) {
            return true
        }

        val stringArray = Arrays.copyOf<String, Any>(permissionsTemp.toTypedArray(),
                permissionsTemp.size, Array<String>::class.java)
        for (permission in permissionsTemp) {
            if (fragment.shouldShowRequestPermissionRationale(permission)) {
                Snackbar.make(snackBarView, resId, Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok)
                { fragment.requestPermissions(stringArray, requestCode) }.setDuration(Snackbar.LENGTH_LONG).show()
            } else {
                fragment.requestPermissions(stringArray, requestCode)
            }
        }
        return false
    }
}