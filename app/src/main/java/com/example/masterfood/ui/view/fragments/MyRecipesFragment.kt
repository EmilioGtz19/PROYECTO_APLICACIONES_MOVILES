package com.example.masterfood.ui.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.masterfood.R
import com.example.masterfood.core.ImageUtilities
import com.example.masterfood.data.model.RecipeModelHome
import com.example.masterfood.data.model.UserApplication
import com.example.masterfood.data.model.UserModel
import com.example.masterfood.ui.adapter.MyRecipesAdapter
import com.example.masterfood.ui.view.RecipeActivity
import com.example.masterfood.ui.viewmodel.MyRecipesViewModel


class MyRecipesFragment : Fragment() {

    private lateinit var viewModel: MyRecipesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()

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

        var id = this.loadPreference(img)

        initRecyclerView(id)

        // Inflate the layout for this fragment
        return view
    }

    private fun initRecyclerView(id : Int?){
        if (id != null) {
            viewModel.getRecipesFromUser(id)
        }
    }

    private fun loadPreference(img : ImageView) : Int?{
        val credential: UserModel = UserApplication.prefs.getUser()
        if (credential.first_name != "No information") {
            img.setImageBitmap(ImageUtilities.getBitMapFromByteArray(credential.user_photo))
        }
        return credential.user_id
    }

    private fun initViewModel(){
        viewModel = ViewModelProvider(requireActivity())[MyRecipesViewModel::class.java]
        viewModel.getRecipeObserver().observe(requireActivity(), androidx.lifecycle.Observer <List<RecipeModelHome>?> { response ->
            if(response == null){
                Toast.makeText(requireActivity(), "Error",Toast.LENGTH_LONG).show()
            }else{
                val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerMyRecipes)
                if (recyclerView != null) {
                    recyclerView.layoutManager = LinearLayoutManager(activity)
                    recyclerView.adapter = MyRecipesAdapter(response)
                }
            }
        })
    }
}