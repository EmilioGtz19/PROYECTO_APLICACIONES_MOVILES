package com.example.masterfood.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.masterfood.core.RestEngine
import com.example.masterfood.data.model.RecipeModelHome
import com.example.masterfood.data.network.RecipeService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyRecipesViewModel : ViewModel() {

    var getRecipesLiveData: MutableLiveData<List<RecipeModelHome>?> = MutableLiveData()

    fun getRecipeObserver() : MutableLiveData<List<RecipeModelHome>?> {
        return getRecipesLiveData
    }

    fun getRecipesFromUser(id : Int){

        val recipeService: RecipeService =  RestEngine.getRestEngine().create(RecipeService::class.java)
        val result : Call<List<RecipeModelHome>> = recipeService.getRecipesFromUser(id)

        result.enqueue(object: Callback<List<RecipeModelHome>> {
            override fun onFailure(call: Call<List<RecipeModelHome>>, t: Throwable) {
                getRecipesLiveData.postValue(null)
            }

            override fun onResponse(call: Call<List<RecipeModelHome>>, response: Response<List<RecipeModelHome>>) {
                if(response.isSuccessful){
                    getRecipesLiveData.postValue(response.body())
                }else{
                    getRecipesLiveData.postValue(null)
                }
            }
        })
    }

}