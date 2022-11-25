package com.example.masterfood.ui.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.CircleCropTransformation
import com.example.masterfood.R
import com.example.masterfood.core.DBHelper
import com.example.masterfood.core.ImageUtilities.getByteArrayFromBitmap
import com.example.masterfood.data.model.ApiResponse
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
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var db: DBHelper
    private lateinit var viewModel: UserViewModel

    private val responseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ activityResult ->
        if(activityResult.resultCode == RESULT_OK){
            val bitmap = activityResult.data?.extras!!["data"] as Bitmap?
            ivProfile.load(bitmap){
                crossfade(true)
                crossfade(1000)
                transformations(CircleCropTransformation())
            }
        }
    }

    private val responseLauncher2 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ activityResult ->
        if(activityResult.resultCode == RESULT_OK){
            val bitmap = activityResult.data?.data
            ivProfile.load(bitmap){
                crossfade(true)
                crossfade(1000)
                transformations(CircleCropTransformation())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        db = DBHelper(this)

        initViewModel()
        btnRegisterActivity.setOnClickListener {
            val isValid = validateForm()
            if(isValid){
                //db.insertUser(txtName.text.toString(),txtLastname.text.toString(),txtEmail.text.toString(),txtPassword.text.toString(),getByteArrayFromBitmap(ivProfile.drawable.toBitmap()))
                insert()
            }
        }

        btnCamera.setOnClickListener {
            cameraCheckPermission()
        }

        btnGallery.setOnClickListener {
            galleryCheckPermission()
        }

    }

    private fun cameraCheckPermission(){
        Dexter.withContext(this)
            .withPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA).withListener(
                object : MultiplePermissionsListener{
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
        ).withListener(object : PermissionListener{
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                gallery()
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(
                    this@RegisterActivity,
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

    private fun validatePass(pass: String): Boolean {
        val pattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}\$")
        return pattern.containsMatchIn(pass)
    }

    private fun insert() {

        val encodedString:String =  Base64.getEncoder().encodeToString(getByteArrayFromBitmap(ivProfile.drawable.toBitmap()))
        val strEncodeImage = "data:image/png;base64,$encodedString"
        val user = UserModel(0,txtName.text.toString(),txtLastname.text.toString(),txtEmail.text.toString(),txtPassword.text.toString(),strEncodeImage)
        viewModel.insertUser(user)

    }

    private fun initViewModel(){
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        viewModel.insertUserObserver().observe(this, androidx.lifecycle.Observer <ApiResponse?> { response ->
            if(response == null){
                Toast.makeText(this@RegisterActivity, "Error ${response?.message}",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this@RegisterActivity, "${response.message}",Toast.LENGTH_LONG).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        })
    }
}


