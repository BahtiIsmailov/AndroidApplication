package com.example.englishapp.model.datasource

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
import com.example.englishapp.model.data.words.WordGroup
import com.example.englishapp.model.data.words.WordsResponse
import okhttp3.RequestBody
import retrofit2.http.*

interface LetMeSpeakService {

    @POST("/user/auth")
    suspend fun getProfiles(
        @Body json: RequestBody,
        @Header("X-Device-Token-Letmespeak") deviceToken: String
    ): List<ProfileResponse>

    @POST("/user/auth")
    suspend fun getProfiles(
        @Header("X-Device-Token-Letmespeak") deviceToken: String,
        @Header("X-C-Token-Letmespeak") XCheader: String
    ): List<ProfileResponse>


    @POST("/api/1.0/auth")
    suspend fun getToken(
        @Body json: RequestBody,
        @Header("X-Device-Token-Letmespeak") XDToken: String,
        @Header("X-C-Token-Letmespeak") XCheader: String
    ): TokenResponse


    @GET("api/1.0/profile")
    suspend fun getProfileDetail(
        @Header("X-Auth-Token-Letmespeak") xAuthTokenLetmespeak: String,
        @Header("X-Device-Token-Letmespeak") XDToken: String
    ): ProfileDetailsResponse

    @POST("/lms/wordset/v2")
    suspend fun getAllWordsGroups(
        @Body json: RequestBody,
        @Header("X-Auth-Token-Letmespeak") xAuthTokenLetmespeak: String,
        @Header("X-Device-Token-Letmespeak") deviceToken: String
    ): List<WordGroup>

    @POST("/lms/wordset/v2/get/{wordsetId}")
    suspend fun getWordsByWordSetId(
        @Path("wordsetId") wordsetId: Int,
        @Body json: RequestBody,
        @Header("X-Auth-Token-Letmespeak") xAuthTokenLetmespeak: String,
        @Header("X-Device-Token-Letmespeak") deviceToken: String
    ): WordsResponse

    @POST("/lms/wordset/v2/complete/{wordsId}")
    suspend fun setCompleteAllWords(
        @Path("wordsId") wordsId: String,
        @Header("X-Auth-Token-Letmespeak") xAuthTokenLetmespeak: String,
        @Header("X-Device-Token-Letmespeak") deviceToken: String
    )

    @POST("/lms/wordset/v2/startWords/{wordsId}")
    suspend fun setStartWords(
        @Path("wordsId") wordsId: String,
        @Header("X-Auth-Token-Letmespeak") xAuthTokenLetmespeak: String,
        @Header("X-Device-Token-Letmespeak") deviceToken: String
    )

    @POST("/api/1.0/training/finish/wordset")
    suspend fun getEndTrainingInfo(
        @Body json: RequestBody,
        @Header("X-Auth-Token-Letmespeak") xAuthTokenLetmespeak: String,
        @Header("X-Device-Token-Letmespeak") deviceToken: String
    ): EndTrainingWordSetResponse

    @POST("api/1.0/training/write")
    suspend fun sendQuantum(
        @Body json: RequestBody,
        @Header("X-Auth-Token-Letmespeak") xAuthTokenLetmespeak: String,
        @Header("X-Device-Token-Letmespeak") deviceToken: String
    ): QuantumResponse

    @POST("/lms/grammar/v2/available")
    suspend fun getGrammarInfo(
        @Body json: RequestBody,
        @Header("X-Auth-Token-Letmespeak") xAuthTokenLetmespeak: String,
        @Header("X-Device-Token-Letmespeak") deviceToken: String
    ): List<GrammarMainData>


    @GET
    suspend fun getGrammarItemInfo(@Url url: String): GrammarDataItemInfo


    @POST("/lms/trainings/v2/byId/{id}?leave_braces=1")
    suspend fun getTrainingInfo(
        @Path("id") grammarId: String,
        @Body json: RequestBody,
        @Header("X-Auth-Token-Letmespeak") xAuthTokenLetmespeak: String,
        @Header("X-Device-Token-Letmespeak") deviceToken: String
    ): GrammarTrainingInfo


    @POST("/api/1.0/training/finish/grammar")
    suspend fun endGrammarTraining(
        @Body json: RequestBody,
        @Header("X-Auth-Token-Letmespeak") xAuthTokenLetmespeak: String,
        @Header("X-Device-Token-Letmespeak") deviceToken: String
    ): EndGrammarData

    @GET("/lms/leaderboard/{id}")
    suspend fun getPersonStatistic(
        @Path("id") id: Int,
        @Header("X-Auth-Token-Letmespeak") xAuthTokenLetmespeak: String,
        @Header("X-Device-Token-Letmespeak") deviceToken: String
    ): GetStatisticFromOtherPerson

    @POST("/api/1.0/auth/create")
    suspend fun createAuthPerson(
        @Body json: RequestBody,
        @Header("X-Device-Token-Letmespeak") deviceToken: String
    ): CreateAuthProfile

    @POST("/user/v2/auth")
    suspend fun getProfilesAfterCreatedAuthPerson(
        @Header("X-C-Token-Letmespeak") XCheader: String,
        @Header("X-Device-Token-Letmespeak") deviceToken: String
    )

    @GET("/lms/languages")
    suspend fun getLanguages(): List<ChooseLanguage>


    @POST("/lms/profile/create")
    suspend fun getTokenForWelcome(
        @Body json: RequestBody,
        @Header("X-Device-Token-Letmespeak") XDToken: String,
        @Header("X-C-Token-Letmespeak") XCheader: String
    ): TokenResponse

    @POST("/lms/profile/update")
    suspend fun updateProfileData(
        @Body json: RequestBody,
        @Header("X-Device-Token-Letmespeak") XDToken: String,
        @Header("X-C-Token-Letmespeak") XCheader: String,
        @Header("X-Auth-Token-Letmespeak") xAuthTokenLetmespeak: String
    )

    @POST("/lms/wordset/v2/lextest/{teamsId}")
    suspend fun getDataForTrainingInWelcome(
        @Path("teamsId") teamsId: String,
        @Body json: RequestBody,
        @Header("X-Device-Token-Letmespeak") XDToken: String,
        @Header("X-C-Token-Letmespeak") XCheader: String,
        @Header("X-Auth-Token-Letmespeak") xAuthTokenLetmespeak: String
    ):List<DataForTrainingWelcome>


}