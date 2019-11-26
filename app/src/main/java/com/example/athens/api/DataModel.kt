package com.example.athens.api

data class RegisterRequest(
    val username: String,
    val password: String,
    val role: String
)

data class RegisterResponse(
    val message: String
)

data class LoginRequest(
    val password: String,
    val username: String
)
data class LoginResponse(
    val data: UserData,
    val message: String
)

data class UserData(
    val api_token: String,
    val created_at: String,
    val email_verified_at: Any,
    val id: Int,
    val role_id: Int,
    val updated_at: String,
    val username: String
)

data class GoodsResponse(
    val data: List<GoodsData>,
    val message: String
)

data class GoodsData(
    val created_at: String,
    val des_station: Station,
    val des_station_id: Int,
    val description: String,
    val id: Int,
    val name: String,
    val now_station: Station,
    val now_station_id: Int,
    val price: Int,
    val start_station: Station,
    val start_station_id: Int,
    val status: String,
    val updated_at: String,
    val weight: Int
)

data class Station(
    val created_at: String,
    val id: Int,
    val name: String,
    val updated_at: String
)

data class Goods(
    val name: String,
    val des_station: String,
    val status: String,
    val weight: Int
)