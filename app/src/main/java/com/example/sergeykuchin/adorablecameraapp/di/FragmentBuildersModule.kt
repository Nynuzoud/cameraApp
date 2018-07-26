package com.example.sergeykuchin.adorablecameraapp.di

import com.example.sergeykuchin.adorablecameraapp.view.camera.CameraFragment
import com.example.sergeykuchin.adorablecameraapp.view.imagesetup.ImageSetupFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    internal abstract fun contributeCameraFragment(): CameraFragment

    @ContributesAndroidInjector
    internal abstract fun contributeImageSetupFragment(): ImageSetupFragment
}