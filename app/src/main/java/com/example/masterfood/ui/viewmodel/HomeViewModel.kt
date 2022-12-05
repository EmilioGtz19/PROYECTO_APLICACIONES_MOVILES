package com.example.masterfood.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.masterfood.core.RestEngine
import com.example.masterfood.data.model.RecipeModel
import com.example.masterfood.data.model.RecipeModel2
import com.example.masterfood.data.model.RecipeModelHome
import com.example.masterfood.data.network.RecipeService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    var getRecipesLiveData: MutableLiveData<List<RecipeModelHome>?> = MutableLiveData()

    var getRecipesByIdLiveData : MutableLiveData<RecipeModel2?> = MutableLiveData()


    fun getRecipeObserver() : MutableLiveData<List<RecipeModelHome>?> {
        return getRecipesLiveData
    }

    fun getRecipeByIdObserver() : MutableLiveData<RecipeModel2?> {
        return getRecipesByIdLiveData
    }

    fun getRecipes(){

        val recipeService: RecipeService =  RestEngine.getRestEngine().create(RecipeService::class.java)
        val result : Call<List<RecipeModelHome>> = recipeService.getRecipes()

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

    fun getRecipeById(id : Int){
        val recipeService: RecipeService =  RestEngine.getRestEngine().create(RecipeService::class.java)
        val result : Call<RecipeModel2> = recipeService.getRecipesFromId(id)

        result.enqueue(object: Callback<RecipeModel2> {
            override fun onFailure(call: Call<RecipeModel2>, t: Throwable) {
                getRecipesByIdLiveData.postValue(null)
            }

            override fun onResponse(call: Call<RecipeModel2>, response: Response<RecipeModel2>) {
                if(response.isSuccessful){
                    getRecipesByIdLiveData.postValue(response.body())
                }else{
                    getRecipesByIdLiveData.postValue(null)
                }
            }
        })
    }

}