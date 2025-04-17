package com.example.pairpa.Discussion

data class Post(
    val email: String,
    val date: String,
    val title: String,
    val content: String,
    val comments: Map<String, Comment>?,
    val tags: List<String>,
    val profile: String,
    val timestamp: Long,
    val id: String = ""
) {
    constructor() : this(
        "Filler",
        "Filler",
        "Filler",
        "Filler",
        mutableMapOf(),
        listOf("Filler"),
        "Filler",
        100
    )
}