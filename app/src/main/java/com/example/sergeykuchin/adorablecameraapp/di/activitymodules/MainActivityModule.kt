package com.example.sergeykuchin.adorablecameraapp.di.activitymodules

import com.example.sergeykuchin.adorablecameraapp.view.main.MainActivity
import com.example.sergeykuchin.adorablecameraapp.di.FragmentBuildersModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [(FragmentBuildersModule::class)])
    internal abstract fun contributeMainActivity(): MainActivity
}