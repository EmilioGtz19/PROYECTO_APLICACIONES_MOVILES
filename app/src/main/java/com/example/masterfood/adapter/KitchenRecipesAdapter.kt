package com.example.masterfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.masterfood.KitchenRecipes
import com.example.masterfood.R

class KitchenRecipesAdapter(private val kitchenRecipesList:List<KitchenRecipes>) : RecyclerView.Adapter<KitchenRecipesViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KitchenRecipesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return KitchenRecipesViewHolder(layoutInflater.inflate(R.layout.item_kitchen_recipes,parent,false))
    }

    override fun onBindViewHolder(holder: KitchenRecipesViewHolder, position: Int) {
        val item = kitchenRecipesList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = kitchenRecipesList.size
}