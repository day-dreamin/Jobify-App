package com.example.pairpa.feedReader

data class Post(
    val id: Int,
    val title: String,
    val content: String,
    val comments: List<Comment>,
    val community: Community
)

data class Comment(
    val id: Int,
    val user: String,
    val content: String
)

data class Community(
    val id: Int,
    val name: String,
    val description: String
)