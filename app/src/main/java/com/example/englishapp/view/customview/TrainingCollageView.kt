package com.example.englishapp.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.englishapp.R
import com.example.englishapp.databinding.CustomViewCollageBinding
import com.squareup.picasso.Picasso

class TrainingCollageView : FrameLayout {

    private var _binding: CustomViewCollageBinding? = null
    val binding get() = _binding!!

    private val imageViewIds = listOf(
        R.id.imageView,
        R.id.imageView2,
        R.id.imageView3,
        R.id.imageView4,
        R.id.imageView5,
        R.id.imageView6,
        R.id.imageView7,
        R.id.imageView8,
        R.id.imageView9,
        R.id.imageView10,
        R.id.imageView11,
        R.id.imageView12,
        R.id.imageView13,
        R.id.imageView14,
    )

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
        _binding = CustomViewCollageBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)
    }

    fun setImageUrls(urls: List<String>) {
        urls.forEachIndexed { index, s ->
            imageViewIds.getOrNull(index)?.let { viewId ->
                Picasso.get().load(s).placeholder(R.drawable.words_training_placeholder)
                    .into(binding.root.findViewById<ImageView>(viewId))
            }
        }

        for (i in (urls.size - 1).coerceAtLeast(0) until imageViewIds.size) {
            binding.root.findViewById<ImageView>(imageViewIds[i])
                .setImageResource(R.drawable.words_training_placeholder)
        }
    }
}