package com.example.mptfood_staff.models

data class Order(
    val client: Any,
    val clientId: Int,
    val date: String,
    val employee: Any,
    val employeeId: Int,
    val id: Int,
    val orderContents: List<OrderContent>,
    val status: Any,
    var statusId: Int,
    val total: Double
)