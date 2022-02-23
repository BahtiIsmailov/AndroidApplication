package com.example.englishapp.view.main.words.wordsinwordset.wordspresenting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishapp.domain.UserInteractorImpl
import com.example.englishapp.model.data.words.Word
import com.example.englishapp.model.datasource.LetMeSpeakRepository
import com.example.englishapp.model.datasource.RemoteDataSource
import com.example.englishapp.model.datasource.onSuccess
import com.example.englishapp.utils.emit
import com.example.englishapp.view.main.words.wordsinwordset.wordspresenting.adapteritems.BigSpaceItem
import com.example.englishapp.view.main.words.wordsinwordset.wordspresenting.adapteritems.HeaderItem
import com.example.englishapp.view.main.words.wordsinwordset.wordspresenting.adapteritems.WordItem
import com.xwray.groupie.Group
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordsInWordSetViewModel @Inject constructor(
    val remoteDataSource: RemoteDataSource,
    val userInteractor: UserInteractorImpl,
    val repository: LetMeSpeakRepository
) : ViewModel() {

    private val _wordsInWordsSetLiveData = MutableLiveData<WordsInWordSetUiState>()
    val wordsInWordsSetLiveData: LiveData<WordsInWordSetUiState> = _wordsInWordsSetLiveData

    init {
        _wordsInWordsSetLiveData.emit(WordsInWordSetUiState.Loading)
    }

    fun getWordsInWordSet(id: Int) {
        viewModelScope.launch {
            delay(500)
            val user = userInteractor.getUser()

            //val words = remoteDataSource.getWordsByWordSetId("ru", "en", id)
            repository.getWordsByWordSetId("ru", "en", id)
                .onSuccess { words ->
                    user?.let {
                        val imgList =
                            words.words.filter { it.level == user.level.coerceAtMost(3) }
                                .flatMap { it.pictures }
                                .map { it.url } + words
                                .words.filter { it.level != user.level.coerceAtMost(3) }
                                .flatMap { it.pictures }
                                .map { it.url }


                        val wordsGroup = words.words.groupBy { it.level }

                        _wordsInWordsSetLiveData.emit(
                            WordsInWordSetUiState.Data(
                                words,
                                getWordsOrderByUserLevel(user.level, wordsGroup),
                                imgList
                            )
                        )
                    }
                }

        }
    }

    fun getWordsOrderByUserLevel(
        userLevel: Int,
        wordsGroup: Map<Int, List<Word>>
    ): List<Group> {
        return when {
            userLevel <= 1 -> {
                ArrayList<Group>().apply {
                    wordsGroup[1]?.let {
                        add(HeaderItem(1))
                        addAll(it.map { WordItem(it) })
                    }
                    wordsGroup[2]?.let {
                        add(HeaderItem(2))
                        addAll(it.map { WordItem(it) })
                    }
                    wordsGroup[3]?.let {
                        add(HeaderItem(3))
                        addAll(it.map { WordItem(it) })
                    }
                    add(BigSpaceItem())
                }
            }
            userLevel == 2 -> {
                ArrayList<Group>().apply {
                    wordsGroup[2]?.let {
                        add(HeaderItem(2))
                        addAll(it.map { WordItem(it) })
                    }
                    wordsGroup[3]?.let {
                        add(HeaderItem(3))
                        addAll(it.map { WordItem(it) })
                    }
                    wordsGroup[1]?.let {
                        add(HeaderItem(1))
                        addAll(it.map { WordItem(it) })
                    }
                    add(BigSpaceItem())
                }
            }
            userLevel >= 3 -> {
                ArrayList<Group>().apply {
                    wordsGroup[3]?.let {
                        add(HeaderItem(3))
                        addAll(it.map { WordItem(it) })
                    }
                    wordsGroup[2]?.let {
                        add(HeaderItem(2))
                        addAll(it.map { WordItem(it) })
                    }
                    wordsGroup[1]?.let {
                        add(HeaderItem(1))
                        addAll(it.map { WordItem(it) })
                    }
                    add(BigSpaceItem())
                }
            }
            else -> {
                listOf()
            }
        }
    }
}
