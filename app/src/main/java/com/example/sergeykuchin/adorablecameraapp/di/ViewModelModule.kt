package com.example.sergeykuchin.adorablecameraapp.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.sergeykuchin.adorablecameraapp.viewmodel.ViewModelFactory
import com.example.sergeykuchin.adorablecameraapp.viewmodel.camera.CameraActivityVM
import com.example.sergeykuchin.adorablecameraapp.viewmodel.imagesetup.ImageSetupActivityVM
import com.example.sergeykuchin.adorablecameraapp.viewmodel.main.MainActivityVM
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityVM::class)
    internal abstract fun bindMainActivityVM(mainActivityVM: MainActivityVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CameraActivityVM::class)
    internal abstract fun bindCameraActivityVM(cameraActivityVM: CameraActivityVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ImageSetupActivityVM::class)
    internal abstract fun bindImageSetupActivityVM(imageSetupActivityVM: ImageSetupActivityVM): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}