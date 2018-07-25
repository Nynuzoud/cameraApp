package com.example.sergeykuchin.adorablecameraapp.di

import android.app.Application
import android.content.Context
import com.example.sergeykuchin.adorablecameraapp.other.utils.Utils
import com.example.sergeykuchin.adorablecameraapp.other.utils.UtilsImpl
import com.example.sergeykuchin.adorablecameraapp.view.camera.camera2helper.Camera2Helper
import com.example.sergeykuchin.adorablecameraapp.view.camera.camera2helper.Camera2HelperImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {


    ////////////////////COMMON///////////////////
    @Singleton
    @Provides
    fun provideContext(app: Application): Context = app.applicationContext

    /////////////CAMERA HELPER MODULE////////////
    @Singleton
    @Provides
    fun provideCamera2Helper(context: Context): Camera2Helper = Camera2HelperImpl(context)

    ////////////////////UTILS//////////////////
    @Singleton
    @Provides
    fun provideUtils(context: Context): Utils = UtilsImpl(context)

}