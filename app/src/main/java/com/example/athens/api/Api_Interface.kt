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

    @GET("/api/preparedTasks")
    fun showGoods(): Call<GoodsResponse>

    //接單
    @POST("/api/tasks")
    fun task(@Body taskRequest: TaskRequest): Call<TasksResponse>

    //顯示接單內容
    @GET("/api/myTask")
    fun myTask(): Call<MyTaskResponse>

    //checkin
    @POST("/api/checkin")
    fun checkin(@Body checkRequest: CheckinRequest): Call<CheckResponse>

    //checkout
    @POST("/api/checkout")
    fun checkout(@Body checkRequest: CheckoutRequest): Call<CheckResponse>

    //cancel
    @POST("/api/statusCancel")
    fun cancel(@Body cancelRequest: CancelRequest): Call<CancelResponse>

    //跑量紀錄
    @GET("/api/medalStatus")
    fun medalStatus(): Call<medalStatusResponse>

    //跑者運送紀錄
    @GET("/api/runnerHistory")
    fun runnerHistory(): Call<HistoryResponse>
}