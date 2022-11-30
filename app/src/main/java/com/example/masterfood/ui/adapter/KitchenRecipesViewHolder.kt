package com.example.masterfood.ui.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.masterfood.R
import com.example.masterfood.data.model.RecipeModelHome

class KitchenRecipesViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val user: TextView = view.findViewById(R.id.tvName2)
    private val title: TextView = view.findViewById(R.id.tvRecipeTitle)
    private val difficulty : TextView = view.findViewById(R.id.tvDifficulty2)
    private val nationality : TextView = view.findViewById(R.id.tvNationality2)
    //val photo = view.findViewById<ImageView>(R.id.ivKitchenRecipes)

    fun render(kitchenRecipesModel: RecipeModelHome){
        user.text = kitchenRecipesModel.first_name
        title.text = kitchenRecipesModel.title
        difficulty.text = kitchenRecipesModel.difficulty
        nationality.text = kitchenRecipesModel.nationality
    }

}