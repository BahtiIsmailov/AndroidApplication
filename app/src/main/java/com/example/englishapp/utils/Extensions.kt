package com.example.englishapp.utils

import android.animation.Animator
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.animation.Animation
import android.widget.ImageView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.example.englishapp.R
import com.example.englishapp.view.customview.PushButtonView
import com.example.englishapp.view.main.words.wordsinwordset.training.DrawableAlwaysCrossFadeFactory

fun Number.toPx(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )
}


fun Number.toDp(): Float {
    return (this.toFloat() / (Resources.getSystem().displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
}

fun Animation.setOnAnimationEnd(callback: () -> Unit) {
    setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(p0: Animation?) {
        }

        override fun onAnimationEnd(p0: Animation?) {
            callback()
        }

        override fun onAnimationRepeat(p0: Animation?) {
        }
    })
}

val placeholderBackgroundColorList = listOf(
    "#267963FF",
    "#26FF4A4A",
    "#40FFD339",
    "#2600C6A2",
    "#2600C6A2",
    "#2654C858",
    "#2680B268",
    "#26FF820E",
    "#26B59D21"
)

var placeholderBackgroundColorListIterator =
    placeholderBackgroundColorList.shuffled().listIterator()

fun ImageView.setStatueOfLibertyBackgroundColor() {
    if (!placeholderBackgroundColorListIterator.hasNext()) {
        placeholderBackgroundColorList.shuffled()
        placeholderBackgroundColorListIterator = placeholderBackgroundColorList.listIterator()
    }

    Color.parseColor(placeholderBackgroundColorListIterator.next())
}

fun ImageView.loadImageWithStatueOfLibertyPlaceholder(
    url: String,
    withPlaceholder: Boolean = true,
    onLoaded: (() -> Unit)? = null
) {
    val imageView = this

    if (withPlaceholder) {
        setStatueOfLibertyBackgroundColor()
    }

    val picassoRequest = Glide.with(imageView)
        .load(url)
        .error(R.drawable.image_statue_of_liberty)
        .centerCrop()
        .transition(DrawableTransitionOptions.with(DrawableAlwaysCrossFadeFactory()))
        .addListener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: com.bumptech.glide.request.target.Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
           setStatueOfLibertyBackgroundColor()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: com.bumptech.glide.request.target.Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            imageView.setBackgroundColor(Color.TRANSPARENT)
            onLoaded?.invoke()
            return false
        }

    })

    picassoRequest.into(imageView)
}

fun LottieAnimationView.setOnAnimationEndListener(
    onEndCallback: () -> Unit,

    ) {

    addAnimatorListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {

        }

        override fun onAnimationEnd(animation: Animator?) {
            onEndCallback()
        }

        override fun onAnimationCancel(animation: Animator?) {

        }

        override fun onAnimationRepeat(animation: Animator?) {

        }

    })
}


fun List<SpannableString>.joinToStringForSpannable(separator: String): SpannableStringBuilder {
    val spannableStringBuilder = SpannableStringBuilder()
    for ((index, word) in this.withIndex()) {
        spannableStringBuilder.append(word)
        if (index != this.lastIndex)
            spannableStringBuilder.append(separator)
    }
    return spannableStringBuilder
}

fun <T> List<T>.startsWith(otherList: List<T>): Boolean =
    subList(0, otherList.size) == otherList

fun PushButtonView.setOneTimeClickListener(l: () -> Unit) {
    var isClicked = false
    setOnClickListener {
        if (!isClicked) {
            l.invoke()
            isClicked = true
        }
    }
}

