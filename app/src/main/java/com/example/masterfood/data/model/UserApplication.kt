package com.example.masterfood.data.model

import android.app.Application
import com.example.masterfood.core.DBHelper
import com.example.masterfood.core.InternetUtilities.isNetworkAvailable


class UserApplication : Application() {

    companion object{
        lateinit var prefs: Prefs
        lateinit var db: DBHelper
    }

    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
        db = DBHelper(this)
        loadPreference()
    }

    private fun loadPreference(){
        val credential: UserModel = prefs.getUser()
        if (credential.user_id != 0) {
            if (isNetworkAvailable(applicationContext)) {
                val lastUpdateMySQL = credential.last_update;
            }
        }
    }

}