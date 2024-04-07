package com.example.newsapp.model

data class NewsResponse(
    val status: String,
    val articles: List<Article>
)
