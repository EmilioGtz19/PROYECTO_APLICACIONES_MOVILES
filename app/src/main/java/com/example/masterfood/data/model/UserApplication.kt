package com.example.masterfood.data.model

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.masterfood.core.DBHelper


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

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

}