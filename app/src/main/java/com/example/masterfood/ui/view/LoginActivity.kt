package com.example.masterfood.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.masterfood.R
import com.example.masterfood.core.DBHelper
import com.example.masterfood.data.model.UserApplication.Companion.prefs
import com.example.masterfood.data.model.UserModel
import com.example.masterfood.ui.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: UserViewModel
    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = DBHelper(this)

        initViewModel()
        btnLogin.setOnClickListener {
            val isValid = validateForm()
            if(isValid){
                /*
                if(db.login(txtUser.text.toString(),txtPass.text.toString())){
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                 */
                val user = UserModel(0,null,null,txtUser.text.toString(),txtPass.text.toString(),null,null)
                viewModel.login(user)

            }
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    private fun validateForm() : Boolean {
        var isValid = true

        with(this){
            if(txtUser.text.toString().isBlank()){
                isValid = false
                txtUser.error = getString(R.string.required)
            }
            if(txtPass.text.toString().isBlank()) {
                isValid = false
                txtPass.error = getString(R.string.required)
            }
        }

        return isValid
    }

    private fun initViewModel(){
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        viewModel.loginObserver().observe(this, androidx.lifecycle.Observer <UserModel?> { response ->
            if(response == null){
                Toast.makeText(this@LoginActivity, "Error", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this@LoginActivity, "Bienvenido ${response.first_name}", Toast.LENGTH_LONG).show()
                prefs.saveUser(response)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        })
    }
}