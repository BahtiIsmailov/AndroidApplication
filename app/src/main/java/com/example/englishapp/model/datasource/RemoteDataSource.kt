package com.example.englishapp.model.datasource

import android.util.Log
import com.example.englishapp.domain.quantum.Quantum
import com.example.englishapp.model.data.ProfileResponse
import com.example.englishapp.model.data.QuantumResponse
import com.example.englishapp.model.data.TokenResponse
import com.example.englishapp.model.data.createauthprofile.ChooseLanguage
import com.example.englishapp.model.data.createauthprofile.CreateAuthProfile
import com.example.englishapp.model.data.createauthprofile.DataForTrainingWelcome
import com.example.englishapp.model.data.grammar.GrammarDataItemInfo
import com.example.englishapp.model.data.grammar.GrammarMainData
import com.example.englishapp.model.data.grammartraining.GrammarTrainingInfo
import com.example.englishapp.model.data.grammartraining.endgrammartraining.EndGrammarData
import com.example.englishapp.model.data.grammartraining.endgrammartraining.GetStatisticFromOtherPerson
import com.example.englishapp.model.data.profiledetailsresponse.ProfileDetailsResponse
import com.example.englishapp.model.data.training.EndTrainingWordSetResponse
import com.example.englishapp.model.data.words.Word
import com.example.englishapp.model.data.words.WordGroup
import com.example.englishapp.model.data.words.WordsResponse
import com.example.englishapp.utils.TokenHelper
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


