package com.example.englishapp.view.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

import com.example.englishapp.R
import com.example.englishapp.utils.toPx

class CreateChipForGrammarCustomView(context: Context, attrs: AttributeSet?) :
    View(context, attrs) {
    private val spaceLength: Int
    private val center: Boolean
    private val backgroundPaint = Paint().apply {
        color = context.resources.getColor(R.color.grammar, context.theme)
        style = Paint.Style.FILL
    }
    val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 20f.toPx()
    }
    val dotPaint = Paint().apply {
        color = context.resources.getColor(R.color.grammar, context.theme)
        textSize = 40f.toPx()
    }
    val dotTextHight = dotPaint.fontMetrics.descent - dotPaint.fontMetrics.ascent
    val dotWidth = dotPaint.measureText("···")

    val rectPaddingX = 10f.toPx()
    val rectPaddingY = 10f.toPx()


    val textHight = textPaint.fontMetrics.descent - textPaint.fontMetrics.ascent

    val elementHight = textHight + rectPaddingY

    var elementsWord: List<String> = mutableListOf()
        set(value) {
            field = value
            invalidate()
        }

    init {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ChipForGrammar,
            0, 0
        )
        try {
            spaceLength = a.getDimensionPixelSize(R.styleable.ChipForGrammar_chipSpaceLength, 10)
            center = a.getBoolean(R.styleable.ChipForGrammar_center, false)
        } finally {
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val containerWidth = MeasureSpec.getSize(widthMeasureSpec)
        val containerHeight = MeasureSpec.getSize(heightMeasureSpec)
        val mode = MeasureSpec.getMode(widthMeasureSpec)

        if (elementsWord.isNotEmpty())
            setMeasuredDimension(
                containerWidth,
                (paddingTop + paddingBottom + elementHight).toInt()
            )
        else
            setMeasuredDimension(containerWidth, 0)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var widthCurrentElement = 0
        if (elementsWord.isEmpty())
            return
        var margin = 0
        if (center){
            val elementsWidth = getElementsWidth()
            margin = (width - elementsWidth)/2
            backgroundPaint.color = resources.getColor(R.color.black_for_grammar_begin_training,context.theme)
        }

        if (elementsWord.size == 1) {
            drawOneElement(canvas, elementsWord[0],margin)
        }

        else {
            for ((index, element) in elementsWord.withIndex()) {

                val textWidth = textPaint.measureText(elementsWord[index]).toInt()

                val elementWidth = rectPaddingX * 2 + textWidth
                val availableSpace =
                    width - paddingLeft - widthCurrentElement - paddingRight - dotWidth - spaceLength
                if (elementWidth < availableSpace) {
                    drawElement(canvas, elementWidth, element, widthCurrentElement,margin)
                } else {
                    canvas?.drawText(
                        "···",
                        (margin + paddingStart + widthCurrentElement).toFloat(),
                        paddingTop + rectPaddingY + dotTextHight / 2 - 7,
                        dotPaint
                    )
                    break
                }
                widthCurrentElement += (elementWidth + spaceLength).toInt()

            }
        }
    }

    private fun drawElement(
        canvas: Canvas?,
        elementWidth: Float,
        element: String,
        widthCurrentElement: Int,
        margin: Int
    ) {
        canvas?.drawRoundRect(
            margin + widthCurrentElement + paddingLeft.toFloat(),
            paddingTop.toFloat(),
            margin + widthCurrentElement + paddingLeft + elementWidth,
            paddingTop + elementHight,
            26f.toPx(),
            26f.toPx(),
            backgroundPaint
        )

        canvas?.drawText(
            element,
            margin + widthCurrentElement + paddingLeft + rectPaddingX,
            paddingTop + rectPaddingY + textHight / 2 + 2,
            textPaint
        )
    }

    fun drawOneElement(canvas: Canvas?, element: String,margin: Int) {
        var lettersWidth = textPaint.measureText(element).toInt()
        var elementWidth = rectPaddingX * 2 + lettersWidth
        if (elementWidth < width)
            drawElement(canvas, elementWidth, element,0,margin)
        else {
            for (index in element.indices) {
                lettersWidth = textPaint.measureText(element.substring(0..index)).toInt()
                elementWidth = rectPaddingX * 2 + lettersWidth
                if (elementWidth > width) {
                    drawElement(canvas, elementWidth, element.substring(0..index - 3) + "...",0,margin)
                    return
                }
            }
        }
    }

    fun getElementsWidth(): Int {
        var widthCurrentElement = 0
        for ((index) in elementsWord.withIndex()) {
            val textWidth = textPaint.measureText(elementsWord[index]).toInt()
            val elementWidth = rectPaddingX * 2 + textWidth
            val availableSpace =
                width - paddingLeft - widthCurrentElement - paddingRight - dotWidth - spaceLength
            if (elementWidth >= availableSpace)
                break
            widthCurrentElement += (elementWidth + spaceLength).toInt()
        }
        return widthCurrentElement
    }
}
