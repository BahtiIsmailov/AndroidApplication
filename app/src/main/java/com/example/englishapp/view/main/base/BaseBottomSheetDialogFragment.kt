package com.example.englishapp.view.main.base

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.example.englishapp.R
import com.example.englishapp.utils.Event
import com.example.englishapp.utils.EventObserver
import com.example.englishapp.utils.StatusBarColor
import com.example.englishapp.view.main.words.wordsinwordset.training.base.UIState
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.windowInsetTypesOf


@AndroidEntryPoint
open class BaseBottomSheetDialogFragment: BottomSheetDialogFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    var x1: Float? = null
    fun<T> LiveData<T>.observe(observer: (T) -> Unit) {
        this.observe(viewLifecycleOwner, observer)
    }

    fun<T> LiveData<Event<T>>.observeEvent(observer: (T) -> Unit) {
        this.observe(viewLifecycleOwner, EventObserver{
            observer(it)
        })
    }
    fun<T> LiveData<UIState<T>>.observeUIState(onLoading: (()-> Unit)? = null, onError:((String)-> Unit)? = null, onSuccess: (T)-> Unit) {
        this.observe(viewLifecycleOwner){
            when (it) {
                is UIState.Loading -> {
                    onLoading?.invoke()
                }
                is UIState.Success -> {
                    onSuccess(it.data)

                }
                is UIState.Error -> {
                    onError?.invoke(it.error)
                }
            }
        }
    }
}