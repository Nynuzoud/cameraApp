package com.example.sergeykuchin.adorablecameraapp.view

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.databinding.ViewDataBinding
import android.support.v4.app.Fragment
import com.example.sergeykuchin.adorablecameraapp.di.Injectable
import com.example.sergeykuchin.adorablecameraapp.view.navigation.NavigationController
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class CommonFragment<M: ViewModel, B: ViewDataBinding> : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigationController: NavigationController

    protected lateinit var binding: B
    protected lateinit var viewModel: M

    protected val disposable = CompositeDisposable()

}