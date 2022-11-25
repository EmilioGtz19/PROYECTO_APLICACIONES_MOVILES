package com.example.masterfood.ui.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.CircleCropTransformation
import com.example.masterfood.R
import com.example.masterfood.core.ImageUtilities
import com.example.masterfood.data.model.ApiResponse
import com.example.masterfood.data.model.UserApplication
import com.example.masterfood.data.model.UserApplication.Companion.prefs
import com.example.masterfood.data.model.UserModel
import com.example.masterfood.ui.viewmodel.UserViewModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.txtEmail
import kotlinx.android.synthetic.main.activity_register.txtLastname
import kotlinx.android.synthetic.main.activity_register.txtName
import java.util.*

class EditProfileActivity : AppCompatActivity() {

    private val responseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ activityResult ->
        if(activityResult.resultCode == RESULT_OK){
            val bitmap = activityResult.data?.extras!!["data"] as Bitmap?
            imageView3.load(bitmap){
                crossfade(true)
                crossfade(1000)
                transformations(CircleCropTransformation())
            }
        }
    }

    private val responseLauncher2 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ activityResult ->
        if(activityResult.resultCode == RESULT_OK){
            val bitmap = activityResult.data?.data
            imageView3.load(bitmap){
                crossfade(true)
                crossfade(1000)
                transformations(CircleCropTransformation())
            }
        }
    }

    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val btnReturn = findViewById<Button>(R.id.btnReturn)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        val txtName = findViewById<EditText>(R.id.txtName)
        val txtLastName = findViewById<EditText>(R.id.txtLastname)
        val txtEmail = findViewById<EditText>(R.id.txtEmail)
        val img = findViewById<ImageView>(R.id.imageView3)

        val id = this.loadPreference(txtName,txtLastName,txtEmail,img)

        initViewModel()

        btnCameraEdit.setOnClickListener {
            cameraCheckPermission()
        }

        btnGalleryEdit.setOnClickListener {
            galleryCheckPermission()
        }

        btnUpdate.setOnClickListener {
            if (id != null && validateForm()) {
                this.update(id,txtName.text.toString(),txtLastName.text.toString(),txtEmail.text.toString())
            }
        }

        btnReturn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }

    private fun validateForm() : Boolean{
        var isValid = true
        with(this){
            if(txtName.text.toString().isBlank()){
                isValid = false
                txtName.error = getString(R.string.required)
            }
            if(txtLastname.text.toString().isBlank()){
                isValid = false
                txtLastname.error = getString(R.string.required)
            }
            if(txtEmail.text.toString().isBlank()){
                isValid = false
                txtEmail.error = getString(R.string.required)
            }
        }
        return isValid
    }

    private fun update(id : Int, name: String, last_name : String, email : String) {

        val encodedString:String =  Base64.getEncoder().encodeToString(
            ImageUtilities.getByteArrayFromBitmap(
                imageView3.drawable.toBitmap()
            )
        )

        val user = UserModel(id,name,last_name,email,null,encodedString)
        viewModel.updateUser(user)
        prefs.saveUser(user)

    }

    private fun initViewModel(){
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        viewModel.updateUserObserver().observe(this, androidx.lifecycle.Observer <ApiResponse?> { response ->
            if(response == null){
                Toast.makeText(this@EditProfileActivity, "Error", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this@EditProfileActivity, "${response.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun loadPreference(txtUserName : EditText, txtLastname : EditText, txtEmail : EditText, img : ImageView) : Int? {
        val credential: UserModel = prefs.getUser()
        if (credential.first_name != "No information") {
            txtUserName.setText(credential.first_name)
            txtLastname.setText(credential.last_name)
            txtEmail.setText(credential.email)
            img.setImageBitmap(ImageUtilities.getBitMapFromByteArray(credential.user_photo))
        }
        return credential.user_id
    }

    private fun cameraCheckPermission(){
        Dexter.withContext(this)
            .withPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA).withListener(
                object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if(report.areAllPermissionsGranted()){
                                camera()
                            }
                        }
                    }
                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?
                    ) {
                        showRotationalDialogForPermission()
                    }

                }
            ).onSameThread().check()
    }

    private fun galleryCheckPermission() {
        Dexter.withContext(this).withPermission(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object : PermissionListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                gallery()
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(
                    this@EditProfileActivity,
                    getString(R.string.permissio_denied),
                    Toast.LENGTH_SHORT
                ).show()

                showRotationalDialogForPermission()
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?,
                p1: PermissionToken?
            ) {
                showRotationalDialogForPermission()
            }
        }).onSameThread().check()
    }

    private fun showRotationalDialogForPermission(){
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.enable_permissions))
            .setPositiveButton(getString(R.string.go_to_configuration)){ _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package",packageName,null)
                    intent.data = uri
                    startActivity(intent)

                }catch(e: ActivityNotFoundException){
                    e.printStackTrace()
                }
            }
            .setNegativeButton(getString(R.string.cancel)){ dialog, _->
                dialog.dismiss()
            }.show()
    }

    private fun camera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        responseLauncher.launch(intent)
    }

    private fun gallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        responseLauncher2.launch(intent)
    }
}