package com.example.englishapp.view.customview

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.englishapp.databinding.CustomViewPushButtonBinding


class SimplePushButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : PushButtonView(context, attrs, defStyle) {

    init {
        init(attrs, defStyle)
    }

    override fun init(attrs: AttributeSet?, defStyle: Int) {
        val binding = CustomViewPushButtonBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)

        root = binding.root
        textView = binding.textView
        viewTop = binding.viewTop
        viewBack = binding.viewBack

        super.init(attrs, defStyle)
    }
}