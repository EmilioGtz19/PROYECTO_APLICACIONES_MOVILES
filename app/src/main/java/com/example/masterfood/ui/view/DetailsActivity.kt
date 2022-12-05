package com.example.masterfood.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.masterfood.R
import com.example.masterfood.data.model.RecipeModel2

class DetailsActivity() : AppCompatActivity() {

    //var recipe : RecipeModel2 = response

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val txtName = findViewById<EditText>(R.id.txtTitle)

        //txtName.setText(recipe.title)
    }

}