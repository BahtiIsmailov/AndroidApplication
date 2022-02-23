package com.example.englishapp.utils

import android.content.Context
import com.example.englishapp.model.data.profiledetailsresponse.ProfileDetailsResponse
import com.google.gson.Gson
import java.lang.Exception
import javax.inject.Inject

class ProfileRepositoryLocal @Inject constructor(val context: Context) {
    companion object {
        const val SHARED_PREF_FILE_NAME = "profile"
        const val PROFILE_DETAILS_KEY = "profile_details"
    }

    fun getProfile(): ProfileDetailsResponse? {
        val sharedPreferences =
            context.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(PROFILE_DETAILS_KEY,null)
        return if (json == null){
            null
        }
        else {
            try {
                val profile = Gson().fromJson(json, ProfileDetailsResponse::class.java)
                profile
            } catch (e:Exception){
                null
            }

        }
    }

    fun saveProfile(profile: ProfileDetailsResponse) {
        val sharedPreferences =
            context.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE)
        val json =  Gson().toJson(profile)
        sharedPreferences.edit().putString(PROFILE_DETAILS_KEY,json ).apply()
    }

    fun clearProfile() {
        val sharedPreferences =
            context.getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE)
       sharedPreferences.edit().clear().apply()
    }
}