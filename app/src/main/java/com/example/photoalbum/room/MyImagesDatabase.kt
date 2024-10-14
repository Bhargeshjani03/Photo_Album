package com.example.photoalbum.room
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.photoalbum.model.MyImages

//1->annotation
//2->abstract class
//3-> abstract method returns dao instance
@Database(entities = [MyImages::class], version = 1)
 abstract class MyImagesDatabase : RoomDatabase() {
     abstract fun myImagesDao():MyImagesDao
     //singleton design pattern
     companion object{
         @Volatile
         private var instance:MyImagesDatabase?=null

         fun getDatabaseInstance(context: Context) : MyImagesDatabase{
             synchronized(this){
                 if(instance==null)
                 {
                     instance=Room.databaseBuilder(context.applicationContext,MyImagesDatabase::class.java,"my_album").build()
                 }
                 return instance as MyImagesDatabase
             }

         }
     }
}