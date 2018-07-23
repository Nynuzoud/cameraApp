package com.example.sergeykuchin.adorablecameraapp

import android.app.Activity
import android.app.Application
import android.support.v7.app.AppCompatDelegate
import com.example.sergeykuchin.adorablecameraapp.di.AppInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

class App : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

//    @Inject
//    lateinit var prefs: Preferences


    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        configureLogging()

        AppInjector.init(this)
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> = dispatchingAndroidInjector

    private fun configureLogging() {

        Timber.plant(Timber.DebugTree())
    }
}