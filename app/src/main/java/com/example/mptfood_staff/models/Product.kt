package com.example.mptfood_staff.models

data class Product(
    val id: Int,
    val image: String,
    val name: String,
    val price: Int,
    val quantityAvailable: Int
)