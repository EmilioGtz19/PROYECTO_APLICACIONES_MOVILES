package com.example.masterfood.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.masterfood.core.DBHelper
import com.example.masterfood.core.RestEngine
import com.example.masterfood.data.network.UserService
import com.example.masterfood.data.model.ApiResponse
import com.example.masterfood.data.model.UserApplication
import com.example.masterfood.data.model.UserApplication.Companion.db
import com.example.masterfood.data.model.UserModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {

    var insertUserLiveData: MutableLiveData<ApiResponse?> = MutableLiveData()
    var updateUserLiveData: MutableLiveData<ApiResponse?> = MutableLiveData()
    var updatePassLiveData: MutableLiveData<ApiResponse?> = MutableLiveData()
    var loginLiveData: MutableLiveData<UserModel?> = MutableLiveData()


    fun insertUserObserver(): MutableLiveData<ApiResponse?> {
        return insertUserLiveData
    }

    fun updateUserObserver(): MutableLiveData<ApiResponse?> {
        return updateUserLiveData
    }

    fun updatePassObserver(): MutableLiveData<ApiResponse?>{
        return updatePassLiveData
    }

    fun loginObserver(): MutableLiveData<UserModel?>{
        return loginLiveData
    }

    fun insertUser(User: UserModel){
        val userService: UserService =  RestEngine.getRestEngine().create(UserService::class.java)
        val result = userService.saveUser(User)

        result.enqueue(object: Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                insertUserLiveData.postValue(null)
            }

            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
               if(response.isSuccessful){
                   insertUserLiveData.postValue(response.body())
               }else{
                   insertUserLiveData.postValue(null)
               }
            }
        })
    }

    fun updateUser(User: UserModel){
        val userService: UserService = RestEngine.getRestEngine().create(UserService::class.java)
        val result = userService.updateUser(User)

        result.enqueue(object: Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                updateUserLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                updateUserLiveData.postValue(null)
            }
        })
    }

    fun updatePass(User: UserModel){
        val userService: UserService = RestEngine.getRestEngine().create(UserService::class.java)
        val result = userService.updatePass(User)

        result.enqueue(object: Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                updatePassLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                updatePassLiveData.postValue(null)
            }
        })
    }

    fun login(User: UserModel){

        val userService: UserService = RestEngine.getRestEngine().create(UserService::class.java)
        val result = userService.login(User)

        result.enqueue(object: Callback<UserModel>{
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                val user : UserModel? = response.body()
                db.insertUser(user)
                loginLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                val user : UserModel = db.login(User)
                Log.e("User",user.user_id.toString())
                if(user.user_id != 0){
                    loginLiveData.postValue(user)
                }else{
                    loginLiveData.postValue(null)
                }
            }

        })

    }

}