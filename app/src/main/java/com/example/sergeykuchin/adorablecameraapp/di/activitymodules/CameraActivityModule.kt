package com.example.sergeykuchin.adorablecameraapp.di.activitymodules

import com.example.sergeykuchin.adorablecameraapp.di.FragmentBuildersModule
import com.example.sergeykuchin.adorablecameraapp.view.camera.CameraActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CameraActivityModule {
    @ContributesAndroidInjector(modules = [(FragmentBuildersModule::class)])
    internal abstract fun contributeCameraActivity(): CameraActivity
}