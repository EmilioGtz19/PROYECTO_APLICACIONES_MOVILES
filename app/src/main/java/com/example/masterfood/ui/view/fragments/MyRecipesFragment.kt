package com.example.masterfood.ui.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.masterfood.data.model.MyRecipesProvider
import com.example.masterfood.R
import com.example.masterfood.core.ImageUtilities
import com.example.masterfood.data.model.UserApplication
import com.example.masterfood.data.model.UserModel
import com.example.masterfood.ui.adapter.MyRecipesAdapter
import com.example.masterfood.ui.view.RecipeActivity


class MyRecipesFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view : View = inflater.inflate(R.layout.fragment_my_recipes, container, false)
        val fab :View = view.findViewById(R.id.fab)
        val img = view.findViewById<ImageView>(R.id.imageView7)


        fab.setOnClickListener {
            val intent =  Intent(requireActivity(),RecipeActivity::class.java)
            startActivity(intent)
        }

        initRecyclerView(view)


        this.loadPreference(img)

        // Inflate the layout for this fragment
        return view
    }

    private fun initRecyclerView(view : View){
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerMyRecipes)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = MyRecipesAdapter(MyRecipesProvider.myRecipes)
    }

    private fun loadPreference(img : ImageView){
        val credential: UserModel = UserApplication.prefs.getUser()
        if (credential.first_name != "No information") {
            img.setImageBitmap(ImageUtilities.getBitMapFromByteArray(credential.user_photo))
        }
    }
}