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

    @GET("/api/tasklist")
    fun showGoods(): Call<GoodsResponse>

    //接單
    @POST("/api/tasks")
    fun task(@Body taskRequest: TaskRequest): Call<TasksResponse>

    //接單內容
    @GET("/api/myTask")
    fun myTask(): Call<TasksResponse>

    //checkin
    @POST("/api/checkin")
    fun checkin(@Body checkRequest: CheckinRequest): Call<CheckResponse>

    //checkout
    @POST("/api/checkout")
    fun checkout(@Body checkRequest: CheckoutRequest): Call<CheckResponse>
}