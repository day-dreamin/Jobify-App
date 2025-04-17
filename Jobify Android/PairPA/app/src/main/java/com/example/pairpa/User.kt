package com.example.pairpa


//This data class contains the data for a User to be stored in Firebase
data class User(val email: String,
                val cart: ArrayList<CartData>,
                val purchases: ArrayList<CartData>,
                val address: String,
                val number: String,
                val job: String,
                val description: String) {
    constructor() : this("Filler", ArrayList(), ArrayList(), "Filler", "Filler", "Filler", "Filler")
}