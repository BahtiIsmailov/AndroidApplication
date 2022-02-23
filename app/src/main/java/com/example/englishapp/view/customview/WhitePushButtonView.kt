package com.example.englishapp.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.englishapp.databinding.CustomViewWhitePushButtonBinding


class WhitePushButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : PushButtonView(context, attrs, defStyle) {

    init {
        init(attrs, defStyle)
    }

    override fun init(attrs: AttributeSet?, defStyle: Int) {
        val oneLetterBinding =
            CustomViewWhitePushButtonBinding.inflate(LayoutInflater.from(context))
        addView(oneLetterBinding.root)

        root = oneLetterBinding.root
        textView = oneLetterBinding.textView
        viewTop = oneLetterBinding.viewTop
        viewBack = oneLetterBinding.viewBack

        super.init(attrs, defStyle)
    }
}