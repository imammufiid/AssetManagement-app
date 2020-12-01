package com.mufiid.assetmanagement.utils

import android.content.Context
import androidx.core.content.edit
import com.mufiid.assetmanagement.models.User

object Constants {

    const val API_ENDPOINT = ""

    fun getUserData(context: Context): User? {
        val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
        return User().apply {
            username = pref.getString("USERNAME", "")
            token = pref.getString("TOKEN", "")
            id = pref.getString("ID_USER", "")
        }
    }

    fun setUserData(context: Context, user: User) {
        val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
        pref.edit {
            putString("USERNAME", user.username)
            putString("TOKEN", user.token)
            putString("ID_USER", user.id)
        }
    }

    fun getIsLoggedIn(context: Context): Boolean? {
        val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
        return pref.getBoolean("IS_LOGGED_IN", false)
    }

    fun setIsLoggedIn(context: Context, isLoggedIn: Boolean) {
        val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
        pref.edit {
            putBoolean("IS_LOGGED_IN", isLoggedIn)
        }
    }
}