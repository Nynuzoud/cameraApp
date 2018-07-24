package com.example.sergeykuchin.adorablecameraapp.viewmodel.camera

import android.arch.lifecycle.ViewModel
import android.view.View
import com.example.sergeykuchin.adorablecameraapp.R
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class CameraActivityVM @Inject constructor() : ViewModel() {


    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////GETTERS/SETTERS///////////////////////////
    ///////////////////////////////////////////////////////////////////////////

    private var _flashStatus: FlashStatus = FlashStatus.OFF
    var flashStatus: FlashStatus
        get() = _flashStatus
        set(value) {
            _flashStatus = value
        }

    private val _flashResource: BehaviorSubject<Int> = BehaviorSubject.create()
    val flashResource: BehaviorSubject<Int>
        get() = _flashResource

    init {
        switchFlash()
    }

    fun switchFlash(view: View? = null) {
        when (_flashStatus) {
            FlashStatus.AUTO ->  {
                _flashStatus = FlashStatus.ON
                flashResource.onNext(R.drawable.ic_flash_on_white_24dp)
            }
            FlashStatus.ON -> {
                _flashStatus = FlashStatus.OFF
                flashResource.onNext(R.drawable.ic_flash_off_white_24dp)
            }
            FlashStatus.OFF -> {
                _flashStatus = FlashStatus.AUTO
                flashResource.onNext(R.drawable.ic_flash_auto_white_24dp)
            }
        }

    }

    fun takePicture(view: View) {}
    fun openGallery(view: View) {}
}