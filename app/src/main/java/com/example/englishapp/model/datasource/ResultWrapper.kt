package com.example.englishapp.model.datasource

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class GenericError(val code: Int? = null, val error: ErrorResponse? = null) :
        ResultWrapper<Nothing>()

    object NetworkError : ResultWrapper<Nothing>()
}

val <T> ResultWrapper<T>.value get() = if (this is ResultWrapper.Success) value else null

fun <T> ResultWrapper<T>.onSuccess(l: (T) -> Unit): ResultWrapper<T> {
    if (this is ResultWrapper.Success) {
        l.invoke(this.value)
    }
    return this
}

fun <T> ResultWrapper<T>.onError(l: (ResultWrapper.GenericError) -> Unit): ResultWrapper<T> {
    when (this) {
        is ResultWrapper.GenericError -> {
            l.invoke(this)
        }
    }
    return this
}


