package com.example.englishapp.view.customview

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.englishapp.R
import com.example.englishapp.databinding.CustomViewPushButtonBinding
import com.example.englishapp.utils.toPx


abstract class PushButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    lateinit var root: View
    var textView: TextView? = null
    lateinit var viewBack: FrameLayout
    lateinit var viewTop: FrameLayout
    var imageView: ImageView? = null

    var text: String = ""
        set(value) {
            field = value
            textView?.text = value
        }

    fun setTextColor(@ColorInt color: Int) {
        textView?.setTextColor(color)
    }

    open fun init(attrs: AttributeSet?, defStyle: Int) {

        val attrText = context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.text))
        val text = attrText.getText(0)
        attrText.recycle()

        val attrTextSize =
            context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.textSize))
        val textSize = attrTextSize.getDimensionPixelSize(0, -1)
        attrTextSize.recycle()

        val attrTextColor =
            context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.textColor))
        val colorRes = attrTextColor.getResourceId(0, -1)
        attrTextColor.recycle()

        val attrTextFont =
            context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.fontFamily))
        val textFont = attrTextFont.getResourceId(0, -1)
        attrTextFont.recycle()



        if (!text.isNullOrEmpty()) {
            textView?.text = text
        }

        if (colorRes != -1) {
            textView?.setTextColor(ContextCompat.getColor(context, colorRes))
        }

        if (textSize != -1) {
            textView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        }


        if (textFont != -1) {
            val face = ResourcesCompat.getFont(context, textFont)
            textView?.typeface = face
        }
    }

    fun setOnPushListener(l: () -> Unit) {
        setListener(l, true)
    }

    fun setOnClickListener(l: () -> Unit) {
        setListener(l, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setListener(l: () -> Unit, withoutActionUp: Boolean = false) {
        root.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {

                    viewTop.animate()
                        .translationY(4.toPx())
                        .setDuration(100)
                        .start()

                    textView?.animate()
                        ?.translationY(4.toPx())
                        ?.setDuration(100)
                        ?.start()

                    imageView
                        ?.animate()
                        ?.translationY(4.toPx())
                        ?.setDuration(100)
                        ?.start()
                }
                MotionEvent.ACTION_UP -> {

                    if (!withoutActionUp) {
                        viewTop.animate()
                            .translationY(0f)
                            .setDuration(100)
                            .withEndAction { l.invoke() }
                            .start()

                        textView?.animate()
                            ?.translationY(0f)
                            ?.setDuration(100)
                            ?.start()

                        imageView
                            ?.animate()
                            ?.translationY(0f)
                            ?.setDuration(100)
                            ?.start()
                    } else {
                        l.invoke()
                    }
                }
                MotionEvent.ACTION_CANCEL -> {
                    viewTop.animate()
                        .translationY(0f)
                        .setDuration(100)
                        .start()

                    textView?.animate()
                        ?.translationY(0f)
                        ?.setDuration(100)
                        ?.start()

                    imageView
                        ?.animate()
                        ?.translationY(0f)
                        ?.setDuration(100)
                        ?.start()
                }
            }

            true
        }

    }
}