package com.example.newsapp.utility

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

object SharedPreferences{

    private val PREFS_NAME = "SharedPreferences"

    fun StoreStringSharedPref(key: String?, value: String?, context: Context) {
        val sharedPreferences = context.applicationContext.getSharedPreferences(
            PREFS_NAME, Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getStringSharedPref(key: String?, context: Context): String? {
        val sharedPreferences = context.applicationContext.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getString(key, null)
    }

    fun storeBooleanSharedPref(key: String?, value: Boolean?, context: Context) {
        val sharedPreferences = context.applicationContext.getSharedPreferences(
            PREFS_NAME, Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value!!)
        editor.apply()
    }

    fun getBooleanSharedPref(key: String?, context: Context): Boolean? {
        val sharedPreferences = context.applicationContext.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getBoolean(key, false)
    }
}