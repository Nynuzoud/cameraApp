package com.example.sergeykuchin.adorablecameraapp

import android.app.Application

class App : Application() {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var prefs: Preferences


    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        configureLogging()

        AppInjector.init(this)

        setLang()
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> = dispatchingAndroidInjector

    private fun configureLogging() {

        Timber.plant(Timber.DebugTree())
    }
}