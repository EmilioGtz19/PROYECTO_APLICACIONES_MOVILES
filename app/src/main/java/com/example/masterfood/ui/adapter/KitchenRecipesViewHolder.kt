package com.example.masterfood.ui.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.masterfood.data.model.KitchenRecipes
import com.example.masterfood.R

class KitchenRecipesViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val title = view.findViewById<TextView>(R.id.tvRecipeTitle)
    val foodType = view.findViewById<TextView>(R.id.tvFoodType)
    val difficulty = view.findViewById<TextView>(R.id.tvDifficulty)
    val photo = view.findViewById<ImageView>(R.id.ivKitchenRecipes)

    fun render(kitchenRecipesModel: KitchenRecipes){
        title.text = kitchenRecipesModel.title
        foodType.text = kitchenRecipesModel.foodType
        difficulty.text = kitchenRecipesModel.difficulty

    }
}