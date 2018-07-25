package com.example.sergeykuchin.adorablecameraapp.di.activitymodules

import com.example.sergeykuchin.adorablecameraapp.di.FragmentBuildersModule
import com.example.sergeykuchin.adorablecameraapp.view.imagesetup.ImageSetupActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ImageSetupActivityModule {
    @ContributesAndroidInjector(modules = [(FragmentBuildersModule::class)])
    internal abstract fun contributeImageSetupActivity(): ImageSetupActivity
}