class RemoteDataSource @Inject constructor(
    private val deviceToken: String,
    val tokenHelper: TokenHelper
) {

    interface Listener {
        fun onAuthorizedError()
    }

    class NoXAuthTokenLMSException : Exception()

    var cachesWord: MutableMap<Int, WordsResponse> = mutableMapOf()
    var needRefreshCashesGrammar: Boolean = false
    var cashesGrammar: List<GrammarMainData>? = null
    var cashesLanguages: List<ChooseLanguage>? = null

    private var _authToken: String? = null
    private val authToken: String
        get() = if (_authToken != null)
            _authToken ?: throw NoXAuthTokenLMSException()
        else {
            _authToken = tokenHelper.readxAuthTokenLMS()
            _authToken ?: throw NoXAuthTokenLMSException()
        }

    fun clearWordsCache() {
        cachesWord.clear()
    }

    var listener: Listener? = null
    fun setOnAuthorizedErrorListener(listener: Listener) {
        this.listener = listener
    }

    val client = OkHttpClient()
        .newBuilder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor(TokenInterceptor(tokenHelper))
        .build()

    private var retrofit = Retrofit.Builder()
        //.baseUrl("https://api2-dev.letmespeak.pro")
        .baseUrl("https://api2.letmespeak.pro") // боевой
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var service = retrofit.create(LetMeSpeakService::class.java)


    suspend fun getDetailsProfile(token: String): ProfileDetailsResponse {
        return service.getProfileDetail(token, deviceToken)
    }

    suspend fun getWordsGroups(
        originLanguage: String,
        translationLanguage: String
    ): List<WordGroup> {
        val json = """{"originalLanguageCode":"ru","translationLanguageCode":"en"}"""
        val body = RequestBody.create("text/plain".toMediaTypeOrNull(), json)
        return service.getAllWordsGroups(body, authToken, deviceToken)
    }

    suspend fun setCompleteAllWords(words: List<Word>) {
        val wordsId = words.joinToString(",") { it.word }
        service.setCompleteAllWords(wordsId, authToken, deviceToken)
    }

    suspend fun setStartWords(words: List<Word>) {
        val wordsId = words.joinToString(",") { it.word }
        service.setStartWords(wordsId, authToken, deviceToken)
    }

    suspend fun getWordsByWordSetId(
        originLanguage: String,
        translationLanguage: String,
        wordsetId: Int,
        fromCache: Boolean = true
    ): WordsResponse {
        val words = cachesWord[wordsetId]
        if (words != null && fromCache) {
            return words
        }
        val json =
            """{"originalLanguageCode":"$originLanguage","translationLanguageCode":"$translationLanguage"}"""
        val body = RequestBody.create("text/plain".toMediaTypeOrNull(), json)
        val result = service.getWordsByWordSetId(wordsetId, body, authToken, deviceToken)
        cachesWord[wordsetId] = result
        return result
    }

    suspend fun getToken(auth_token: String, xcToken: String): TokenResponse {
        val json = """{"token":"$deviceToken","auth_token":"$auth_token"}"""
        val body = RequestBody.create("text/plain".toMediaTypeOrNull(), json)
        return service.getToken(body, deviceToken, xcToken)
    }

    suspend fun getProfiles(login: String, pass: String): List<ProfileResponse> {
        val json = """{"login": "dev_mobile@test.com", "password":"test12345"}"""
        //val json = """{"login": "kd@letmespeak.org", "password":"Verysimple1"}"""
        //val json = """{"login": "$login", "password":"$pass"}"""
        val body = RequestBody.create("application/json".toMediaTypeOrNull(), json)

        return service.getProfiles(body, deviceToken)
    }

    suspend fun getProfiles(xcToken: String): List<ProfileResponse> {
        return service.getProfiles(deviceToken, xcToken)
    }

    suspend fun getEndTrainingInfo(
        wordsIds: String,
        total: String,
        error: String
    ): EndTrainingWordSetResponse {
        val json = """{"wordIDs": "$wordsIds", "total": "$total","errors": "$error"}"""
        val body = RequestBody.create("application/json".toMediaTypeOrNull(), json)
        return service.getEndTrainingInfo(body, authToken, deviceToken)
    }

    suspend fun sendQuantum(quantum: Quantum): QuantumResponse {
        val json: String = Gson().toJson(quantum)
        val body = RequestBody.create("application/json".toMediaTypeOrNull(), json)
        return service.sendQuantum(body, authToken, deviceToken)
    }

    suspend fun getGrammarInfo(
        originLanguage: String,
        translationLanguage: String
    ): List<GrammarMainData> {
        if (!needRefreshCashesGrammar)
            cashesGrammar?.let {
                return it
            }
        val json = """{"originalLanguageCode":"en","translationLanguageCode":"ru"}"""
        val body = RequestBody.create("application/json".toMediaTypeOrNull(), json)
        val result = service.getGrammarInfo(body, authToken, deviceToken)
        cashesGrammar = result
        return result
    }

    suspend fun getGrammarItemInfo(url: String): GrammarDataItemInfo {
        return service.getGrammarItemInfo(url)
    }

    suspend fun getTrainingInfo(
        originLanguage: String,
        translationLanguage: String,
        id: String
    ): GrammarTrainingInfo {
        val json =
            """{"originalLanguageCode": "en","translationLanguageCode": "ru","languageLevel": "level_2"}"""
        val body = RequestBody.create("application/json".toMediaTypeOrNull(), json)
        return service.getTrainingInfo(id, body, authToken, deviceToken)
    }

    suspend fun endGrammarTraining(
        game_id: String,
        id: String,
        result: String,
        value: String
    ): EndGrammarData {
        val json = """{"game_id":"$game_id","id": "$id", "result": "$result","value": "$value"}"""
        val body = RequestBody.create("application/json".toMediaTypeOrNull(), json)
        val apiResponse = service.endGrammarTraining(body, authToken, deviceToken)
        Log.d("apiResponse", apiResponse.toString())
        return apiResponse
    }

    suspend fun getPersonStatistic(id: Int): GetStatisticFromOtherPerson {
        return service.getPersonStatistic(id, authToken, deviceToken)
    }

    suspend fun createAuthPerson(login: String, pass: String): CreateAuthProfile {
        val json = """{"login": "$login", "password":"$pass"}"""
        val body = RequestBody.create("application/json".toMediaTypeOrNull(), json)
        val response = service.createAuthPerson(body, deviceToken)
        Log.d("responseForTest", response.toString())
        return response
    }

    suspend fun getProfilesAfterCreatedAuthPerson(ctoken: String) {
        return service.getProfilesAfterCreatedAuthPerson(ctoken, deviceToken)
    }

    suspend fun getLanguages(): List<ChooseLanguage> {
        if (cashesLanguages != null)
            cashesLanguages
        val result = service.getLanguages()
        cashesLanguages = result
        return result
    }

    suspend fun getTokenForWelcome(): TokenResponse {
        val json = """{"device_id": "$deviceToken"}"""
        val body = json.toRequestBody("application/json".toMediaTypeOrNull())
        return service.getTokenForWelcome(body, deviceToken, tokenHelper.readxcTokenLMS()!!)
    }

    suspend fun updateProfileData(nickName: String, originLanguage: String, avatar: String) {
        val json =
            """{"nickname": "$nickName", "originLanguage":"$originLanguage","avatar":"$avatar"}"""
        val body = json.toRequestBody("application/json".toMediaTypeOrNull())
        return service.updateProfileData(
            body,
            deviceToken,
            tokenHelper.readxcTokenLMS()!!,
            authToken
        )

    }

    suspend fun getDataForTrainingInWelcome(
        teamsId: String,
        originLanguage: String,
        translationLanguage: String
    ): List<DataForTrainingWelcome> {
        val json =
            """{"originalLanguageCode":"$originLanguage","translationLanguageCode":"$translationLanguage"}"""
        val body = json.toRequestBody("application/json".toMediaTypeOrNull())
        return service.getDataForTrainingInWelcome(
            teamsId,
            body,
            deviceToken,
            tokenHelper.readxcTokenLMS()!!,
            authToken
        )
    }
}
