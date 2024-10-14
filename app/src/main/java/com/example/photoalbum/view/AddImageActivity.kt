package com.example.photoalbum.view

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.photoalbum.R
import com.example.photoalbum.databinding.ActivityAddImageBinding
import com.example.photoalbum.model.MyImages
import com.example.photoalbum.util.ControlPermission
import com.example.photoalbum.util.ConvertImage
import com.example.photoalbum.viewmodel.MyImagesViewMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddImageActivity : AppCompatActivity() {
    lateinit var addImageBinding: ActivityAddImageBinding
    lateinit var activityResultLauncherForSelectImage:ActivityResultLauncher<Intent>
    lateinit var selectedImage: Bitmap
    lateinit var myImagesViewMode: MyImagesViewMode
    var control =  false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        addImageBinding=ActivityAddImageBinding.inflate(layoutInflater)
        setContentView(addImageBinding.root)
        myImagesViewMode = ViewModelProvider(this)[MyImagesViewMode::class.java]
        //register
        registerActivityForSelectImage()
        addImageBinding.imageViewAddImage.setOnClickListener {
            if(ControlPermission.checkPermission(this))
            {
                //access the gallery
                val intent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                //Start Activity for result -> Before Api 30
                activityResultLauncherForSelectImage.launch(intent)

            }
            else
            {
                if(Build.VERSION.SDK_INT>=33)
                {
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),1)

                }
                else
                {
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
                }
            }
        }
        addImageBinding.buttonAdd.setOnClickListener {
            if(control)
            {
                addImageBinding.buttonAdd.text="Uploading Please Wait"
                addImageBinding.buttonAdd.isEnabled = false

                GlobalScope.launch(Dispatchers.IO) {
                    val title = addImageBinding.editTextAddTitle.text.toString()
                    val description = addImageBinding.ediTextAddDescription.text.toString()
                    val imageAsString = ConvertImage.convertToString(selectedImage)
                    if(imageAsString!=null)
                    {
                        myImagesViewMode.insert(MyImages(title,description,imageAsString))
                        control = false
                        finish()
                    }
                    else
                    {
                        Toast.makeText(applicationContext,"Please Select a new image",Toast.LENGTH_LONG).show()
                    }
                }

            }
            else
            {
                Toast.makeText(applicationContext,"Please Select a photo",Toast.LENGTH_LONG).show()
            }

        }
        addImageBinding.toolbarAddImage.setNavigationOnClickListener {
            finish()

        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun registerActivityForSelectImage()
    {
        activityResultLauncherForSelectImage=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            //returns result for intent
            val resultCode= result.resultCode
            val imageData=result.data

            if(resultCode== RESULT_OK && imageData!=null)
            {
                val imageUri = imageData.data
               imageUri?.let {
                   selectedImage = if(Build.VERSION.SDK_INT>=28)
                   {
                       val imageSource= ImageDecoder.createSource(this.contentResolver,it)
                       ImageDecoder.decodeBitmap(imageSource)
                   }
                   else
                   {
                       MediaStore.Images.Media.getBitmap(this.contentResolver,imageUri)
                   }
                   addImageBinding.imageViewAddImage.setImageBitmap(selectedImage)
                   control = true
               }

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==1 && grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED)
        {
            val intent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            //Start Activity for result -> Before Api 30
            activityResultLauncherForSelectImage.launch(intent)
        }
    }
}