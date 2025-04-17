package com.example.pairpa

//This data class contains the data for a review made by a user
data class ReviewData(val dispName: String,
                      val content: String,
                      val rating: Float,
                      val added: Boolean) {
    constructor() : this("", "", 5.0f, false)
}