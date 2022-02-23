package com.example.englishapp.view.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.example.englishapp.R
import com.example.englishapp.utils.toPx
import java.lang.Exception

class ProgressBarForLearning(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    val lineHeight: Int
    val linesCount: Int
    val spaceLength: Int
    val activeLine: Drawable
    val inActiveLine: Drawable
    var learning_progress: Int = 0
        set(value) {
            field = value
            invalidate()
        }


    init {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ProgressForLearning,
            0, 0
        )

        try {

            lineHeight = a.getDimensionPixelSize(R.styleable.ProgressForLearning_lineHeight, 8.toPx().toInt())
            linesCount = a.getInt(R.styleable.ProgressForLearning_linesCount, 4)
            spaceLength = a.getDimensionPixelSize(R.styleable.ProgressForLearning_spaceLength, 12)
            learning_progress = a.getInt(R.styleable.ProgressForLearning_learning_progress, 0)
            activeLine = a.getDrawable(R.styleable.ProgressForLearning_activeLine)!!
            inActiveLine = a.getDrawable(R.styleable.ProgressForLearning_inActiveLine)!!

        } finally {
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = paddingTop + paddingBottom + lineHeight
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)

        setMeasuredDimension(parentWidth, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val countSpace = linesCount - 1
        val allSpaceLength = countSpace * spaceLength
        val availableSpaceforLines = width - allSpaceLength
        val lineLength = availableSpaceforLines / linesCount

        for (i in 0 until linesCount) {
            val delta = (lineLength + spaceLength) * i
            val topLeftPointY = paddingTop
            val topLeftPointX = paddingLeft + delta
            val rightBottomX = paddingLeft + lineLength + delta
            val rightBottomY = paddingTop + lineHeight

            if (i < learning_progress) {
                activeLine.setBounds(topLeftPointX, topLeftPointY, rightBottomX, rightBottomY)
                activeLine.draw(canvas)
            } else {
                inActiveLine.setBounds(topLeftPointX, topLeftPointY, rightBottomX, rightBottomY)
                inActiveLine.draw(canvas)
            }

        }

    }
}