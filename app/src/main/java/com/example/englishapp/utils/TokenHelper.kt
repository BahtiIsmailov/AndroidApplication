package com.example.englishapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.util.Log
import javax.inject.Inject


class TokenHelper @Inject constructor(val context: Context) {
    companion object {
        const val SHARED_PREF_FILE_NAME = "main"
        const val AUTH_TOKEN = "auth_token"
        const val DEVICE_TOKEN_KEY = "gf8wfgwfh7weaf4r"
        const val XC_TOKEN = "xc_token"
        const val PROFILE_TOKEN = "profile_token"
    }

    infix fun String.xor(that: String) = mapIndexed { index, originalChar ->
        val newChar = that[index].code.xor(originalChar.code).toChar()
        if (newChar in (('a'..'z') + ('A'..'Z') + ('0'..'9')))
            newChar
        else
            originalChar
    }.joinToString(separator = "") {
        it.toString()
    }

    @SuppressLint("HardwareIds")
    fun generateDeviceToken(): String {
        val androidId = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )

        val TestToken = androidId xor DEVICE_TOKEN_KEY
        return TestToken
    }


    fun readxAuthTokenLMS(): String? {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(AUTH_TOKEN, null)
    }
    fun savexAuthTokenLMS(authToken: String) {
        val sharedPreferences =
            context.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(AUTH_TOKEN, authToken).apply()
    }
    fun readxcTokenLMS(): String? {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(XC_TOKEN, null)
    }
    fun savexcTokenLMS(xcToken: String) {
        val sharedPreferences =
            context.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(XC_TOKEN, xcToken).apply()
    }

    fun readProfileToken(): String? {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(PROFILE_TOKEN, null)
    }
    fun saveProfileToken(token: String) {
        val sharedPreferences =
            context.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(PROFILE_TOKEN, token).apply()
    }
}