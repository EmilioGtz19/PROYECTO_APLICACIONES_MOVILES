package com.example.masterfood.data.network

import com.example.masterfood.data.model.ApiResponse
import com.example.masterfood.data.model.RecipeModel
import com.example.masterfood.data.model.RecipeModelHome
import retrofit2.Call
import retrofit2.http.*

interface RecipeService {

    @Headers("Content-Type: application/json")
    @POST("Recipes/insert")
    fun saveRecipe(@Body recipeData : RecipeModel) : Call<ApiResponse>

    @GET("Recipes/list")
    fun getRecipes() : Call<List<RecipeModelHome>>

    @GET("Recipes/recipesUser")
    fun getRecipesFromUser(@Query("id") id : Int) : Call<List<RecipeModelHome>>

}