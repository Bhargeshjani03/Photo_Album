package com.example.photoalbum.view

import android.content.Intent
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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.photoalbum.R
import com.example.photoalbum.databinding.ActivityUpdateImageBinding
import com.example.photoalbum.model.MyImages
import com.example.photoalbum.util.ConvertImage
import com.example.photoalbum.viewmodel.MyImagesViewMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateImageActivity : AppCompatActivity() {
    lateinit var updateImageBinding: ActivityUpdateImageBinding
    var id=-1
    lateinit var viewMode: MyImagesViewMode
    var imageAsString  = ""
    lateinit var activityResultLauncherForSelectImage: ActivityResultLauncher<Intent>
    lateinit var selectedImage:Bitmap
    var control = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        updateImageBinding = ActivityUpdateImageBinding.inflate(layoutInflater)
        setContentView(updateImageBinding.root)
        viewMode = ViewModelProvider(this)[MyImagesViewMode::class.java]
        getAndSetData()
        //register
        registerActivityForSelectImage()
        updateImageBinding.imageViewUpdateImage.setOnClickListener {
            //access the gallery
            val intent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            //Start Activity for result -> Before Api 30
            activityResultLauncherForSelectImage.launch(intent)
        }
        updateImageBinding.buttonUpdate.setOnClickListener {
            updateImageBinding.buttonUpdate.text="Updating Please Wait"
            updateImageBinding.buttonUpdate.isEnabled = false

            GlobalScope.launch(Dispatchers.IO) {
                val updatedTitle = updateImageBinding.editTextUpdateTitle.text.toString()
                val updatedDescription= updateImageBinding.ediTextUpdateDescription.text.toString()
                if(control)
                {
                    val newImageAsString = ConvertImage.convertToString(selectedImage)
                    if(newImageAsString!=null)
                    {
                        imageAsString=newImageAsString
                    }
                    else
                    {
                        Toast.makeText(applicationContext,"Please Select a valid Image",Toast.LENGTH_LONG).show()
                    }
                }
                val myUpdateImage = MyImages(updatedTitle,updatedDescription,imageAsString)
                myUpdateImage.imageId=id
                viewMode.update(myUpdateImage)
                finish()
            }

        }
        updateImageBinding.toolbarUpdateImage.setNavigationOnClickListener {
            finish()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun getAndSetData()
    {
        id=intent.getIntExtra("id",-1)
        if(id!=-1)
        {
            CoroutineScope(Dispatchers.IO).launch {
              val myImage =  viewMode.getItemById(id)
                withContext(Dispatchers.Main)
                {
                    updateImageBinding.editTextUpdateTitle.setText(myImage.imageTitle)
                     updateImageBinding.ediTextUpdateDescription.setText(myImage.imageDescription)
                    imageAsString= myImage.imageAsString
                    val imageAsBitmap=ConvertImage.convertToBitmap(imageAsString)
                    updateImageBinding.imageViewUpdateImage.setImageBitmap(imageAsBitmap)
                }
            }

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
                    updateImageBinding.imageViewUpdateImage.setImageBitmap(selectedImage)
                    control = true
                }

            }
        }
    }
}