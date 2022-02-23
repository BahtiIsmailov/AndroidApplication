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
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class LetMeSpeakRepositoryImpl @Inject constructor(
    private val remote: RemoteDataSource,
    @IoDispatcher private val dispatcherIO: CoroutineDispatcher
) : LetMeSpeakRepository {

    override suspend fun getDetailsProfile(token: String): ResultWrapper<ProfileDetailsResponse> =
        safeApiCall(dispatcherIO) { remote.getDetailsProfile(token) }

    override suspend fun getWordsGroups(
        originLanguage: String,
        translationLanguage: String
    ): ResultWrapper<List<WordGroup>> = safeApiCall(dispatcherIO) {
        remote.getWordsGroups(
            originLanguage,
            translationLanguage
        ).filter { it.wordsets != null }
    }

    override suspend fun setCompleteAllWords(words: List<Word>): ResultWrapper<Unit> =
        safeApiCall(dispatcherIO) { remote.setCompleteAllWords(words) }

    override suspend fun setStartWords(words: List<Word>): ResultWrapper<Unit> =
        safeApiCall(dispatcherIO) { remote.setStartWords(words) }

    override suspend fun getWordsByWordSetId(
        originLanguage: String,
        translationLanguage: String,
        wordsetId: Int,
        fromCache: Boolean
    ): ResultWrapper<WordsResponse> = safeApiCall(dispatcherIO) {
        remote.getWordsByWordSetId(
            originLanguage,
            translationLanguage,
            wordsetId
        )
    }

    override suspend fun getToken(
        auth_token: String,
        xcToken: String
    ): ResultWrapper<TokenResponse> =
        safeApiCall(dispatcherIO) { remote.getToken(auth_token, xcToken) }

    override suspend fun getProfiles(
        login: String,
        pass: String
    ): ResultWrapper<List<ProfileResponse>> =
        safeApiCall(dispatcherIO) { remote.getProfiles(login, pass) }

    override suspend fun getProfiles(xcToken: String): ResultWrapper<List<ProfileResponse>> =
        safeApiCall(dispatcherIO) { remote.getProfiles(xcToken) }

    override suspend fun getEndTrainingInfo(
        wordsIds: String,
        total: String,
        error: String
    ): ResultWrapper<EndTrainingWordSetResponse> =
        safeApiCall(dispatcherIO) { remote.getEndTrainingInfo(wordsIds, total, error) }

    override suspend fun sendQuantum(quantum: Quantum): ResultWrapper<QuantumResponse> =
        safeApiCall(dispatcherIO) { remote.sendQuantum(quantum) }

    override suspend fun getGrammarInfo(
        originLanguage: String,
        translationLanguage: String
    ): ResultWrapper<List<GrammarMainData>> =
        safeApiCall(dispatcherIO) { remote.getGrammarInfo(originLanguage, translationLanguage) }

    override suspend fun getGrammarItemInfo(url: String): ResultWrapper<GrammarDataItemInfo> =
        safeApiCall(dispatcherIO) { remote.getGrammarItemInfo(url) }

    override suspend fun getTrainingInfo(
        originLanguage: String,
        translationLanguage: String,
        id: String
    ): ResultWrapper<GrammarTrainingInfo> =
        safeApiCall(dispatcherIO) {
            remote.getTrainingInfo(
                originLanguage,
                translationLanguage,
                id
            )
        }

    override suspend fun endGrammarTraining(
        game_id: String,
        id: String,
        result: String,
        value: String
    ): ResultWrapper<EndGrammarData> =
        safeApiCall(dispatcherIO) { remote.endGrammarTraining(game_id, id, result, value) }

    override suspend fun getPersonStatistic(id: Int): ResultWrapper<GetStatisticFromOtherPerson> =
        safeApiCall(dispatcherIO) { remote.getPersonStatistic(id) }

    override suspend fun createAuthPerson(
        login: String,
        pass: String
    ): ResultWrapper<CreateAuthProfile> =
        safeApiCall(dispatcherIO) { remote.createAuthPerson(login, pass) }

    override suspend fun getProfilesAfterCreatedAuthPerson(ctoken: String): ResultWrapper<Unit> =
        safeApiCall(dispatcherIO) { remote.getProfilesAfterCreatedAuthPerson(ctoken) }

    override suspend fun getDataForTrainingInWelcome(
        teamsId: String,
        originLanguage: String,
        translationLanguage: String,
    ): ResultWrapper<List<DataForTrainingWelcome>> = safeApiCall(dispatcherIO) {
        remote.getDataForTrainingInWelcome(
            teamsId,
            originLanguage,
            translationLanguage
        )
    }


}