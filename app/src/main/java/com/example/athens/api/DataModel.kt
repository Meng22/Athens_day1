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
    val message: String,
    val data: List<GoodsData>
)
data class GoodsData(
    val good_name: String,
    val created_at: String,
    val des_station_id: Int,
    val good_id: Int,
    val id: Int,
    val runner_id: Any,
    val start_station_id: Int,
    val status: String,
    val updated_at: String
)

data class TaskRequest(
    val shipment_id: Int
)
data class TasksResponse(
    val created_at: String,
    val des_station_id: Int,
    val good_id: Int,
    val good_name: String,
    val id: Int,
    val runner_id: Int,
    val start_station_id: Int,
    val status: String,
    val updated_at: String
)
data class CheckinRequest(
    val start_station_name: String
)

data class CheckoutRequest(
    val des_station_name: String
)
data class CheckResponse(
    val data: ShipmentData?,
    val message: String
)

data class ShipmentData(
    val created_at: String,
    val des_station_id: Int,
    val good_id: Int,
    val good_name: String,
    val id: Int,
    val runner_id: Int,
    val start_station_id: Int,
    val status: String,
    val updated_at: String
)