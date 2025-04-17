package com.example.pairpa.Job

data class JobListing(
    val title: String,
    val company: String,
    val location: String,
    val tags: List<String>,
    val detail: String,
    val timestamp: Long = System.currentTimeMillis()
) {
    constructor() : this("Filler", "Filler", "Filler", listOf("Filler"), "Filler")
}