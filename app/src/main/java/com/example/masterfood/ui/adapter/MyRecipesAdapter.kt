package com.example.masterfood.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.masterfood.R
import com.example.masterfood.data.model.RecipeModelHome

class MyRecipesAdapter(private val MyRecipesList:List<RecipeModelHome>) : RecyclerView.Adapter<MyRecipesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecipesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyRecipesViewHolder(layoutInflater.inflate(R.layout.item_my_recipes,parent,false))
    }

    override fun onBindViewHolder(holder: MyRecipesViewHolder, position: Int) {
        val item = MyRecipesList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = MyRecipesList.size
}