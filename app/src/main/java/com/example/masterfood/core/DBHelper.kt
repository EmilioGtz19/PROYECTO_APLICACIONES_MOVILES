package com.example.masterfood.core

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.example.masterfood.data.model.UserApplication.Companion.prefs
import com.example.masterfood.data.model.UserModel

class DBHelper(var context: Context) : SQLiteOpenHelper(context,
    SetDB.DB_NAME,null,
    SetDB.DB_VERSION
) {

    override fun onCreate(db: SQLiteDatabase?) {

        try{
            val createUsersTable:String =  "CREATE TABLE " + SetDB.UsersTable.TABLE_NAME + "(" +
                    SetDB.UsersTable.COL_ID + " INTEGER PRIMARY KEY," +
                    SetDB.UsersTable.COL_NAME + " VARCHAR(50)," +
                    SetDB.UsersTable.COL_DATE + " VARCHAR(50)," +
                    SetDB.UsersTable.COL_LASTNAME + " VARCHAR(50)," +
                    SetDB.UsersTable.COL_EMAIL + " VARCHAR(100) UNIQUE," +
                    SetDB.UsersTable.COL_PASS + " VARCHAR(100)," +
                    SetDB.UsersTable.COL_IMG + " BLOB)"

            db?.execSQL(createUsersTable)

            Log.e("Create Users Table","Success")

        }catch (e: Exception){
            Log.e("Exception", e.toString())
        }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTable = "DROP TABLE IF EXISTS USERS"
        db!!.execSQL(dropTable)
        onCreate(db)
    }

    fun insertUser(user: UserModel?): Boolean{
        var boolResult = true

        if(!checkEmail(user?.user_id)) {
            val dataBase: SQLiteDatabase = this.writableDatabase
            val values = ContentValues()


            values.put(SetDB.UsersTable.COL_ID, user?.user_id)
            values.put(SetDB.UsersTable.COL_NAME, user?.first_name)
            values.put(SetDB.UsersTable.COL_LASTNAME, user?.last_name)
            values.put(SetDB.UsersTable.COL_EMAIL, user?.email)
            values.put(SetDB.UsersTable.COL_PASS, user?.pass)
            values.put(SetDB.UsersTable.COL_IMG, user?.user_photo)
            values.put(SetDB.UsersTable.COL_DATE, user?.last_update)
            values.put(SetDB.UsersTable.COL_PASS, user?.pass)

            try {
                val result = dataBase.insert(SetDB.UsersTable.TABLE_NAME, null, values)

                if (result == (0).toLong()) {
                    Log.e("Insert", "Error")
                } else {
                    Log.e("Insert", "Success")
                }

            } catch (e: Exception) {
                Log.e("Exception", e.toString())
                boolResult = false
            }

            dataBase.close()
        }

        return boolResult
    }

    private fun checkEmail(id : Int?) : Boolean {
        var isValid = true

        val dataBase:SQLiteDatabase = this.readableDatabase

        try{
            val c: Cursor = dataBase.rawQuery("SELECT " + SetDB.UsersTable.COL_ID + " FROM " + SetDB.UsersTable.TABLE_NAME + " WHERE " + SetDB.UsersTable.COL_ID + " = ?",
                arrayOf(id.toString())
            )

            if(c.moveToFirst()){
                Log.e("Insert","Existing User")
            }else{
                Log.e("Insert","Go to function insertUser")
                isValid = false
            }


        }catch(e: Exception){
            Log.e("Exception", e.toString())
            isValid =  false
        }

        dataBase.close()

        return isValid
    }

    fun login(user: UserModel) : UserModel {
        val dataBase:SQLiteDatabase = this.readableDatabase

        try{
            val c: Cursor = dataBase.rawQuery("SELECT * FROM users WHERE email = ? AND pass = ?",
                arrayOf(user.email, user.pass)
            )

            if(c.moveToFirst()){
                user.user_id = c.getInt(c.getColumnIndexOrThrow(SetDB.UsersTable.COL_ID))
                user.first_name = c.getString(c.getColumnIndexOrThrow(SetDB.UsersTable.COL_NAME))
                user.last_name = c.getString(c.getColumnIndexOrThrow(SetDB.UsersTable.COL_LASTNAME))
                user.email = c.getString(c.getColumnIndexOrThrow(SetDB.UsersTable.COL_EMAIL))
                user.user_photo = c.getString(c.getColumnIndexOrThrow(SetDB.UsersTable.COL_IMG))
                user.last_update = c.getString(c.getColumnIndexOrThrow(SetDB.UsersTable.COL_DATE))
                Log.e("Login SQLite", "Success")
            }else{
                Log.e("Login SQLite", "Failed")
            }

            c.close()

        }catch(e: Exception){
            Log.e("Exception", e.toString())
        }

        dataBase.close()

       return user
    }

}