package com.example.pairpa

//This data class contains the data for a single purchase made by the user
data class Purchase(val name: String, val price: Int, val date: String) {
    constructor() : this("", 0, "")
}