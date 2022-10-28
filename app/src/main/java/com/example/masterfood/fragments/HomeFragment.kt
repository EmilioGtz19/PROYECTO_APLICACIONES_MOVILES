package com.example.masterfood.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.masterfood.EditProfileActivity
import com.example.masterfood.KitchenRecipesProvider
import com.example.masterfood.R
import com.example.masterfood.RegisterActivity
import com.example.masterfood.adapter.KitchenRecipesAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_home, container, false)
        val btn = view.findViewById<Button>(R.id.button)

        btn.setOnClickListener{
            val intent = Intent(requireActivity(), RegisterActivity::class.java)
            requireActivity().startActivity(intent)
        }

        initRecyclerView(view)
        // Inflate the layout for this fragment
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun initRecyclerView(view : View){
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerKitchenRecipes)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = KitchenRecipesAdapter(KitchenRecipesProvider.kitchenRecipes)
    }
}