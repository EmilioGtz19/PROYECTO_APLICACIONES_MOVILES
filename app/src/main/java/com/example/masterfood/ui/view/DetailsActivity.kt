package com.example.masterfood.ui.view

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.masterfood.R


class DetailsActivity() : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val txtTitle = findViewById<TextView>(R.id.txtTitle)
        val txtName = findViewById<TextView>(R.id.txtName)
        val tvDifficulty = findViewById<TextView>(R.id.tvDifficulty)
        val tvFoodType = findViewById<TextView>(R.id.tvFoodType)
        val tvNationality = findViewById<TextView>(R.id.tvNationality)
        val tvAmount = findViewById<TextView>(R.id.tvAmount)


        val iin = intent
        val b = iin.extras

        if (b != null) {
            val title = b["title"] as String?
            val n = b["first_name"]  as String?
            val l = b["last_name"] as String?
            val difficulty = b["difficulty"] as String?
            val foodType = b["food_type"] as String?
            val nationality = b["nationality"] as String?
            val amount = b["amount"] as Int?

            txtTitle.text = title
            txtName.text = n + " " + l
            tvDifficulty.text = difficulty
            tvFoodType.text = foodType
            tvAmount.text = amount.toString()
            tvNationality.text = nationality

        }
    }

}