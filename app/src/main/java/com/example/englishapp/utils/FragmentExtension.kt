package com.example.englishapp.utils

import android.annotation.SuppressLint
import android.graphics.Rect
import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.google.android.material.internal.ViewUtils.requestApplyInsetsWhenAttached
import java.time.Duration


fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(requireContext(), message, duration).show()
}

fun Fragment.showToast(resId: Int, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(requireContext(), resources.getString(resId), duration).show()
}

fun View.doOnApplyWindowInsets(block: (View, WindowInsetsCompat, Rect) -> WindowInsetsCompat) {

    val initialPadding = recordInitialPaddingForView(this)

    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        block(v, insets, initialPadding)
    }

    requestApplyInsetsWhenAttached()
}

private fun recordInitialPaddingForView(view: View) =
    Rect(view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

fun View.setStatusBarPadding() {
    doOnApplyWindowInsets { v, insets, padding ->
        v.updatePadding(
            top = padding.top + insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
        )
        insets
    }
}