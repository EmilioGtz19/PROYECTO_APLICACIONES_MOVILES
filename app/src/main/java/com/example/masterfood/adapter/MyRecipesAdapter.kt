package com.example.masterfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.masterfood.MyRecipes
import com.example.masterfood.R

class MyRecipesAdapter(private val MyRecipesList:List<MyRecipes>) : RecyclerView.Adapter<MyRecipesViewHolder>() {
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