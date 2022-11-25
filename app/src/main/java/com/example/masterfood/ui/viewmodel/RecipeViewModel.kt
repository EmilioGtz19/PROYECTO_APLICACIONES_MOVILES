package com.example.masterfood.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.masterfood.core.RestEngine
import com.example.masterfood.data.model.ApiResponse
import com.example.masterfood.data.model.RecipeModel
import com.example.masterfood.data.network.RecipeService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeViewModel : ViewModel() {

    var insertRecipeLiveData: MutableLiveData<ApiResponse?> = MutableLiveData()

    fun insertRecipeObserver() : MutableLiveData<ApiResponse?> {
        return insertRecipeLiveData
    }

    fun insertRecipe(Recipe: RecipeModel){
        val recipeService: RecipeService =  RestEngine.getRestEngine().create(RecipeService::class.java)
        val result = recipeService.saveRecipe(Recipe)

        result.enqueue(object: Callback<ApiResponse> {
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                insertRecipeLiveData.postValue(null)
            }

            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if(response.isSuccessful){
                    insertRecipeLiveData.postValue(response.body())
                }else{
                    insertRecipeLiveData.postValue(null)
                }
            }
        })
    }

}