package com.example.sergeykuchin.adorablecameraapp.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.sergeykuchin.adorablecameraapp.viewmodel.ViewModelFactory
import com.example.sergeykuchin.adorablecameraapp.viewmodel.camera.CameraFragmentVM
import com.example.sergeykuchin.adorablecameraapp.viewmodel.imagesetup.ImageSetupFragmentVM
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CameraFragmentVM::class)
    internal abstract fun bindCameraActivityVM(cameraFragmentVM: CameraFragmentVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ImageSetupFragmentVM::class)
    internal abstract fun bindImageSetupActivityVM(imageSetupFragmentVM: ImageSetupFragmentVM): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}