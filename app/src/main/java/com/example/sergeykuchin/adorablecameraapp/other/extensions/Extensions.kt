package com.example.sergeykuchin.adorablecameraapp.other.extensions

import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.Subject

fun <T> Subject<T>.simpleSubscribe(onNext: ((obj: T) -> Unit)? = null, onError: ((obj: Throwable) -> Unit)? = null, onCompleted: (() -> Unit)? = null): Disposable {

    return this
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    { onNext?.invoke(it) },
                    { onError?.invoke(it) },
                    { onCompleted?.invoke() })
}

fun View.showSnackBarErrorLoadData(@StringRes message: Int, @StringRes buttonText: Int, function: () -> Unit) {
    val snackbar = Snackbar
            .make(this, message, Snackbar.LENGTH_INDEFINITE)

    snackbar
            .setAction(buttonText) {
                function()
                snackbar.dismiss()
            }
            .show()
}