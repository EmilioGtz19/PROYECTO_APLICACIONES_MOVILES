package com.example.masterfood.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.masterfood.R
import com.example.masterfood.data.model.ApiResponse
import com.example.masterfood.data.model.UserApplication
import com.example.masterfood.data.model.UserModel
import com.example.masterfood.ui.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_edit_pass.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.txtConfirmPass
import kotlinx.android.synthetic.main.activity_register.txtPassword

class EditPassActivity : AppCompatActivity() {

    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_pass)

        val btnReturn = findViewById<Button>(R.id.btnReturn)
        val btnUpdatePass = findViewById<Button>(R.id.btnUpdatePass)

        initViewModel()

        btnUpdatePass.setOnClickListener {
            if(validateForm()){
                val id = this.loadPreference()
                updatePass(id!!,txtPassword.text.toString())
            }
        }

        btnReturn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updatePass(id : Int, pass: String) {

        val user = UserModel(id,null,null,null,pass)
        viewModel.updatePass(user)

    }

    private fun initViewModel(){
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        viewModel.updatePassObserver().observe(this, androidx.lifecycle.Observer <ApiResponse?> { response ->
            if(response == null){
                Toast.makeText(this@EditPassActivity, "Error", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this@EditPassActivity, "${response.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun validatePass(pass: String): Boolean {
        val pattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}\$")
        return pattern.containsMatchIn(pass)
    }

    private fun validateForm() : Boolean{
        var isValid = true
        with(this){
            if(!validatePass(txtPassword.text.toString())){
                isValid = false
                txtPassword.error = getString(R.string.rulesPass)
            }
            if(txtConfirmPass.text.toString().isBlank()){
                isValid = false
                txtConfirmPass.error = getString(R.string.required)
            }
            if(txtConfirmPass.text.toString() != txtPassword.text.toString()){
                isValid = false
                txtConfirmPass.error = getString(R.string.passnotmatch)
            }
        }
        return isValid
    }

    private fun loadPreference() : Int? {
        val credential: UserModel = UserApplication.prefs.getUser()
        return credential.user_id
    }

}