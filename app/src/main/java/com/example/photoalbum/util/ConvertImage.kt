package com.example.photoalbum.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import kotlin.io.encoding.Base64

class ConvertImage {

    companion object{
        fun convertToString(bitmap:Bitmap) : String?
        { val stream = ByteArrayOutputStream()
          val resultCompress=  bitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
            if(resultCompress)
            {
                val byteArray= stream.toByteArray()
                val imageAsString =  if(byteArray.size> 2000000)
                {
                    resizeImage(bitmap,0.1)
                }
                else if(byteArray.size in 1000000..2000000)
                {
                    resizeImage(bitmap,0.5)
                }
                else
                {
                    android.util.Base64.encodeToString(byteArray,android.util.Base64.DEFAULT)
                }
                return imageAsString
            }
            else
            {
                return null
            }
        }
        fun resizeImage(bitmap: Bitmap,coefficient:Double) : String?
        {
            val resizedBitmap = Bitmap.createScaledBitmap(bitmap,(bitmap.width * coefficient).toInt(),(bitmap.height * coefficient).toInt(),true)
            val newStream = ByteArrayOutputStream()
            val resultCompress= resizedBitmap.compress(Bitmap.CompressFormat.PNG,100,newStream)
            if(resultCompress)
            {
                val newByteArray = newStream.toByteArray()
                return android.util.Base64.encodeToString(newByteArray,android.util.Base64.DEFAULT)
            }
            else
            {
                return null
            }
        }
        fun convertToBitmap(imageAsString: String) : Bitmap
        {
            val byteArrayAsDecodedString=android.util.Base64.decode(imageAsString,android.util.Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(byteArrayAsDecodedString,0,byteArrayAsDecodedString.size)
            return bitmap
        }
    }
}