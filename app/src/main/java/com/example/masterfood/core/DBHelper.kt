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
            val createUsersTable:String =  "CREATE TABLE " + SetDB.table_users.TABLE_NAME + "(" +
                    SetDB.table_users.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SetDB.table_users.COL_NAME + " VARCHAR(50)," +
                    SetDB.table_users.COL_LASTNAME + " VARCHAR(50)," +
                    SetDB.table_users.COL_EMAIL + " VARCHAR(100) UNIQUE," +
                    SetDB.table_users.COL_PASS + " VARCHAR(100)," +
                    SetDB.table_users.COL_IMG + " BLOB)"

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

    fun insertUser(user: UserModel): Boolean{

        val dataBase:SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        var boolResult =  true

        values.put(SetDB.table_users.COL_NAME,user.first_name)
        values.put(SetDB.table_users.COL_LASTNAME,user.last_name)
        values.put(SetDB.table_users.COL_EMAIL,user.email)
        values.put(SetDB.table_users.COL_PASS,user.pass)
        values.put(SetDB.table_users.COL_IMG,user.user_photo)
        values.put(SetDB.table_users.COL_DATE,user.last_update)

        try {
            val result =  dataBase.insert(SetDB.table_users.TABLE_NAME, null, values)

            if (result == (0).toLong()) {
                Log.e("Insertar usuario","Error")
            }
            else {
                Log.e("Insertar usuario","Agregado")
            }

        }catch (e: Exception){
            Log.e("Exception", e.toString())
            boolResult =  false
        }

        dataBase.close()

        return boolResult


    }

    fun login(email : String, pass : String) : Boolean {

        var isValid = true

        val user = UserModel()
        val dataBase:SQLiteDatabase = this.readableDatabase

        try{
            val c: Cursor = dataBase.rawQuery("SELECT user_id, name, last_name, email, pass FROM users WHERE email = ? AND pass = ?",
                arrayOf(email, pass)
            )

            if(c.moveToFirst()){
                user.user_id = c.getInt(c.getColumnIndexOrThrow(SetDB.table_users.COL_ID))
                user.first_name = c.getString(c.getColumnIndexOrThrow(SetDB.table_users.COL_NAME))
                user.last_name = c.getString(c.getColumnIndexOrThrow(SetDB.table_users.COL_LASTNAME))
                user.email = c.getString(c.getColumnIndexOrThrow(SetDB.table_users.COL_EMAIL))
                prefs.saveUser(user)
                Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()
                isValid = false
            }

            c.close()

        }catch(e: Exception){
            Log.e("Exception", e.toString())
            isValid =  false
        }

        dataBase.close()

        return isValid

    }

}