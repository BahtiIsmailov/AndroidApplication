package com.example.englishapp.view.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.englishapp.R
import com.example.englishapp.databinding.CustomStarContainerLayoutBinding
import com.example.englishapp.utils.toPx

class ProgressForGrammarMainPageStar(context: Context, val attrs: AttributeSet?) : LinearLayout(context, attrs) {
    val lineHeight: Int
    val starsCount: Int
    val spaceLength: Int
    val activeStar: Drawable
    val inActiveStar: Drawable
    var learning_progress: Int = 0
        set(value) {
            field = value
            updateStars()
        }
    init {
        orientation = HORIZONTAL

        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ProgressForGrammer,
            0, 0
        )

        try {
            lineHeight = a.getDimensionPixelSize(R.styleable.ProgressForGrammer_starLineHeight, 40.toPx().toInt())
            starsCount = a.getInt(R.styleable.ProgressForGrammer_starsCount, 3)
            spaceLength = a.getDimensionPixelSize(R.styleable.ProgressForGrammer_starSpaceLength, 0.toPx().toInt())
            learning_progress = a.getInt(R.styleable.ProgressForGrammer_star_learning_progress, 0.toPx().toInt())
            activeStar = a.getDrawable(R.styleable.ProgressForGrammer_activeStar)!!
            inActiveStar = a.getDrawable(R.styleable.ProgressForGrammer_inActiveStar)!!

        } finally {
            a.recycle()
        }

        val binding = CustomStarContainerLayoutBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)

        updateStars()
    }

    private fun updateStars() {
        removeAllViewsInLayout()

        for (i in 0 until starsCount) {
            val iv = ImageView(context).apply {
                layoutParams = LayoutParams(
                    lineHeight,
                    lineHeight,
                ).apply {
                    weight = 1f
                    if(i != starsCount - 1) {
                        setMargins(0, 0, spaceLength, 0)
                    }
                }
            }

            if (i < learning_progress) {
                iv.setImageDrawable(activeStar)
                addView(iv)
            } else {
                iv.setImageDrawable(inActiveStar)
                addView(iv)
            }

        }
    }

}