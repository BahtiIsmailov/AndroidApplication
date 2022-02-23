package com.example.englishapp.view.main.grammar.grammaritemdescription

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import com.example.englishapp.R
import com.example.englishapp.model.data.grammar.Content
import com.example.englishapp.model.data.grammar.SpanItem
import com.google.gson.internal.LinkedTreeMap

fun getStyledTextFromList(
    items: List<Any>?,
    context: Context,
    getOriginText: Boolean = true
): SpannableStringBuilder {
    val resultText = SpannableStringBuilder()
    if (items == null || items.isEmpty())
        return resultText

    for (content in items) {
        when (content) {
            is Content -> {
                val result = getStyledTextFromContent(content, context, false)
                resultText.append(result)
            }
            is LinkedTreeMap<*, *> -> {
                try {
                    val linkedTreeMap =  content as LinkedTreeMap<String,String>
                    val spanItem = linkedTreeMapToSpanItem(linkedTreeMap)
                    resultText.append(getStyledTextFromSpanItem(spanItem, context))
                }
                catch (e:java.lang.Exception){

                }
            }
            else -> {
                throw Exception("этот тип не поддерживает обработчиком джейсона")
            }
        }
    }
    return resultText
}

fun getStyledTextFromContent(
    content: Content,
    context: Context,
    getOriginText: Boolean
): SpannableStringBuilder {
    val resultText = SpannableStringBuilder()
    val items: List<SpanItem> = if (getOriginText){
        listLinkedTreeMapToListSpanItem(content.text as List<LinkedTreeMap<String,String>>)
    }
    else {
        listLinkedTreeMapToListSpanItem(content.value as List<LinkedTreeMap<String,String>>)
    }

    for (element in items)
        resultText.append(getStyledTextFromSpanItem(element, context))

    return resultText
}

fun getStyledTextFromSpanItem(
    element: SpanItem,
    context: Context,
): SpannableStringBuilder {
    val textValue = element.text ?: ""
    val resultText = SpannableStringBuilder(textValue)
    if (element.color != null) {
        val color = when (element.color) {
            "base" -> null
            "purple" -> R.color.purple
            "black" -> R.color.black
            "blue" -> R.color.blue
            "green" -> R.color.green
            else -> R.color.black
        }
        val currentTheme = context.theme
        if (color != null) {
            val foregroundSpan = ForegroundColorSpan(
                context.resources.getColor(
                    color,
                    currentTheme
                )
            )
            resultText.setSpan(
                foregroundSpan,
                0,
                resultText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    if (element.font_weight != null) {
        if (element.font_weight == "bold") {
            resultText.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                resultText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    if (element.font_style != null) {
        if (element.font_style == "italic") {
            resultText.setSpan(
                StyleSpan(Typeface.ITALIC),
                0,
                resultText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    if (element.font_decorations != null) {
        if (element.font_decorations == "underline") {
            resultText.setSpan(
                UnderlineSpan(),
                0,
                resultText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    return resultText
}

fun listLinkedTreeMapToListSpanItem(listTreeMap: List<LinkedTreeMap<String, String>>): List<SpanItem> {
    return listTreeMap.map {
        linkedTreeMapToSpanItem(it)
    }
}

fun linkedTreeMapToSpanItem(linkedTreeMap: LinkedTreeMap<String, String>): SpanItem {
    val textInfoInText = SpanItem()
    for (item in linkedTreeMap) {
        when (item.key) {
            "color" -> {
                textInfoInText.color = item.value
            }
            "font_weight" -> {
                textInfoInText.font_weight = item.value
            }
            "text" -> {
                textInfoInText.text = item.value
            }
            "font_decorations" -> {
                textInfoInText.font_decorations = item.value
            }
            "font_style" -> {
                textInfoInText.font_style = item.value
            }
        }
    }
    return textInfoInText
}

fun getContentFromList(contents: List<Any>): List<Content> {
    val contentList: List<Content> = contents.map {
        var text: List<Any>? = null
        var value: List<Any>? = null

        if (it is Map<*, *>) {
            it.keys.forEach {  key ->
                if ((key as? String).equals("text")) {
                    text = it["text"] as? List<Any>
                }
                if ((key as? String).equals("value")) {
                    value = it["value"] as? List<Any>
                }
            }
        }
        Content(value = value, text = text)
    }
    return contentList
}
