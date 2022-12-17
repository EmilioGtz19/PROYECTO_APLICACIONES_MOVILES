package com.example.masterfood.ui.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.masterfood.R
import com.example.masterfood.core.ImageUtilities.getByteArrayFromBitmap
import com.example.masterfood.data.model.ApiResponse
import com.example.masterfood.data.model.RecipeModel
import com.example.masterfood.data.model.UserApplication
import com.example.masterfood.data.model.UserModel
import com.example.masterfood.ui.viewmodel.RecipeViewModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_recipe.*
import kotlinx.android.synthetic.main.activity_recipe.btnAddSteps
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.dialog_addphotos.*
import java.util.*
import kotlin.collections.ArrayList


class RecipeActivity : AppCompatActivity() {

    private lateinit var viewModel: RecipeViewModel

    private var etTitle : EditText? = null
    private var etAmount : EditText? = null
    private var etNationality : EditText? = null
    private var etIngredients : EditText? = null
    private var etUtensils : EditText? = null
    private var etSteps : EditText? = null

    private var lvIngredients : ListView? = null
    private var lvUtensils : ListView? = null
    private var lvSteps : ListView? = null

    private var lvIngredientsList = ArrayList<String>()
    private var lvIngredientsAdapter : ArrayAdapter<String>?  = null

    private var lvUtensilsList = ArrayList<String>()
    private var lvUtensilsAdapter : ArrayAdapter<String>?  = null

    private var lvStepsList = ArrayList<String>()
    private var lvStepsAdapter : ArrayAdapter<String>?  = null

    private var foodSpinner : Spinner? = null
    private var difficultSpinner : Spinner? = null

    private var foodItem : Int? = null
    private var difficultItem : Int? = null

    private lateinit var mDialogView : View
    private lateinit var mBuilder : AlertDialog.Builder

    private var photoList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        initViewModel()
        etTitle = findViewById(R.id.txtTitle)
        etAmount = findViewById(R.id.txtAmount)
        etNationality = findViewById(R.id.txtNationality)
        etIngredients = findViewById(R.id.txtIngredients)
        etUtensils = findViewById(R.id.txtUtensils)
        etSteps = findViewById(R.id.txtSteps)

        lvIngredients = findViewById(R.id.lvIngredients)
        lvUtensils = findViewById(R.id.lvUtensils)
        lvSteps = findViewById(R.id.lvSteps)

        foodSpinner = findViewById(R.id.food_spinner)
        difficultSpinner = findViewById(R.id.difficulty_spinner)


        lvIngredientsAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            lvIngredientsList
        )

        lvUtensilsAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            lvUtensilsList
        )

        lvStepsAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            lvStepsList
        )

        btnAddIngredient.setOnClickListener {
            getIngredient()
        }

        btnAddUtensils.setOnClickListener {
            getUtensils()
        }

        btnAddSteps.setOnClickListener {
            getSteps()
        }

        btnCamera2.setOnClickListener {
            cameraCheckPermission(1)
        }

        btnGallery2.setOnClickListener {
            galleryCheckPermission(1)
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.food_type,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            foodSpinner?.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.difficulty_type,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            difficultSpinner?.adapter = adapter
        }

        foodSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                foodItem = position
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        difficultSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                difficultItem = position
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        btnUpload.setOnClickListener {
            if(validateForm()) {
                insert()
            }
        }

        btnSteps.setOnClickListener {
            mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_addphotos,null)
            mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle(getString(R.string.list_images))

            val btnCamera = mDialogView.findViewById<Button>(R.id.btnCamera)
            val btnGallery = mDialogView.findViewById<Button>(R.id.btnGallery)
            val btnAdd = mDialogView.findViewById<Button>(R.id.btnAddPhotos)

            btnCamera.setOnClickListener {
                cameraCheckPermission(2)
            }

            btnGallery.setOnClickListener {
                galleryCheckPermission(2)
            }

            btnAdd.setOnClickListener {
                getPhoto()
            }


            mBuilder.show()
        }

    }

    private fun validateForm() : Boolean{
        var isValid = true

        with(this){
            if(etTitle?.text.toString().isBlank()){
                isValid = false
                etTitle?.error = getString(R.string.required)
            }
            if(etAmount?.text.toString().isBlank()){
                isValid = false
                etAmount?.error = getString(R.string.required)
            }
            if(etNationality?.text.toString().isBlank()){
                isValid = false
                etNationality?.error = getString(R.string.required)
            }
            if(lvIngredientsList.isEmpty()){
                isValid = false
                etIngredients?.error = getString(R.string.EmptyIngredientsList)
            }
            if(lvUtensilsList.isEmpty()){
                isValid = false
                etUtensils?.error =  getString(R.string.EmptyUtensilsList)
            }
            if(lvStepsList.isEmpty()){
                isValid = false
                etSteps?.error = getString(R.string.EmptyStepsList)
            }
            if(photoList.isEmpty()){
                isValid = false
                Toast.makeText(this@RecipeActivity, getString(R.string.photolist_empty),Toast.LENGTH_SHORT).show()
            }
        }

        return isValid
    }

    private fun getIngredient(){
        val text = etIngredients?.text.toString().trim()
        lvIngredientsList.add(text)
        lvIngredients?.adapter = lvIngredientsAdapter
        etIngredients?.setText("")
    }

    private fun getUtensils(){
        val text = etUtensils?.text.toString().trim()
        lvUtensilsList.add(text)
        lvUtensils?.adapter = lvUtensilsAdapter
        etUtensils?.setText("")
    }

    private fun getSteps(){
        val text = etSteps?.text.toString().trim()
        lvStepsList.add(text)
        lvSteps?.adapter = lvStepsAdapter
        etSteps?.setText("")
    }

    private fun getPhoto(){
        val encodedString:String =  Base64.getEncoder().encodeToString(getByteArrayFromBitmap(mDialogView.findViewById<ImageView>(R.id.ivPhotoDialog).drawable.toBitmap()))
        val strEncodeImage = "data:image/png;base64,$encodedString"
        photoList.add(strEncodeImage)
        Toast.makeText(this@RecipeActivity, getString(R.string.image_add),Toast.LENGTH_SHORT).show()
    }

    private fun initViewModel(){
        viewModel = ViewModelProvider(this)[RecipeViewModel::class.java]
        viewModel.insertRecipeObserver().observe(this, androidx.lifecycle.Observer <ApiResponse?> { response ->
            if(response == null){
                Toast.makeText(this@RecipeActivity, "Error", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this@RecipeActivity, getString(R.string.recipe_publish), Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        })
    }

    private fun loadUser() : Int? {
        val credential: UserModel = UserApplication.prefs.getUser()
        return credential.user_id
    }

    private fun insert() {
        val encodedString:String =  Base64.getEncoder().encodeToString(getByteArrayFromBitmap(imageView2.drawable.toBitmap()))
        val strEncodeImage = "data:image/png;base64,$encodedString"

        val recipe = RecipeModel(
            loadUser(),
            etTitle?.text.toString(),
            foodItem?.plus(1),
            Integer.valueOf(etAmount?.text.toString()),
            lvIngredientsList,
            lvUtensilsList,
            lvStepsList,
            difficultItem?.plus(1),
            etNationality?.text.toString(),
            strEncodeImage,
            photoList
        )
        viewModel.insertRecipe(recipe)
    }

    private fun cameraCheckPermission(option : Int){
        Dexter.withContext(this)
            .withPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA).withListener(
                object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if(report.areAllPermissionsGranted()){
                                if(option == 1){
                                    camera()
                                }else{
                                    camera2()
                                }

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

    private fun galleryCheckPermission(option : Int) {
        Dexter.withContext(this).withPermission(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object : PermissionListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                if(option == 1){
                    gallery()
                }else{
                    gallery2()
                }
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(
                    this@RecipeActivity,
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

    private fun camera2(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        responseLauncher3.launch(intent)
    }

    private fun gallery2(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        responseLauncher4.launch(intent)
    }

    private val responseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ activityResult ->
        if(activityResult.resultCode == RESULT_OK){
            val bitmap = activityResult.data?.extras!!["data"] as Bitmap?
            imageView2.load(bitmap){
                crossfade(true)
                crossfade(1000)
            }
        }
    }

    private val responseLauncher2 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ activityResult ->
        if(activityResult.resultCode == RESULT_OK){
            val bitmap = activityResult.data?.data
            imageView2.load(bitmap){
                allowHardware(false)
                crossfade(true)
                crossfade(1000)
            }
        }
    }

    private val responseLauncher3 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ activityResult ->
        if(activityResult.resultCode == RESULT_OK){
            val bitmap = activityResult.data?.extras!!["data"] as Bitmap?
            mDialogView.findViewById<ImageView>(R.id.ivPhotoDialog).load(bitmap){
                crossfade(true)
                crossfade(1000)
            }
        }
    }

    private val responseLauncher4 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ activityResult ->
        if(activityResult.resultCode == RESULT_OK){
            val bitmap = activityResult.data?.data
            mDialogView.findViewById<ImageView>(R.id.ivPhotoDialog).load(bitmap){
                allowHardware(false)
                crossfade(true)
                crossfade(1000)
            }
        }
    }
}