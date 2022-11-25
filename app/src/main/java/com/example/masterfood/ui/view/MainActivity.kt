package com.example.masterfood.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.masterfood.R
import com.example.masterfood.data.model.UserApplication
import com.example.masterfood.data.model.UserApplication.Companion.prefs
import com.example.masterfood.data.model.UserModel
import com.example.masterfood.ui.view.fragments.HomeFragment
import com.example.masterfood.ui.view.fragments.MyRecipesFragment
import com.example.masterfood.ui.view.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val homeFragment = HomeFragment()
    private val myRecipesFragment = MyRecipesFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(homeFragment)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val credentials: UserModel = prefs.getUser()

        if(credentials.first_name != "No information") {
            bottomNavigation.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.ic_home -> replaceFragment(homeFragment)
                    R.id.ic_book -> replaceFragment(myRecipesFragment)
                    R.id.ic_person -> replaceFragment(profileFragment)
                }
                true
            }
        }else{
            bottomNavigation.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.ic_book -> Toast.makeText(this@MainActivity, "No has iniciado sesion", Toast.LENGTH_LONG).show()
                    R.id.ic_person -> Toast.makeText(this@MainActivity, "No has iniciado sesion", Toast.LENGTH_LONG).show()
                }
                true
            }
        }

    }

    private fun  replaceFragment(fragment: Fragment){

        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }

    }

}