package com.example.masterfood.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.masterfood.R
import com.example.masterfood.data.model.RecipeModelHome

class MyRecipesAdapter(private var MyRecipesList:List<RecipeModelHome>) : RecyclerView.Adapter<MyRecipesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecipesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyRecipesViewHolder(layoutInflater.inflate(R.layout.item_listview_steps,parent,false))
    }

    override fun onBindViewHolder(holder: MyRecipesViewHolder, position: Int) {
        val item = MyRecipesList[position]
        holder.render(item)
    }

    fun updateRecipes(kitchenRecipesListFilter:List<RecipeModelHome>) {
        this.MyRecipesList = kitchenRecipesListFilter
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = MyRecipesList.size
}