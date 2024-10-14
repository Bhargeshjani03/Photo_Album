package com.example.photoalbum.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.photoalbum.model.MyImages
import com.example.photoalbum.viewmodel.MyImagesViewMode

@Dao
interface MyImagesDao {
    @Insert
    suspend fun insert(myImages: MyImages)
    @Update
    suspend fun update(myImages: MyImages)
    @Delete
    suspend fun delete(myImages: MyImages)
    @Query("SELECT * FROM  my_images ORDER BY imageId asc")
    fun getAllImages() : LiveData<List<MyImages>>
    @Query("SELECT * FROM my_images WHERE imageId = :id")
    suspend fun getItemById(id:Int) : MyImages

}