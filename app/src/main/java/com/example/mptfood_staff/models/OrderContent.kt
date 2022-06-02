package com.example.mptfood_staff.models

data class OrderContent(
    val id: Int,
    val orderId: Int,
    val product: Product,
    val productId: Int,
    val quantity: Int
)