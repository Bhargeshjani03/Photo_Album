package com.example.photoalbum.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.photoalbum.model.MyImages
import com.example.photoalbum.room.MyImagesDao
import com.example.photoalbum.room.MyImagesDatabase

class MyImagesRepository(application: Application) {
    var myImagesDao:MyImagesDao
    var images_list:LiveData<List<MyImages>>

    init {
        val database=MyImagesDatabase.getDatabaseInstance(application)
        myImagesDao=database.myImagesDao()
        images_list=myImagesDao.getAllImages()
    }
    suspend fun insert(myImages: MyImages)
    {
        myImagesDao.insert(myImages)
    }
    suspend fun update(myImages: MyImages)
    {
        myImagesDao.update(myImages)
    }
    suspend fun delete(myImages: MyImages)
    {
        myImagesDao.delete(myImages)
    }
    fun getAllImages():LiveData<List<MyImages>>
    {
        return images_list
    }
    suspend fun getItemById(id:Int):MyImages
    {
        return myImagesDao.getItemById(id)
    }
}