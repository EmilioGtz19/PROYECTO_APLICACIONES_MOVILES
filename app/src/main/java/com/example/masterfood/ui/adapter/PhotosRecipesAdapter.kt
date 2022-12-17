package com.example.masterfood.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.masterfood.R


class PhotosRecipesAdapter(private val PhotoList:List<String>) : RecyclerView.Adapter<PhotosRecipesViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosRecipesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PhotosRecipesViewHolder(layoutInflater.inflate(R.layout.item_my_recipes,parent,false))
    }

    override fun onBindViewHolder(holder: PhotosRecipesViewHolder, position: Int) {
        val item = PhotoList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = PhotoList.size
}