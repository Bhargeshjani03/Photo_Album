package com.example.photoalbum.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_images")
class MyImages(
    val imageTitle:String,
    val imageDescription:String,
    //BLOB -> Binary Large Object
    //String -> Base64 Format
    val imageAsString : String
) {
    @PrimaryKey(autoGenerate = true)
    var imageId=0
}