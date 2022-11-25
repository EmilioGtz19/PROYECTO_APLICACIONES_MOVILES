package com.example.masterfood.data.model

data class UserModel(
    var user_id: Int? = null,
    var first_name: String? = null,
    var last_name: String? = null,
    var email: String? = null,
    val pass: String? = null,
    var user_photo: String? = null,
    var last_update: String? = null
)
