package com.example.masterfood.data.model

import android.content.Context
import java.sql.Timestamp

class Prefs (val context: Context) {

    private val sharedName = "user_preferences"
    private val managerPrefs = context.getSharedPreferences(sharedName,Context.MODE_PRIVATE)

    fun saveUser(user: UserModel){
        val editor =  managerPrefs.edit()
        editor.putString("user_id", user.user_id.toString())
        editor.putString("name",user.first_name)
        editor.putString("lastname",user.last_name)
        editor.putString("email",user.email)
        editor.putString("user_photo",user.user_photo)
        editor.putString("last_update",user.last_update)
        editor.commit()
    }

    fun getUser(): UserModel {

        val user =  UserModel()

        val intId: String? = managerPrefs.getString("user_id","0")
        val strName: String? =  managerPrefs.getString("name","No information")
        val strLastName: String? =  managerPrefs.getString("lastname","No information")
        val strEmail:String? =  managerPrefs.getString("email", "No information")
        val photo:String? = managerPrefs.getString("user_photo","No information")
        val lastUpdate:String? = managerPrefs.getString("last_update","No information")

        user.user_id =  intId!!.toInt()
        user.first_name =  strName!!
        user.last_name = strLastName!!
        user.email = strEmail!!
        user.user_photo = photo!!
        user.last_update = lastUpdate!!


        return user
    }

    fun logout(){
        val editor =  managerPrefs.edit()
        editor.remove("user_id")
        editor.remove("name")
        editor.remove("lastname")
        editor.remove("email")
        editor.remove("user_photo")
        editor.remove("last_update")
        editor.commit()
    }
}