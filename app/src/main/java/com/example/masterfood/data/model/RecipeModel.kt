package com.example.masterfood.data.model

//Clase para insertar la receta
data class RecipeModel(
    var user_id : Int? = null,
    var title : String? = null,
    var food_type_id : Int? = null,
    var amount_people : Int? = null,
    var ingredients : ArrayList<String>? = null,
    var utensils : ArrayList<String>? = null,
    var steps : ArrayList<String>? = null,
    var difficulty_id : Int? = null,
    var nationality : String? = null,
    var photo_cover: String? = null
)

//Clase para mostrar las recetas en la pantalla principal y en la pantalla de las recetas del usuario
data class RecipeModelHome(
    var user_id : Int? = null,
    var recipe_id : Int? = null,
    var first_name: String? = null,
    var title : String? = null,
    var difficulty : String? = null,
    var nationality : String? = null,
    var photo_cover: String? = null
)

//Clase para mostrar el detalle de la receta
data class RecipeModel2(
    var first_name: String? = null,
    var last_name: String? = null,
    var title : String? = null,
    var difficulty: String? = null,
    var nationality : String? = null,
    var amount_people: Int? = null,
    var food_type: String? = null,
    var ingredients: ArrayList<String>,
    var utensils: ArrayList<String>,
    var steps: ArrayList<String>,
    var photo_cover: String? = null
)