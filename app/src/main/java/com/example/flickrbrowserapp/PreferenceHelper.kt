package com.example.flickrbrowserapp

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PreferenceHelper {
        private lateinit var sharedPreferences: SharedPreferences

        private const val PREFS_KEY = "com.example.flickrbrowserapp.PREFERENCE_FILE_KEY"
        const val PHOTOS_LIST ="photos"
        const val LAST_PHOTOS_LIST ="last"

    fun init(context: Context) {
            sharedPreferences =  context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        }

        //this function is to get the shared preference list
        fun getItemList(key: String): ArrayList<Photo> {
            val emptyList = Gson().toJson(ArrayList<Photo>())
            return Gson().fromJson(sharedPreferences.getString(key, emptyList), object :
                TypeToken<ArrayList<Photo>>() {
            }.type)
        }

        //this function is to set the shared preference recycler view list
        fun setItemList(key: String, ArrayList: ArrayList<Photo>) {
            val arrayString = Gson().toJson(ArrayList)
            sharedPreferences.edit().putString(key, arrayString).apply()
        }



}