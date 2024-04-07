package com.example.newsapp.network

import com.example.newsapp.model.NewsResponse
import retrofit2.Call
import retrofit2.http.GET

interface NewsService {

    @GET("Android/news-api-feed/staticResponse.json")
    fun getArticles(): Call<NewsResponse>
}