package com.example.sergeykuchin.adorablecameraapp.di

import android.app.Application
import com.example.sergeykuchin.adorablecameraapp.App
import com.example.sergeykuchin.adorablecameraapp.di.activitymodules.CameraActivityModule
import com.example.sergeykuchin.adorablecameraapp.di.activitymodules.MainActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    MainActivityModule::class,
    CameraActivityModule::class])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
    fun inject(app: App)
}