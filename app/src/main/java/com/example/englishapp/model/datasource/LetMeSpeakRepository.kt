package com.example.englishapp.model.datasource

import com.example.englishapp.domain.quantum.Quantum
import com.example.englishapp.model.data.ProfileResponse
import com.example.englishapp.model.data.QuantumResponse
import com.example.englishapp.model.data.TokenResponse
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

interface LetMeSpeakRepository {

    suspend fun getDetailsProfile(token: String): ResultWrapper<ProfileDetailsResponse>

    suspend fun getWordsGroups(
        originLanguage: String,
        translationLanguage: String
    ): ResultWrapper<List<WordGroup>>

    suspend fun setCompleteAllWords(words: List<Word>): ResultWrapper<Unit>

    suspend fun setStartWords(words: List<Word>): ResultWrapper<Unit>

    suspend fun getWordsByWordSetId(
        originLanguage: String,
        translationLanguage: String,
        wordsetId: Int,
        fromCache: Boolean = true
    ): ResultWrapper<WordsResponse>

    suspend fun getToken(auth_token: String, xcToken: String): ResultWrapper<TokenResponse>

    suspend fun getProfiles(login: String, pass: String): ResultWrapper<List<ProfileResponse>>

    suspend fun getProfiles(xcToken: String): ResultWrapper<List<ProfileResponse>>

    suspend fun getEndTrainingInfo(
        wordsIds: String,
        total: String,
        error: String
    ): ResultWrapper<EndTrainingWordSetResponse>

    suspend fun sendQuantum(quantum: Quantum): ResultWrapper<QuantumResponse>

    suspend fun getGrammarInfo(
        originLanguage: String,
        translationLanguage: String
    ): ResultWrapper<List<GrammarMainData>>

    suspend fun getGrammarItemInfo(url: String): ResultWrapper<GrammarDataItemInfo>

    suspend fun getTrainingInfo(
        originLanguage: String,
        translationLanguage: String,
        id: String
    ): ResultWrapper<GrammarTrainingInfo>

    suspend fun endGrammarTraining(
        game_id: String,
        id: String,
        result: String,
        value: String
    ): ResultWrapper<EndGrammarData>

    suspend fun getPersonStatistic(id: Int): ResultWrapper<GetStatisticFromOtherPerson>

    suspend fun createAuthPerson(login: String, pass: String): ResultWrapper<CreateAuthProfile>

    suspend fun getProfilesAfterCreatedAuthPerson(ctoken: String): ResultWrapper<Unit>

    suspend fun getDataForTrainingInWelcome(
        teamsId: String,
        originLanguage: String,
        translationLanguage: String
    ): ResultWrapper<List<DataForTrainingWelcome>>

}