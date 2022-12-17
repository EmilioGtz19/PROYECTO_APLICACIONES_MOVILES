package com.example.masterfood.ui.adapter

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.masterfood.R
import com.example.masterfood.core.ImageUtilities

class PhotosRecipesViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val photo = view.findViewById<ImageView>(R.id.ivPhotoRecipes)

    fun render(photoString: String){
        photo.setImageBitmap(ImageUtilities.getBitMapFromByteArray(photoString))
    }

}