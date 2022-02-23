package com.example.englishapp.utils

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import android.graphics.drawable.VectorDrawable
import androidx.core.content.ContextCompat
import android.os.Build
import android.annotation.TargetApi
import android.content.Context
import java.lang.IllegalArgumentException
import javax.inject.Singleton

class ParallaxEffect(
    val resources: Resources,
    val bitmap: Bitmap,
    val image: ImageView,
    private val interpolator: TimeInterpolator,
    private val animationDuration: Long
) {
    private lateinit var animator:ValueAnimator
    fun runAnimation(){

        val drawable: BitmapDrawable = object : BitmapDrawable(resources, bitmap) {
            override fun draw(canvas: Canvas) {
                super.draw(canvas)
                canvas.drawBitmap(bitmap, intrinsicWidth.toFloat(),0f, null)// получает картинку и дорисовывает справа
            }
        }

        image.setImageDrawable(drawable) // установка картинки в имадж сюда саму каринку
        image.scaleType = ImageView.ScaleType.MATRIX // создаешь матрицу для будущей анимации

        animator = ValueAnimator.ofFloat(0f) // как будет работать сама анимация
        animator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            private val matrix: Matrix = Matrix()
            override fun onAnimationUpdate(animator: ValueAnimator) {
                matrix.reset()
                val height: Float = image.drawable.intrinsicHeight.toFloat()
                val width: Float = image.drawable.intrinsicWidth.toFloat()
                val translate = -width * animator.animatedFraction
                matrix.postTranslate(translate, 0f)

                val scale: Float = image.height / height
                matrix.postScale(scale, scale)
                image.imageMatrix = matrix
            }
        })

        animator.interpolator = interpolator // задаешь зависимости анимации плавность хода
        animator.repeatCount = ValueAnimator.INFINITE // бесконечно
        animator.repeatMode = ValueAnimator.RESTART // заново
        animator.duration = animationDuration // время одной итерации
        animator.start() //start
    }

    fun stopAnimation(){
        animator.cancel()
    }

    companion object {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        fun getBitmap(vectorDrawable: VectorDrawable): Bitmap? {
            val bitmap = Bitmap.createBitmap(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
            vectorDrawable.draw(canvas)
            return bitmap
        }

        fun getBitmap(context: Context, drawableId: Int): Bitmap? {
            val drawable = ContextCompat.getDrawable(context, drawableId)
            return if (drawable is BitmapDrawable) {
                BitmapFactory.decodeResource(context.resources, drawableId)
            } else if (drawable is VectorDrawable) {
                getBitmap(drawable)
            } else {
                throw IllegalArgumentException("unsupported drawable type")
            }
        }
    }
}