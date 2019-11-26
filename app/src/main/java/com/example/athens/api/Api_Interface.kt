package com.example.athens.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api_Interface {
    @POST("/api/register")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @POST("/api/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("/api/goods")
    fun showGoods(): Call<GoodsResponse>
}