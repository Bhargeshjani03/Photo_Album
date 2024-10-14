package com.example.photoalbum.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photoalbum.R
import com.example.photoalbum.adapter.MyImagesAdapter
import com.example.photoalbum.databinding.ActivityMainBinding
import com.example.photoalbum.viewmodel.MyImagesViewMode

class MainActivity : AppCompatActivity() {
    lateinit var myImagesViewMode: MyImagesViewMode
    lateinit var mainBinding:ActivityMainBinding
    lateinit var myImagesAdapter: MyImagesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding=ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(mainBinding.root)
        myImagesViewMode = ViewModelProvider(this)[MyImagesViewMode::class.java]
        mainBinding.recyclerView.layoutManager=LinearLayoutManager(this)
        myImagesAdapter= MyImagesAdapter(this)
        mainBinding.recyclerView.adapter=myImagesAdapter
        myImagesViewMode.getAllImages().observe(this, Observer {images->
            //update UI
        myImagesAdapter.setImage(images)

        })
        mainBinding.floatingActionButton.setOnClickListener {
            //Open Add image Activity
            val intent=Intent(this,AddImageActivity::class.java)
            startActivity(intent)
        }
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               myImagesViewMode.delete(myImagesAdapter.returnItemAtGivenPosition(viewHolder.adapterPosition))
            }
        }).attachToRecyclerView(mainBinding.recyclerView)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

}