package com.example.sergeykuchin.adorablecameraapp.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {


    ////////////////////COMMON///////////////////

    @Singleton
    @Provides
    fun provideContext(app: Application): Context = app.applicationContext

//    @Singleton
//    @Provides
//    fun provideCameraObject() = CameraObject()

//    /////////////////////Preferences////////////////////
//
//    @Singleton
//    @Provides
//    fun providePrefs(app: Application) : Preferences = PreferencesImpl(app)
//
//

}