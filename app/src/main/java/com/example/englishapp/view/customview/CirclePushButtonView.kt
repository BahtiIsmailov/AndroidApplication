package com.example.englishapp.view.customview

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.englishapp.databinding.CustomViewCirclePushButtonBinding
import com.example.englishapp.databinding.CustomViewPushButtonBinding


class CirclePushButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : PushButtonView(context, attrs, defStyle) {

    init {
        init(attrs, defStyle)
    }


    override fun init(attrs: AttributeSet?, defStyle: Int) {
        val binding = CustomViewCirclePushButtonBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)

        root = binding.root
        viewTop = binding.viewTop
        viewBack = binding.viewBack
        imageView = binding.imageView

        super.init(attrs, defStyle)
    }
}