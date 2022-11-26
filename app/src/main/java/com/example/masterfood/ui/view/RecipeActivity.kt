package com.example.masterfood.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.masterfood.R
import com.example.masterfood.data.model.*
import com.example.masterfood.ui.viewmodel.RecipeViewModel
import kotlinx.android.synthetic.main.activity_recipe.*
import java.util.*


class RecipeActivity : AppCompatActivity() {

    private lateinit var viewModel: RecipeViewModel

    private var etTitle : EditText? = null
    private var etAmount : EditText? = null
    private var etNationality : EditText? = null
    private var etIngredients : EditText? = null
    private var etUtensils : EditText? = null
    private var etSteps : EditText? = null

    private var lvIngredients : ListView? = null
    private var lvUtensils : ListView? = null
    private var lvSteps : ListView? = null

    private var lvIngredientsList = ArrayList<String>()
    private var lvIngredientsAdapter : ArrayAdapter<String>?  = null

    private var lvUtensilsList = ArrayList<String>()
    private var lvUtensilsAdapter : ArrayAdapter<String>?  = null

    private var lvStepsList = ArrayList<String>()
    private var lvStepsAdapter : ArrayAdapter<String>?  = null

    private var foodSpinner : Spinner? = null
    private var difficultSpinner : Spinner? = null

    private var foodItem : Int? = null
    private var difficultItem : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        initViewModel()
        etTitle = findViewById(R.id.txtTitle)
        etAmount = findViewById(R.id.txtAmount)
        etNationality = findViewById(R.id.txtNationality)
        etIngredients = findViewById(R.id.txtIngredients)
        etUtensils = findViewById(R.id.txtUtensils)
        etSteps = findViewById(R.id.txtSteps)

        lvIngredients = findViewById(R.id.lvIngredients)
        lvUtensils = findViewById(R.id.lvUtensils)
        lvSteps = findViewById(R.id.lvSteps)

        foodSpinner = findViewById(R.id.food_spinner)
        difficultSpinner = findViewById(R.id.difficulty_spinner)


        lvIngredientsAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            lvIngredientsList
        )

        lvUtensilsAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            lvUtensilsList
        )

        lvStepsAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            lvStepsList
        )

        btnAddIngredient.setOnClickListener {
            getIngredient()
        }

        btnAddUtensils.setOnClickListener {
            getUtensils()
        }

        btnAddSteps.setOnClickListener {
            getSteps()
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.food_type,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            foodSpinner?.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.difficulty_type,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            difficultSpinner?.adapter = adapter
        }

        foodSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                foodItem = position
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        difficultSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                difficultItem = position
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        btnUpload.setOnClickListener {
            if(validateForm()) {
                insert()
            }
        }
    }

    private fun validateForm() : Boolean{
        var isValid = true

        with(this){
            if(etTitle?.text.toString().isBlank()){
                isValid = false
                etTitle?.error = getString(R.string.required)
            }
            if(etAmount?.text.toString().isBlank()){
                isValid = false
                etAmount?.error = getString(R.string.required)
            }
            if(etNationality?.text.toString().isBlank()){
                isValid = false
                etNationality?.error = getString(R.string.required)
            }
            if(lvIngredientsList.isEmpty()){
                isValid = false
                etIngredients?.error = getString(R.string.EmptyIngredientsList)
            }
            if(lvUtensilsList.isEmpty()){
                isValid = false
                etUtensils?.error =  getString(R.string.EmptyUtensilsList)
            }
            if(lvStepsList.isEmpty()){
                isValid = false
                etSteps?.error = getString(R.string.EmptyStepsList)
            }
        }

        return isValid
    }

    private fun getIngredient(){
        val text = etIngredients?.text.toString().trim()
        lvIngredientsList.add(text)
        lvIngredients?.adapter = lvIngredientsAdapter
        etIngredients?.setText("")
    }

    private fun getUtensils(){
        val text = etUtensils?.text.toString().trim()
        lvUtensilsList.add(text)
        lvUtensils?.adapter = lvUtensilsAdapter
        etUtensils?.setText("")
    }

    private fun getSteps(){
        val text = etSteps?.text.toString().trim()
        lvStepsList.add(text)
        lvSteps?.adapter = lvStepsAdapter
        etSteps?.setText("")
    }

    private fun initViewModel(){
        viewModel = ViewModelProvider(this)[RecipeViewModel::class.java]
        viewModel.insertRecipeObserver().observe(this, androidx.lifecycle.Observer <ApiResponse?> { response ->
            if(response == null){
                Toast.makeText(this@RecipeActivity, "Error", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this@RecipeActivity, getString(R.string.recipe_publish), Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        })
    }

    private fun loadUser() : Int? {
        val credential: UserModel = UserApplication.prefs.getUser()
        return credential.user_id
    }

    private fun insert() {
        val recipe = RecipeModel(
            loadUser(),
            etTitle?.text.toString(),
            foodItem,
            Integer.valueOf(etAmount?.text.toString()),
            lvIngredientsList,
            lvUtensilsList,
            lvStepsList,
            difficultItem,
            etNationality?.text.toString())
        viewModel.insertRecipe(recipe)
    }

}