package com.example.masterfood.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

class DBHelper (var context : Context) : SQLiteOpenHelper(context, SetDB.DB_NAME,null,SetDB.DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {

        try{
            val createUsersTable:String =  "CREATE TABLE " + SetDB.TABLE_USERS.TABLE_NAME + "(" +
                    SetDB.TABLE_USERS.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SetDB.TABLE_USERS.COL_NAME + " VARCHAR(50)," +
                    SetDB.TABLE_USERS.COL_LASTNAME + " VARCHAR(50)," +
                    SetDB.TABLE_USERS.COL_EMAIL + " VARCHAR(100) UNIQUE," +
                    SetDB.TABLE_USERS.COL_PASS + " VARCHAR(100)," +
                    SetDB.TABLE_USERS.COL_IMG + " BLOB)"

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

    fun insertUser(name:String,lastname:String,email:String,pass:String,img:ByteArray): Boolean{

        val dataBase:SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        var boolResult =  true

        values.put(SetDB.TABLE_USERS.COL_NAME,name)
        values.put(SetDB.TABLE_USERS.COL_LASTNAME,lastname)
        values.put(SetDB.TABLE_USERS.COL_EMAIL,email)
        values.put(SetDB.TABLE_USERS.COL_PASS,pass)
        values.put(SetDB.TABLE_USERS.COL_IMG,img)

        try {
            val result =  dataBase.insert(SetDB.TABLE_USERS.TABLE_NAME, null, values)

            if (result == (0).toLong()) {
                Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
            }

        }catch (e: Exception){
            Log.e("Exception", e.toString())
            boolResult =  false
        }

        dataBase.close()

        return boolResult
    }

    fun checkEmail(checkEmail:String) : Boolean{
        var isValid = true
        val dataBase:SQLiteDatabase = this.readableDatabase

        try{
            val c: Cursor = dataBase.rawQuery("SELECT email FROM users WHERE email = ?",
                arrayOf(checkEmail)
            )

            if(c.moveToFirst()){
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