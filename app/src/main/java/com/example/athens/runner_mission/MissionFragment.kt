package com.example.athens.runner_mission


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.athens.main.RunnerActivity
import com.example.athens.R
import com.example.athens.api.*

import com.uuzuche.lib_zxing.activity.CaptureActivity
import com.uuzuche.lib_zxing.activity.CodeUtils
import kotlinx.android.synthetic.main.fragment_mission.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MissionFragment : Fragment() {
    companion object {
        private const val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }
    private var isEnter = false

    private var shipmentName = "無訂單"
    private var shipmentStart = "無"
    private var shipmentDes = "無"
    private var shipmentStatus = "無"
    private var shipmentPrice = "無"
    private var shipmentId = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mission, container, false)
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        if (enter && !isEnter){
            isEnter = true

            //初始畫面
            initial()
            com.example.athens.main.println("======刷新畫面")

        }else{
            isEnter = false
        }
        return super.onCreateAnimation(transit, enter, nextAnim)
    }

    override fun onPause() {
        super.onPause()
        isEnter = false

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //初始畫面
        initial()

        //相機權限
        cameraPermission()

        //載入跑者目前訂單
        myTask()

        //掃描
        qr_code.setOnClickListener {
            val intent = Intent(this.context, CaptureActivity::class.java)
            startActivityForResult(intent,1)
        }
        //求救電話
        btn_sos.setOnClickListener {
            val dial = "119"
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", dial, null))
            startActivity(intent)
        }

        //註銷訂單
        btn_alert.setOnClickListener {
            val condition_list = arrayOf("出了車禍要去醫院","貨物被山賊劫走","貨物被豆芽咬爛","其他")
            var position = 0            //設定position
            AlertDialog.Builder(this.context!!)
                .setTitle("回報訂單的狀況，我....")
                .setSingleChoiceItems(condition_list, 0) { _, i ->
                        position = i }
                .setPositiveButton("註銷訂單") {dialog,_ ->

                    if(shipmentId == 0) {
                        Toast.makeText(context, "尚未接單，無法註銷", Toast.LENGTH_SHORT).show()
                    }else if(shipmentStatus == "準備中"){
                        Toast.makeText(context, "貨物尚未出發，無法註銷", Toast.LENGTH_SHORT).show()
                    }else if(shipmentStatus == "運送中"){
                        cancel()
                    }

                    dialog.cancel()
                }
                .setNegativeButton("沒事"){dialog,_ ->
                    dialog.cancel()
                }
                .show()
        }

        //更新訂單狀態
        btn_renew.setOnClickListener {
            initial()
            myTask()
        }

    }
    override fun onRequestPermissionsResult(requestCode: Int,  permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    (activity as RunnerActivity).finish()
                }
                return
            }
        }
    }

    //掃描回傳結果
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                val bundle = data.getExtras();
                if (bundle == null) {
                    return
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    val result = bundle.getString(CodeUtils.RESULT_STRING)
                    Toast.makeText(this.context, "掃描：" + result, Toast.LENGTH_LONG).show()

                    if (shipmentId == 0){
                        Toast.makeText(context, "目前無訂單！", Toast.LENGTH_SHORT).show()
                    }else{
                        //check API
                        checkAPI(shipmentStatus, result!!)
                    }

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this.context, "解析二维码失败", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    //相機權限
    fun cameraPermission(){
        if (ContextCompat.checkSelfPermission(this.context!!,  Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {  //判斷是否未取得權限(!=)
            if (ActivityCompat.shouldShowRequestPermissionRationale((activity as RunnerActivity), Manifest.permission.CAMERA)) {
                AlertDialog.Builder(this.context!!)
                    .setMessage("我需要相機才能送貨，給我權限吧？")
                    .setPositiveButton("OK") { _, _ ->
                        //跳出視窗，嘗試向使用者取得權限
                        ActivityCompat.requestPermissions((activity as RunnerActivity),
                            arrayOf(Manifest.permission.CAMERA),
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS
                        )
                    }
                    .setNegativeButton("No") { _, _ -> (activity as RunnerActivity).finish() }
                    .show()
            } else {
                ActivityCompat.requestPermissions((activity as RunnerActivity),
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS
                )
            }
        }
    }


    fun myTask(){
        API.apiInterface.myTask().enqueue(object: Callback<MyTaskResponse>{
            override fun onFailure(call: Call<MyTaskResponse>, t: Throwable) {
            }
            override fun onResponse(call: Call<MyTaskResponse>, response: Response<MyTaskResponse>) {
                if (response.code() == 200){
                    val responsebody = response.body()
                    val name = responsebody!!.good_name
                    val start_station_id = responsebody.start_station_id
                    val des_station_id = responsebody.des_station_id
                    val status = responsebody.status
                    val shipment_id = responsebody.id
                    val image = responsebody.photo_url
                    val price = responsebody.price

                    Glide.with(shipment_image.context).load(image).into(shipment_image)
                    shipmentName = name
                    shipmentId = shipment_id
                    startStation(start_station_id)
                    destination(des_station_id)
                    shipmentStatus = status
                    shipmentPrice = price.toString()
                    renewText()

                }
            }
        })
    }

    //顯示目的地
    fun destination(mode: Int){
        if(!isResumed) return

        when(mode){
            1 ->{
                shipmentDes = "雅典"
                flag_1.visibility = View.VISIBLE
            }
            2 ->{
                shipmentDes = "菲基斯"
                flag_2.visibility = View.VISIBLE
            }
            3 ->{
                shipmentDes = "阿卡迪亞"
                flag_3.visibility = View.VISIBLE
            }
            4 ->{
                shipmentDes = "斯巴達"
                flag_4.visibility = View.VISIBLE
            }
        }
    }
    //顯示起始站
    fun startStation(mode: Int){
        when(mode){
            1 ->{
                shipmentStart = "雅典"
            }
            2 ->{
                shipmentStart = "菲基斯"
            }
            3 ->{
                shipmentStart = "阿卡迪亞"
            }
            4 ->{
                shipmentStart = "斯巴達"
            }
        }
    }

    //更新顯示字串
    fun renewText(){
        if(!isResumed) return

        tv_name.text = "訂單名稱：" + shipmentName
        tv_start.text = "起始驛站：" + shipmentStart
        tv_des.text = "目的驛站：" + shipmentDes
        tv_status.text = "狀態：" + shipmentStatus
        tv_price.text = "本單收入：" + shipmentPrice
    }

    //check in/out
    fun checkAPI(status: String, result: String){
        if (status == "準備中"){
            if (shipmentStart == result){
                API.apiInterface.checkin(CheckinRequest(result)).enqueue(object : Callback<CheckResponse>{
                    override fun onFailure(call: Call<CheckResponse>, t: Throwable) {
                    }
                    override fun onResponse(call: Call<CheckResponse>, response: Response<CheckResponse>) {
                        if (response.isSuccessful) {
                            val responsebody = response.body()!!
                            if (responsebody.data != null) {
                                val newStatus = responsebody.data.status
                                shipmentStatus = newStatus
                                renewText()
                                return
                            }
                        }
                    }
                })
            }else{
                Toast.makeText(context, "你送錯啦，請跟驛站人員確認！", Toast.LENGTH_SHORT).show()
            }

        }else if (status == "運送中"){
            if (shipmentDes == result){
                API.apiInterface.checkout(CheckoutRequest(result)).enqueue(object : Callback<CheckResponse>{
                    override fun onFailure(call: Call<CheckResponse>, t: Throwable) {
                    }
                    override fun onResponse(call: Call<CheckResponse>, response: Response<CheckResponse>) {
                        if (response.isSuccessful) {
                            val responsebody = response.body()!!
                            if (responsebody.data != null) {
                                val newStatus = responsebody.data.status
                                shipmentStatus = newStatus
                                renewText()
                                Toast.makeText(context, "恭喜完成任務！", Toast.LENGTH_LONG).show()
                                return
                            }
                        }else if(response.code() == 409){
                            val responsebody = response.body()
                            val message = responsebody!!.message
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }else{
                Toast.makeText(context, "你送錯啦，請跟驛站人員確認！", Toast.LENGTH_SHORT).show()
            }

        }else return

    }

    //更新畫面狀態
    fun initial(){
        if(!isResumed) return

        shipmentName = "無訂單"
        shipmentStart = "無"
        shipmentDes = "無"
        shipmentStatus = "無"
        shipmentPrice = "無"

        flag_1.visibility = View.INVISIBLE
        flag_2.visibility = View.INVISIBLE
        flag_3.visibility = View.INVISIBLE
        flag_4.visibility = View.INVISIBLE

        renewText()

    }
    //註銷訂單
    fun cancel() {
        println("==========$shipmentId")
        API.apiInterface.cancel(CancelRequest(shipmentId)).enqueue(object : Callback<CancelResponse> {
            override fun onFailure(call: Call<CancelResponse>, t: Throwable) {
                println("=============$t")
            }

            override fun onResponse(call: Call<CancelResponse>, response: Response<CancelResponse>) {
                if (response.isSuccessful) {
                    val responsebody = response.body()
                    val message_status = responsebody!!.message
                    shipmentStatus = message_status
                    renewText()
                    Toast.makeText(context, "遭遇不測，貨物${shipmentStatus}", Toast.LENGTH_SHORT).show()
                    shipmentId = 0
                    return
                } else if (response.code() == 409) {
                    println("===============$response")
                }
            }
        })

    }



}
