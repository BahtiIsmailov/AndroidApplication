package com.example.englishapp.model.datasource

import android.content.Context
import android.os.Process
import android.util.Log
import com.example.englishapp.model.data.TokenResponse
import com.example.englishapp.utils.TokenHelper
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


internal class TokenInterceptor(val tokenHelper: TokenHelper) : Interceptor {
    private var isRefreshing = false

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val response: Response = chain.proceed(original)
        if (response.code == 401 && !isRefreshing) {
            synchronized(this) {
                Log.d("TokenInterceptor", "Token need update ${tokenHelper.readProfileToken()}")

                response.close()
                isRefreshing = true
                updateToken(original, chain)
                isRefreshing = false

                if (tokenHelper.readxAuthTokenLMS() != null) {
                    val builder = original.newBuilder()

                    builder.removeHeader(deviceTokenHeader)
                    builder.removeHeader(authTokenHeader)

                    builder.header(deviceTokenHeader, tokenHelper.generateDeviceToken())
                    builder.header(authTokenHeader, tokenHelper.readxAuthTokenLMS() ?: "")
                    val request = builder.build()
                    return chain.proceed(request)
                }
            }
        }
        return response
    }

    private fun updateToken(request: Request, chain: Interceptor.Chain) {
        val json = """{"token":"${tokenHelper.generateDeviceToken()}","auth_token":"${tokenHelper.readProfileToken() ?: ""}"}"""
        val body = json.toRequestBody("application/json".toMediaTypeOrNull())
        val builder = request.newBuilder()

        builder.url("https://${request.url.host}/api/1.0/auth")
        builder.method("POST", body)
        builder.addHeader(authXCTokenHeader, tokenHelper.readxcTokenLMS() ?: "")

        val authRequest = builder.build()
        val authResponse = chain.proceed(authRequest)


        try {
            if (authResponse.isSuccessful) {
                val bytes = authResponse.body?.byteStream()?.readBytes()
                if (bytes != null) {
                    val tokenResponse = Gson().fromJson(
                        String(bytes),
                        TokenResponse::class.java
                    )
                    tokenHelper.savexAuthTokenLMS(tokenResponse.token)
                    Log.d(
                        "TokenInterceptor",
                        "Token updated! New token: " + tokenHelper.readxAuthTokenLMS()
                    )
                }
            }

        } catch (e: InterruptedException) {
            e.printStackTrace()
            authResponse.close()
        }
        authResponse.close()
    }
}

private const val deviceTokenHeader = "X-Device-Token-Letmespeak"
private const val authTokenHeader = "X-Auth-Token-Letmespeak"
private const val authXCTokenHeader = "X-C-Token-Letmespeak"