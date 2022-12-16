package com.example.masterfood.ui.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.masterfood.R
import com.example.masterfood.core.ImageUtilities
import com.example.masterfood.data.model.RecipeModelHome

class MyRecipesViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val title : TextView = view.findViewById<TextView>(R.id.tvRecipeTitle)
    private val name: TextView = view.findViewById<TextView>(R.id.tvName2)
    private val nationality = view.findViewById<TextView>(R.id.tvNationality2)
    private val difficulty = view.findViewById<TextView>(R.id.tvDifficulty2)
    private val photo = view.findViewById<ImageView>(R.id.ivKitchenRecipes)

    fun render(myRecipes: RecipeModelHome){
        title.text = myRecipes.title
        name.text = myRecipes.first_name
        nationality.text = myRecipes.nationality
        difficulty.text = myRecipes.difficulty
        photo.setImageBitmap(ImageUtilities.getBitMapFromByteArray(myRecipes.photo_cover))
    }
}