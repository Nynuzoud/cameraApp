package com.example.sergeykuchin.adorablecameraapp.view

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.databinding.ViewDataBinding
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class CommonActivity<M: ViewModel, B: ViewDataBinding> : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected lateinit var binding: B
    protected lateinit var viewModel: M

    protected val disposable = CompositeDisposable()

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector
}