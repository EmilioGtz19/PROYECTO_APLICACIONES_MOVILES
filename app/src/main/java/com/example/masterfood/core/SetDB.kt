package com.example.masterfood.core

class SetDB {

    companion object{
        const val DB_NAME = "DB_MasterFood.db"
        const val DB_VERSION = 2
    }

    abstract class table_users{
        companion object{
            const val TABLE_NAME = "USERS"
            const val COL_ID =  "USER_ID"
            const val COL_NAME =  "NAME"
            const val COL_LASTNAME = "LAST_NAME"
            const val COL_EMAIL =  "EMAIL"
            const val COL_PASS =  "PASS"
            const val COL_DATE = "LAST_UPDATE"
            const val COL_IMG =  "AVATAR"
        }
    }

}