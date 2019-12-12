package com.example.athens.api

data class RegisterRequest(
    val username: String,
    val password: String,
    val role: String
)
data class RegisterResponse(
    val message: String
)
//登入
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
    val username: String,
    val role_name: String
)

//查看各驛站任務
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
    val weight: Float,
    val price: Int,
    val updated_at: String,
    val photo_url: String
)

//接案
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
    val weight: Float
)

//查看我的案子
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
    val weight: Float,
    val photo_url: String

)



//checkin/checkout
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
    val weight: Float,
    val price: Int,
    val updated_at: String,
    val photo_url: String
)

//個人成就
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

//個人歷史跑量
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
    val weight: Double,
    val start_station_name: String,
    val des_station_name: String,
    val distance: Int
    )

//註銷任務
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
    val weight: Double
)

//上傳任務
data class AddGoodsRequest(
    val name: String,
    val description: String,
    val price: Int,
    val start_station_name: String,
    val des_station_name: String,
    val weight: Int
)
data class AddGoodsResponse(
    val data: GoodData,
    val message: String
)
data class GoodData(
    val des_station_id: Int,
    val description: String,
    val id: Int,
    val name: String,
    val now_station_id: Int,
    val photo_url: String,
    val price: String,
    val start_station_id: Int,
    val status: String,
    val weight: String
)

//上傳圖片
data class UploadImageRequest(
    val good_id: Int
)
data class UploadImageResponse(
    val message: String
)


//驛站物品
data class StationGoodsResponse(
    val data: List<StationGoods>,
    val message: String
)
data class StationGoods(
    val created_at: String,
    val des_station: DesStation,
    val des_station_id: Int,
    val description: String,
    val id: Int,
    val name: String,
    val now_station: NowStation,
    val now_station_id: Int,
    val photo_url: String,
    val price: Int,
    val start_station: StartStation,
    val start_station_id: Int,
    val status: String,
    val updated_at: String,
    val weight: Float
)
data class DesStation(
    val created_at: String,
    val id: Int,
    val name: String,
    val updated_at: String
)
data class NowStation(
    val created_at: String,
    val id: Int,
    val name: String,
    val updated_at: String
)
data class StartStation(
    val created_at: String,
    val id: Int,
    val name: String,
    val updated_at: String
)

//驛站等級
data class StationLevelResponse(
    val data: List<StationDetails>
)
data class StationDetails(
    val created_at: String,
    val id: Int,
    val income: Int,
    val level: Int,
    val name: String,
    val totalWeight: Float,
    val updated_at: String
)