package com.example.photoalbum.adapter

import android.app.Activity
import android.content.Intent
import android.provider.Telephony.Mms.Intents
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.photoalbum.databinding.ImageItemBinding
import com.example.photoalbum.model.MyImages
import com.example.photoalbum.util.ConvertImage
import com.example.photoalbum.view.UpdateImageActivity
import java.util.ArrayList

// MyImagesAdapter: Adapter class for displaying images in a RecyclerView
class MyImagesAdapter(val activity:Activity) : RecyclerView.Adapter<MyImagesAdapter.MyImagesViewHolder>() {

    // imageList stores the list of images to be displayed in the RecyclerView
    var imageList: List<MyImages> = ArrayList()

    // setImage method updates the adapter's image list and refreshes the UI
    fun setImage(images: List<MyImages>) {
        this.imageList = images
        notifyDataSetChanged()  // Notify the adapter that data has changed, so the UI updates
    }

    // ViewHolder class that holds the view elements for each item in the RecyclerView
    class MyImagesViewHolder(val itemBinding: ImageItemBinding) : RecyclerView.ViewHolder(itemBinding.root)

    // Called when RecyclerView needs a new ViewHolder to represent an item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyImagesViewHolder {
        // Inflate the custom layout for each item using data binding
        val view = ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyImagesViewHolder(view)  // Return a new ViewHolder instance
    }

    // Returns the number of items in the image list
    override fun getItemCount(): Int {
        return imageList.size  // The size of the list determines the number of items in RecyclerView
    }

    // Called by RecyclerView to display data at the specified position
    override fun onBindViewHolder(holder: MyImagesViewHolder, position: Int) {
        // Get the image item at the current position in the list
        val myImage = imageList[position]
        // Use 'with' to apply changes to the holder (ViewHolder)
        with(holder) {
            // Set the title of the image in the TextView
            itemBinding.textViewTitle.text = myImage.imageTitle
            // Set the description of the image in the TextView
            itemBinding.textViewDescription.text = myImage.imageDescription
            // Convert the image string to a Bitmap and set it in the ImageView
            val imageAsBitmap = ConvertImage.convertToBitmap(myImage.imageAsString)
            itemBinding.imageView.setImageBitmap(imageAsBitmap)
            itemBinding.cardView.setOnClickListener {
                val intent=Intent(activity,UpdateImageActivity::class.java)
                intent.putExtra("id",myImage.imageId)
                activity.startActivity(intent)
            }
        }

    }
    fun returnItemAtGivenPosition(position: Int) : MyImages
    {
        return imageList[position]
    }
}
