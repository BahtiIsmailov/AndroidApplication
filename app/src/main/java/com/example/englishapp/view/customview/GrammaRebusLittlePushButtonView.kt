package com.example.englishapp.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.englishapp.databinding.CustomViewGrammaRebusLittleButtonBinding
import com.example.englishapp.databinding.CustomViewRebusLittleButtonBinding


class GrammaRebusLittlePushButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : PushButtonView(context, attrs, defStyle) {

    init {
        init(attrs, defStyle)
    }


    override fun init(attrs: AttributeSet?, defStyle: Int) {
        val binding = CustomViewGrammaRebusLittleButtonBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)

        root = binding.root
        viewTop = binding.viewTop
        viewBack = binding.viewBack
        textView = binding.textView

        super.init(attrs, defStyle)
    }
}