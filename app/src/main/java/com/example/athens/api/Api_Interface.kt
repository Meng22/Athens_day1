package com.example.athens.api

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface Api_Interface {
    //註冊
    @POST("/api/register")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    //登入
    @POST("/api/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    //======================================runner
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



    //=================================station

    //新增貨物
    @POST("/api/goods")
    fun addGoods(@Body addGoodsRequest: AddGoodsRequest): Call<AddGoodsResponse>

    //上傳圖片
    @Multipart
    @POST("/api/image")
    fun uploadImage(@Part photo: MultipartBody.Part, @Part good_id: MultipartBody.Part) : Call<UploadImageResponse>

    //驛站查看商品
    @GET("/api/goods")
    fun stationInfo(): Call<StationGoodsResponse>

}