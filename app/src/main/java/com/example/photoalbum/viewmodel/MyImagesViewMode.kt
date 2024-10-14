package com.example.photoalbum.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.photoalbum.model.MyImages
import com.example.photoalbum.repository.MyImagesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyImagesViewMode(application: Application) : AndroidViewModel(application) {
    var repository:MyImagesRepository
    var images_list:LiveData<List<MyImages>>
    init {
        repository= MyImagesRepository(application)
        images_list = repository.getAllImages()
    }
    fun insert(myImages: MyImages) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(myImages)
    }
    fun update(myImages: MyImages) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(myImages)
    }
    fun delete(myImages: MyImages) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(myImages)
    }
    fun getAllImages() : LiveData<List<MyImages>>
    {
        return images_list
    }
    suspend fun getItemById(id:Int):MyImages
    {
        return repository.getItemById(id)
    }
}