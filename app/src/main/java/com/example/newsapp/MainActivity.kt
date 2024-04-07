package com.example.newsapp

import NewsAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.model.NewsResponse
import com.example.newsapp.network.NewsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NewsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))

        recyclerView = binding.recyclerView
        progressBar = binding.progressBar

        adapter = NewsAdapter(emptyList()) { url ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fetchDataFromApi()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.oldToNew -> {
                sortArticlesByOldToNew()
                true
            }
            R.id.newToOld -> {
                sortArticlesByNewToOld()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun fetchDataFromApi() {
        // Show progress bar
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        val retrofit = Retrofit.Builder()
            .baseUrl("https://candidate-test-data-moengage.s3.amazonaws.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(NewsService::class.java)
        val call = service.getArticles()

        call.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                // Hide progress bar
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE

                if (response.isSuccessful) {
                    val articles = response.body()?.articles ?: emptyList()
                    adapter.articles = articles
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                // Hide progress bar
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE

                t.printStackTrace()
            }
        })
    }

    private fun sortArticlesByOldToNew() {
        adapter.articles = adapter.articles.sortedBy { it.publishedAt }
        adapter.notifyDataSetChanged()
    }

    private fun sortArticlesByNewToOld() {
        adapter.articles = adapter.articles.sortedByDescending { it.publishedAt }
        adapter.notifyDataSetChanged()
    }
}
