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
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.example.englishapp.R
import com.example.englishapp.utils.Event
import com.example.englishapp.utils.EventObserver
import com.example.englishapp.utils.StatusBarColor
import com.example.englishapp.view.main.words.wordsinwordset.training.base.UIState
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.windowInsetTypesOf


@AndroidEntryPoint
open class BaseFragment(): Fragment() {

    private var statusBarColor: StatusBarColor? = null
    private var useCustomInsets: Boolean = false

    constructor(
        statusBarColor: StatusBarColor
    ) : this() {
        this.statusBarColor = statusBarColor
    }

    constructor(
        useCustomInsets: Boolean
    ) : this() {
        this.useCustomInsets = useCustomInsets
    }

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

    @SuppressLint("ClickableViewAccessibility")
    fun addSwipeLeftToRightListener(view: View, callback: ()->Unit){
        view.setOnTouchListener { view, motionEvent ->
            when(motionEvent.action){
                MotionEvent.ACTION_DOWN -> x1 = motionEvent.x
                MotionEvent.ACTION_UP -> {
                    x1?.let{x1->
                        val delta = motionEvent.x - x1
                        if(delta>150)
                            callback()
                    }
                }
            }
            true
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    fun addSwipeRightToLeftListener(view: View, callback: ()->Unit){
        view.setOnTouchListener { view, motionEvent ->
            when(motionEvent.action){
                MotionEvent.ACTION_DOWN -> x1 = motionEvent.x
                MotionEvent.ACTION_UP -> {
                    x1?.let{x1->
                        val delta = x1 -  motionEvent.x
                        if(delta>150)
                            callback()
                    }
                }
            }
            true
        }
    }

    private fun Fragment.setWhiteStatusBar() {
        setStatusBarColor(
            requireActivity(),
            ContextCompat.getColor(requireContext(), R.color.white),
            true
        )
    }

    private fun Fragment.setVelvetStatusBar() {
        setStatusBarColor(
            requireActivity(),
            Color.parseColor("#3C00BE"),
            false
        )
    }

    private fun setStatusBarColor(activity: Activity, @ColorInt color: Int, shouldChangeStatusBarTintToDark: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = activity.window.decorView;
            if (shouldChangeStatusBarTintToDark) {
                activity.window.statusBarColor = color
                decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                // We want to change tint color to white again.
                // You can also record the flags in advance so that you can turn UI back completely if
                // you have set other flags before, such as translucent or full screen.
                activity.window.statusBarColor = color;
                decor.systemUiVisibility = 0;
            }
        }
    }

}