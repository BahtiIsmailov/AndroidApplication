package com.example.englishapp.view.welcomefragment1.login.authsteps.welcome

import android.util.Log
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.englishapp.utils.InformationForWelcome
import com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.chooselanguage.ChooseLanguageFragment
import com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.chooseyourstyle.ChooseYourStyleFragment
import com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.inputnickname.InputNickNameFragment
import com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.motivation.MotivationFragment
import com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.startcheckvocabulary.StartCheckVocabularyFragment
import com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.startcheckvocabulary.trainingwordinwelcome.TrainingWordInWelcomeFragment
import com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.startspeaknow.StartSpeakNowFragment
import com.example.englishapp.view.welcomefragment1.login.authsteps.welcome.teams.TeamsFragment
import kotlinx.coroutines.flow.MutableStateFlow

class BaseWelcomeFragmentViewModel : ViewModel() {

    val containers = mutableListOf<Fragment>(
        ChooseLanguageFragment(),
        MotivationFragment(),
        TeamsFragment(),
        ChooseYourStyleFragment(),
        InputNickNameFragment(),
        StartSpeakNowFragment(),
        StartCheckVocabularyFragment(),
        TrainingWordInWelcomeFragment()
    )

    private val welcomeDataForServer = mutableMapOf<String,String>()

    private val _currentIndexFragmentLiveData = MutableStateFlow(0)
    val currentIndexFragmentLiveData: LiveData<Int> = _currentIndexFragmentLiveData.asLiveData()


    fun upCurrentIndexFragmentContainers() {
        _currentIndexFragmentLiveData.value++
    }

    fun downCurrentIndexFragmentContainers() {
        _currentIndexFragmentLiveData.value--
    }

    fun getCurrentIndexFragmentContainers():Int{
        return _currentIndexFragmentLiveData.value
    }
    fun setDataForMapWelcomeDataForServer(key:String,value:String){
         welcomeDataForServer[key] = value
        Log.d("welcomeDataForServer", welcomeDataForServer.toString())
    }
    fun setFewValueForMapWelcomeDataForServer(key: String, value: String){
        if (!welcomeDataForServer.containsKey(key)){
            welcomeDataForServer[key] = value
        }else {
            for (i in welcomeDataForServer.keys) {
                if (i == key) {
                   welcomeDataForServer[i] += ",$value"
                }
            }
        }
        Log.d("welcomeDataForServer",welcomeDataForServer.toString())
    }
    fun getValueFromMapWelcomeDataForServer(key:String) : String{
        return welcomeDataForServer[key]?:"null"
    }

    fun getImageFromChooseYourStyle(value: String,view: ImageView) {
        for (avatar in InformationForWelcome.getAvatar()) {
            if (value == avatar.nameAvatar) {
                view.setImageResource(avatar.images)
            }
        }
    }

    fun workWithUniqueValueTeams(): String  {
        val exampleSet = mutableSetOf<Char>()
        val valueIfSizeLessFour = "123456789"
        val value = welcomeDataForServer["teams"].toString().replace(",", "").replace("[", "").replace("]", "")
        var newValue = ""
        if (value.length > 4) {
            newValue = value.substring(0,4)
        }

        for (i in newValue) {
            exampleSet.add(i)
        }
        if (exampleSet.size < 4) {
            for (i in 0..100) {
                if (exampleSet.size == 4)
                    break
                else
                    exampleSet.add(valueIfSizeLessFour.random())
            }
        }
        return exampleSet.toString().replace("[","").replace("]","")
    }


}