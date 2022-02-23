package com.example.englishapp.view.main.words.wordsinwordset.training.base

import androidx.lifecycle.*
import com.example.englishapp.model.data.words.Word
import com.example.englishapp.utils.emit
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class TrainingWordTypesBaseViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val wordLiveData = MutableLiveData<Word>()
    val wordVariantsLiveData = MutableLiveData<List<String>>()
    val imageVariantsLiveData = MutableLiveData<List<Pair<String, String>>>()
    val animationTypeLiveData = MutableLiveData<AnimationType?>()

    init {
        val word = savedStateHandle.get<Word>(FragmentTrainingWordTypesBase.WORD)
        word?.let {
            wordLiveData.emit(it)
        }

        val variants = savedStateHandle.get<List<String>>(FragmentTrainingWordTypesBase.VARIANTS)
        variants?.let {
            wordVariantsLiveData.emit(it)
        }

        val imageVariants =
            savedStateHandle.get<List<Pair<String, String>>>(FragmentTrainingWordTypesBase.IMAGES)
        imageVariants?.let {
            imageVariantsLiveData.emit(it)
        }

        val animationType = savedStateHandle.get<AnimationType>(FragmentTrainingWordTypesBase.ANIMATION_TYPE)
        animationTypeLiveData.emit(animationType)
    }
}