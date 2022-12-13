package com.example.masterfood.ui.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
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
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.masterfood.R
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
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_recipe.*


class RecipeActivity : AppCompatActivity() {

    private lateinit var viewModel: RecipeViewModel

    private lateinit var path : String
    private lateinit var originalUri : Uri
    lateinit var product_Image_name : String

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
            cameraCheckPermission()
        }

        btnGallery2.setOnClickListener {
            galleryCheckPermission()
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
        val recipe = RecipeModel(
            loadUser(),
            etTitle?.text.toString(),
            foodItem,
            Integer.valueOf(etAmount?.text.toString()),
            lvIngredientsList,
            lvUtensilsList,
            lvStepsList,
            difficultItem,
            etNationality?.text.toString())
        viewModel.insertRecipe(recipe)
    }

    private fun getPath(uri : Uri) : String{
        var cursor : Cursor = contentResolver.query(uri, null, null, null,null)!!

        cursor.moveToFirst()
        var document = cursor.getString(0)
        document = document.substring(document.lastIndexOf(":") + 1);
        Log.e("document_id",document)
        cursor.close()

        cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            MediaStore.Images.Media._ID + " = ? ",
            arrayOf(document),
            null)!!
        cursor.moveToFirst()

        path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
        cursor.close()

        return path
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
            originalUri = activityResult.data?.data as Uri
            product_Image_name = "" + getPath(originalUri).toString().substring(getPath(originalUri).toString().lastIndexOf('/')+1)
            val takeFlags: Int =
                activityResult.data!!.flags and (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            contentResolver.takePersistableUriPermission(originalUri,takeFlags)

            Log.e("uri",originalUri.toString())
            imageView2.load(bitmap){
                crossfade(true)
                crossfade(1000)
            }
        }
    }

    /*
    class uploadImageTask : AsyncTask<Void?, Void?, Void?>() {

        var name : String = ""
        var path : String = ""

        override fun onPreExecute() {
            super.onPreExecute()

            name =
            path = path
        }

        override fun doInBackground(vararg p0: Void?): Void? {

        }

    }

     */

}