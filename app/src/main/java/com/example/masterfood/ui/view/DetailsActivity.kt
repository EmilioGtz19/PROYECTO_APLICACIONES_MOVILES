package com.example.masterfood.ui.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.masterfood.R
import com.example.masterfood.core.ImageUtilities
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.dialog_ingredients.*


class DetailsActivity() : AppCompatActivity() {

    private var lvIngredientsAdapter : ArrayAdapter<String>?  = null
    private var lvUtensilsAdapter : ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val txtTitle = findViewById<TextView>(R.id.txtTitle)
        val txtName = findViewById<TextView>(R.id.txtName)
        val tvDifficulty = findViewById<TextView>(R.id.tvDifficulty)
        val tvFoodType = findViewById<TextView>(R.id.tvFoodType)
        val tvNationality = findViewById<TextView>(R.id.tvNationality)
        val tvAmount = findViewById<TextView>(R.id.tvAmount)
        var ingredients: ArrayList<String> = arrayListOf()
        var utensils: ArrayList<String> = arrayListOf()
        var ivDetails = findViewById<ImageView>(R.id.ivDetails)

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
            ingredients = b["ingredients"] as ArrayList<String>
            utensils = b["utensils"] as ArrayList<String>
            val photo = b["photo_cover"] as String?

            txtTitle.text = title
            txtName.text = n + " " + l
            tvDifficulty.text = difficulty
            tvFoodType.text = foodType
            tvAmount.text = amount.toString()
            tvNationality.text = nationality
            ivDetails.setImageBitmap(ImageUtilities.getBitMapFromByteArray(photo))
        }

        lvIngredientsAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            ingredients
        )

        lvUtensilsAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            utensils
        )

        btnShowIngredients.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_ingredients,null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle(getString(R.string.listIngredients))
            val lv = mDialogView.findViewById<ListView>(R.id.lvIngredients)
            lv.adapter = lvIngredientsAdapter
            mBuilder.show()
        }


        btnShowUtensils.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_utensils,null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle(getString(R.string.listUtensils))
            val lv = mDialogView.findViewById<ListView>(R.id.lvUtensils)
            lv.adapter = lvUtensilsAdapter
            mBuilder.show()
        }





    }

}