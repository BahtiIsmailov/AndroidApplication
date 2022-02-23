package com.example.englishapp.view.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.englishapp.R
import com.example.englishapp.utils.toPx

class ArcProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {
    // The amount of degrees that we wanna reserve for the divider between 2 sections
    private val DIVIDER_ANGLE = 8f
    val DEGREES_IN_CIRCLE = 360f

    private val greenPaint = Paint()
    private val yellowPaint = Paint()
    private val dashPaint = Paint()
    private val backgroundPaint = Paint()
    private var totalSections = 3
    private var fullSections = 3

    private var lastMaxProgress = 8f
    private var lastProgress = 3f
    private var stateIndex = -1

    private var padding = 0

    private val rect = RectF()

    val greenGradient: Shader by lazy {
        val colors = intArrayOf(
            Color.parseColor("#67EC8D"),
            Color.parseColor("#38C53C")
        )

        val positions = floatArrayOf(0.0f, 1.0f)
        SweepGradient(width / 2F, height / 2F, colors, positions)
    }

    val yellowGradient: Shader by lazy {
        val colors = intArrayOf(
            Color.parseColor("#FFB800"),
            Color.parseColor("#FFD600")
        )

        val positions = floatArrayOf(0.0f, 1.0f)
        SweepGradient(width / 2F, height / 2F, colors, positions)
    }

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val strokeWidth = 3.toPx().toInt()
        setupPaint(context, strokeWidth, greenPaint, Color.parseColor("#5DD661"))
        //greenPaint.shader = greenGradient
        setupPaint(context, strokeWidth, yellowPaint, Color.parseColor("#FFB800"))
        //yellowPaint.shader = yellowGradient

        setupPaint(context, strokeWidth, backgroundPaint, context.resources.getColor(R.color.black10,context.theme))
    }

    private fun setupPaint(
        context: Context,
        strokeWidth: Int,
        backgroundPaint: Paint,
        color: Int
    ) {
        backgroundPaint.strokeCap = Paint.Cap.BUTT
        backgroundPaint.color = color
        backgroundPaint.isAntiAlias = true
        backgroundPaint.strokeWidth = strokeWidth.toFloat()
        backgroundPaint.style = Paint.Style.STROKE
    }

    fun getTotalSections(): Int {
        return totalSections
    }

    fun setTotalSections(totalSections: Int) {
        this.totalSections = totalSections
        invalidate()
    }

    fun getFullSections(): Int {
        return fullSections
    }

    fun setNumberOfSections(fullSections: Int, totalSections: Int) {
        this.fullSections = fullSections
        this.totalSections = totalSections
        invalidate()
    }


    fun setEmptyState() {
        totalSections = 3
        fullSections = 0
    }

    fun setRepeatAfter8Hour(timeLeft: Long) {
        totalSections = 3
        fullSections = 2
        stateIndex = 1

        lastMaxProgress = 8f
        lastProgress = 8f - timeLeft
    }

    fun setRepeatAfter24Hour(timeLeft: Long) {
        totalSections = 3
        fullSections = 3
        stateIndex = 2

        lastMaxProgress = 24f
        lastProgress = 24f - timeLeft
    }

    fun learned() {
        totalSections = 3
        fullSections = 3
        stateIndex = -1
    }



    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        rect.set(
            0.toFloat() + padding,
            (0).toFloat() + padding,
            (width).toFloat() - padding,
            height.toFloat() - padding
        )

        val angleOfSection = DEGREES_IN_CIRCLE / totalSections - DIVIDER_ANGLE

        var startAngle: Float
        for (i in 0 until totalSections) {
            startAngle = -90 + i * (angleOfSection + DIVIDER_ANGLE)
            canvas.drawArc(rect, startAngle, angleOfSection, false, backgroundPaint)
            canvas.drawArc(rect, startAngle + angleOfSection, DIVIDER_ANGLE, false, dashPaint)
        }

        for (i in 0 until totalSections) {
            startAngle = -90 + i * (angleOfSection + DIVIDER_ANGLE)
            if (i < fullSections) {
                if(i == stateIndex) {
                    val percentAngleOfSection = lastProgress * angleOfSection / lastMaxProgress

                    canvas.drawArc(rect, startAngle, percentAngleOfSection, false, yellowPaint)
                    canvas.drawArc(
                        rect,
                        startAngle + percentAngleOfSection,
                        DIVIDER_ANGLE,
                        false,
                        dashPaint
                    )
                } else {
                    canvas.drawArc(rect, startAngle, angleOfSection, false, greenPaint)
                    canvas.drawArc(
                        rect,
                        startAngle + angleOfSection,
                        DIVIDER_ANGLE,
                        false,
                        dashPaint
                    )
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.v("Chart onMeasure w", MeasureSpec.toString(widthMeasureSpec))
        Log.v("Chart onMeasure h", MeasureSpec.toString(heightMeasureSpec))

        val desiredWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val desiredHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        padding = paddingStart

        setMeasuredDimension(
            measureDimension(desiredWidth, widthMeasureSpec),
            measureDimension(desiredHeight, heightMeasureSpec)
        )
    }

    private fun measureDimension(desiredSize: Int, measureSpec: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = desiredSize
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        if (result < desiredSize) {
            Log.e("ChartView", "The view is too small, the content might get cut")
        }
        return result
    }
}