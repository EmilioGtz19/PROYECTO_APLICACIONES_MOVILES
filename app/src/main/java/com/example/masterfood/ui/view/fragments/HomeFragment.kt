package com.example.masterfood.ui.view.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.masterfood.*
import com.example.masterfood.core.ImageUtilities
import com.example.masterfood.ui.adapter.KitchenRecipesAdapter
import com.example.masterfood.data.model.KitchenRecipesProvider
import com.example.masterfood.data.model.UserApplication.Companion.prefs
import com.example.masterfood.data.model.UserModel
import com.example.masterfood.ui.view.LoginActivity
import com.example.masterfood.ui.view.RegisterActivity
import kotlinx.android.synthetic.main.fragment_home.*
import com.example.masterfood.core.ImageUtilities.getBitMapFromByteArray


class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerKitchenRecipes)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = KitchenRecipesAdapter(KitchenRecipesProvider.kitchenRecipes)
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

}