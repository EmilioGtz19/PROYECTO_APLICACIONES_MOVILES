package com.example.masterfood.data.model

data class RecipeModel(
    var user_id : Int? = null,
    var title : String? = null,
    var food_type_id : Int? = null,
    var amount_people : Int? = null,
    var ingredients : ArrayList<String>? = null,
    var utensils : ArrayList<String>? = null,
    var steps : ArrayList<String>? = null,
    var difficulty_id : Int? = null,
    var nationality : String? = null
)

data class RecipeModelHome(
    var user_id : Int? = null,
    var recipe_id : Int? = null,
    var first_name: String? = null,
    var title : String? = null,
    var difficulty : String? = null,
    var nationality : String? = null
)

data class RecipeModel2(

    var title : String? = null,

)