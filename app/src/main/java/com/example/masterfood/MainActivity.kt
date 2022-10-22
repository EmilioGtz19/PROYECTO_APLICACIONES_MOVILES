package com.example.masterfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.masterfood.fragments.HomeFragment
import com.example.masterfood.fragments.MyRecipesFragment
import com.example.masterfood.fragments.ProfileFragment
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

         bottomNavigation.setOnItemSelectedListener{
            when(it.itemId){
                R.id.ic_home -> replaceFragment(homeFragment)
                R.id.ic_book -> replaceFragment(myRecipesFragment)
                R.id.ic_person -> replaceFragment(profileFragment)
            }
            true
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