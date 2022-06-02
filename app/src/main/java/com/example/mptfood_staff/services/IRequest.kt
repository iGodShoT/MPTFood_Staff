package com.example.mptfood_staff.services

import com.example.mptfood_staff.models.Order
import com.example.mptfood_staff.models.Product
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface IRequest {

    @GET("orders/{id}")
    fun getOrder(@Path("id") id : String) : Call<Order>

    @GET("products/{id}")
    fun getProduct(@Path("id") id : String) : Call<Product>

    @PUT("orders/{id}")
    fun changeStatus(@Path("id") id : String, @Body order : Order) : Call<Order>
}