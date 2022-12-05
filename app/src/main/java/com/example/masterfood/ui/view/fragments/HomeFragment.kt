package com.example.masterfood.ui.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.masterfood.*
import com.example.masterfood.ui.adapter.KitchenRecipesAdapter
import com.example.masterfood.data.model.UserApplication.Companion.prefs
import com.example.masterfood.ui.view.LoginActivity
import com.example.masterfood.core.ImageUtilities.getBitMapFromByteArray
import com.example.masterfood.data.model.*
import com.example.masterfood.ui.view.DetailsActivity
import com.example.masterfood.ui.viewmodel.HomeViewModel
import com.example.masterfood.ui.viewmodel.UserViewModel
import java.text.FieldPosition


class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
        findRecipeViewModel()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_home, container, false)
        val btn = view.findViewById<Button>(R.id.button)
        val txt = view.findViewById<TextView>(R.id.txtUserHome)
        val img = view.findViewById<ImageView>(R.id.imageView7)

        this.loadPreference(txt,img,btn)

        btn.setOnClickListener{
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            requireActivity().startActivity(intent)
        }


        initRecyclerView(view)

        // Inflate the layout for this fragment
        return view
    }


    private fun initRecyclerView(view : View){
        viewModel.getRecipes()
    }

    private fun loadPreference(txt : TextView, img : ImageView, btn : Button){
        val credential: UserModel = prefs.getUser()
        if (credential.first_name != "No information") {
            (getString(R.string.welcome) + " " + credential.first_name).also { txt.text = it }
            img.setImageBitmap(getBitMapFromByteArray(credential.user_photo))
            btn.isVisible = false
        }else{
            txt.text = getString(R.string.welcome)
            btn.isVisible = true
        }
    }

    private fun initViewModel(){
        viewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        viewModel.getRecipeObserver().observe(requireActivity(), androidx.lifecycle.Observer <List<RecipeModelHome>?> { response ->
            if(response == null){
                Toast.makeText(requireActivity(), "Error",Toast.LENGTH_LONG).show()
            }else{
                val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerKitchenRecipes)
                if (recyclerView != null) {
                    recyclerView.layoutManager = LinearLayoutManager(activity)
                    var adapter = KitchenRecipesAdapter(response)
                    recyclerView.adapter = adapter

                    adapter.setOnItemClickListener(object : KitchenRecipesAdapter.OnItemClickListener{

                        override fun onItemClick(position: Int){
                            //Toast.makeText(requireActivity(),"Receta : $position", Toast.LENGTH_SHORT).show()

                            viewModel.getRecipeById(position)

                        }
                    })
                }
            }
        })
    }

    private fun findRecipeViewModel(){
        viewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        viewModel.getRecipeByIdObserver().observe(requireActivity(), androidx.lifecycle.Observer <RecipeModel2?> { response ->
            if(response == null){
                Toast.makeText(requireActivity(), "Error",Toast.LENGTH_LONG).show()
            }else{
                val intent = Intent(requireActivity(), DetailsActivity()::class.java)
                //intent.putExtra("list", response);
                startActivity(intent)
            }
        })
    }
}