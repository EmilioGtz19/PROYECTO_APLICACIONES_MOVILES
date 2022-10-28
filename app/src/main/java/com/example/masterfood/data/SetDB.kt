package com.example.masterfood.data

class SetDB {

    companion object{
        val DB_NAME = "DB_MasterFood.db"
        val DB_VERSION = 2
    }

    abstract class TABLE_USERS{
        companion object{
            val TABLE_NAME = "USERS"
            val COL_ID =  "USER_ID"
            val COL_NAME =  "NAME"
            val COL_LASTNAME = "LAST_NAME"
            val COL_EMAIL =  "EMAIL"
            val COL_PASS =  "PASS"
            val COL_IMG =  "AVATAR"
        }
    }

}