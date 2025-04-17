package com.example.pairpa

//This data class stores the data for a clothing item to be placed in the User's cart
data class CartData(val itemName: String, val price: Int) {
    constructor() : this("", 100)
}