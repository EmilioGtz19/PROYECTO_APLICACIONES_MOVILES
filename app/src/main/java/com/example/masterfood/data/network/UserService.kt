package com.example.masterfood.data.network

import androidx.room.Insert
import com.example.masterfood.data.model.ApiResponse
import com.example.masterfood.data.model.UserModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserService {

    @Headers("Content-Type: application/json")
    @POST("Users/register")
    fun saveUser(@Body userData : UserModel) : Call<ApiResponse>

    @Headers("Content-Type: application/json")
    @POST("Users/login")
    fun login(@Body userData: UserModel) : Call <UserModel>

    @Headers("Content-Type: application/json")
    @POST("Users/updateUser")
    fun updateUser(@Body userData: UserModel) : Call <ApiResponse>

    @Headers("Content-Type: application/json")
    @POST("Users/updatePass")
    fun updatePass(@Body userData: UserModel) : Call <ApiResponse>

}