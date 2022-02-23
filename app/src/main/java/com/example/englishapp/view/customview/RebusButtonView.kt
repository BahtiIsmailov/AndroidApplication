package com.example.englishapp.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.isVisible
import com.example.englishapp.R
import com.example.englishapp.databinding.CustomViewCollageBinding
import com.example.englishapp.databinding.CustomViewRebusButtonBinding
import com.squareup.picasso.Picasso

class RebusButtonView : FrameLayout {

    var _binding: CustomViewRebusButtonBinding? = null
    val binding get() = _binding!!

    private var badgeCount: Int = 0

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
        _binding = CustomViewRebusButtonBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)

        binding.tvBadge.isVisible = false
        binding.ivBg.isVisible = false
    }

    fun setLetter(letter: String, capitalLetter: Boolean) {
        binding.button.text = if (capitalLetter)
            letter.uppercase() else letter
    }

    fun setBadge(count: Int) {
        if (count > 1) {
            badgeCount = count
            binding.tvBadge.isVisible = true
            binding.tvBadge.text = count.toString()
        }
    }

    fun badgeDecrement() {
        badgeCount--

        binding.button.text = binding.button.text.lowercase()

        if (badgeCount == 1) {
            binding.tvBadge.isVisible = false
        }

        if (badgeCount <= 0) {
            binding.button.isVisible = false
            binding.ivBg.isVisible = true
        }
        binding.tvBadge.text = badgeCount.toString()
    }
}