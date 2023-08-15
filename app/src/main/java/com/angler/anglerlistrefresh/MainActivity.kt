package com.angler.anglerlistrefresh

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var customAdapter: CustomImageAdapter
    private val batchSize = 5 // Number of images to load at a time
    private var startIndex = 0
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadingProgressBar = findViewById(R.id.loadingProgressBar)
        databaseHelper = DatabaseHelper(this)
        databaseHelper.clearImages()
        val imagePaths = arrayOf(
            R.drawable.angler1, R.drawable.angler2, /* Add all 20 image resource IDs here */
            R.drawable.angler3, R.drawable.angler4,
            R.drawable.angler5, R.drawable.angler6,
            R.drawable.angler7, R.drawable.angler8,
            R.drawable.angler9, R.drawable.angler10,
            R.drawable.angler11, R.drawable.angler12,
            R.drawable.angler13, R.drawable.angler14,
            R.drawable.angler15, R.drawable.angler16,
            R.drawable.angler17, R.drawable.angler18,
            R.drawable.angler19, R.drawable.angler20,
        )


        for (resourceId in imagePaths) {
            databaseHelper.insertImage(resourceId)
        }

        val initialImages = databaseHelper.getImagesInBatch(startIndex, batchSize)
        val mutableInitialImages = initialImages.toMutableList()
        val swipeRefreshLayout: SwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        val listView: ListView = findViewById(R.id.listView)

        customAdapter = CustomImageAdapter(this, mutableListOf())
        listView.adapter = customAdapter
        loadMoreImages()

        swipeRefreshLayout.setOnRefreshListener {
            // Clear the existing data and load initial images again
            customAdapter.clear()
            startIndex = 0
            loadInitialImages()
            swipeRefreshLayout.isRefreshing = false // Complete the refresh animation
        }

        listView.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {}

            override fun onScroll(
                view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int
            ) {
                val lastVisibleItem = firstVisibleItem + visibleItemCount
                if (!isLoading && lastVisibleItem >= totalItemCount - 1) {
                    loadMoreImages()
                    Log.d("Scroll", "Last Visible Item: $lastVisibleItem, Total Item Count: $totalItemCount")
                }
            }
        })

        loadInitialImages()

    }

    private fun loadInitialImages() {
        val initialImages = databaseHelper.getImagesInBatch(startIndex, batchSize)
        if (initialImages.isNotEmpty()) {
            customAdapter.addAll(initialImages)
            startIndex += batchSize
        }
    }

    private fun loadMoreImages() {
        if (isLoading) {
            return // Already loading, prevent multiple loading requests
        }

        isLoading = true // Set isLoading to true to indicate a loading operation is starting
        loadingProgressBar.visibility = View.VISIBLE // Show the loading progress

        // Introduce a 5-second delay using a Handler
        Handler().postDelayed({
            // Fetch new images
            val newImages = databaseHelper.getImagesInBatch(startIndex, batchSize)
            if (newImages.isNotEmpty()) {
                customAdapter.addAll(newImages)
                startIndex += batchSize
                Log.d("LoadMore", "Loaded ${newImages.size} more images")
            } else {
                // No more images to load
                Log.d("LoadMore", "No more images to load")
            }

            loadingProgressBar.visibility = View.GONE // Hide the loading progress
            isLoading = false // Set isLoading back to false as the loading operation is complete
        }, 3000) // 5000 milliseconds (5 seconds)
    }




}
