package com.example.englishapp.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.englishapp.databinding.CustomViewRebusGrammarButtonBinding

class RebusGrammarButtonView : FrameLayout {

    var _binding: CustomViewRebusGrammarButtonBinding? = null
    val binding get() = _binding!!

    var showAll = true
    set(value) {
        field = value
        if (value) {
            binding.backgrounCardView.isGone = true
            binding.fronCardView.isVisible = true
        }else{
            binding.fronCardView.isInvisible = true
            binding.backgrounCardView.isVisible = true
        }

    }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        _binding = CustomViewRebusGrammarButtonBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)

        showAll = true
    }

    fun setOnClickListener(l: () -> Unit) {
        binding.fronCardView.setOnClickListener(l)
    }

    fun setWord(word: String) {
        binding.fronCardView.text = word.replace(" ","")
    }
}
