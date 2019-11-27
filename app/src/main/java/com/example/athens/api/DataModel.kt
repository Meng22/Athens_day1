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
    val weight: Int,
    val price: Int,
    val updated_at: String,
    val photo_url: String
)

data class TaskRequest(
    val shipment_id: Int
)
data class TasksResponse(
    val data: TaskData?,
    val message: String
)
data class TaskData(
    val created_at: String,
    val des_station_id: Int,
    val des_station_name: String,
    val good_id: Int,
    val good_name: String,
    val id: Int,
    val photo_url: Any,
    val price: Int,
    val runner_id: Int,
    val start_station_id: Int,
    val start_station_name: String,
    val status: String,
    val updated_at: String,
    val weight: Int
)

data class MyTaskResponse(
    val created_at: String,
    val des_station_id: Int,
    val good_id: Int,
    val good_name: String,
    val id: Int,
    val price: Int,
    val runner_id: Int,
    val start_station_id: Int,
    val status: String,
    val updated_at: String,
    val weight: Int,
    val photo_url: String

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
    val weight: Int,
    val price: Int,
    val updated_at: String,
    val photo_url: String
)

data class medalStatusResponse(
    val data: medalData,
    val message: String
)

data class medalData(
    val badge_id: Int,
    val badge_name: String,
    val created_at: String,
    val distance: Int,
    val id: Int,
    val runner_id: Int,
    val updated_at: String
)

data class HistoryResponse(
    val data: List<HistoryData>,
    val message: String
)
data class HistoryData(
    val created_at: String,
    val des_station_id: Int,
    val good_id: Int,
    val good_name: String,
    val id: Int,
    val price: Int,
    val runner_id: Int,
    val start_station_id: Int,
    val status: String,
    val updated_at: String,
    val weight: Int,
    val start_station_name: String,
    val des_station_name: String
    )

data class CancelRequest(
    val shipment_id: Int
)
data class CancelResponse(
    val data: List<CancelData>?,
    val message: String
)

data class CancelData(
    val created_at: String,
    val des_station_id: Int,
    val des_station_name: String,
    val good_id: Int,
    val good_name: String,
    val id: Int,
    val photo_url: Any,
    val price: Int,
    val runner_id: Int,
    val start_station_id: Int,
    val start_station_name: String,
    val status: String,
    val updated_at: String,
    val weight: Int
)