package com.example.pairpa.Discussion

data class Comment(val email: String, val content: String, val profile: String, val timestamp: Long = System.currentTimeMillis()) {
    constructor() : this("Filler", "Filler", "Filler")
}