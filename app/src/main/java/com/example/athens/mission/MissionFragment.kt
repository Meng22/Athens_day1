package com.example.athens.mission


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
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
import com.example.athens.HomeActivity
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mission, container, false)
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        if (enter && !isEnter){
            isEnter = true

            //初始畫面
            shipmentName = "無訂單"
            shipmentStart = "無"
            shipmentDes = "無"
            shipmentStatus = "無"
            myTask()

            com.example.athens.println("======刷新畫面")

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
        renewText()

        //相機權限
        permission()

        //載入跑者目前訂單
        myTask()

        //掃描
        qr_code.setOnClickListener {
            val intent = Intent(this.context, CaptureActivity::class.java)
            startActivityForResult(intent,1)
        }

        //debug only
        button_start.setOnClickListener {
            val result = "斯巴達"
            Toast.makeText(this.context, "地點:" + result, Toast.LENGTH_LONG).show()

            //check API
            checkAPI("準備中", result)
        }

        button_end.setOnClickListener {
            val result = "阿卡迪亞"
            Toast.makeText(this.context, "地點:" + result, Toast.LENGTH_LONG).show()

            //check API
            checkAPI("運送中", result)
        }

    }
    override fun onRequestPermissionsResult(requestCode: Int,  permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    (activity as HomeActivity).finish()
                }
                com.example.athens.println("=========$permissions")
                com.example.athens.println("=========$grantResults")
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
                    Toast.makeText(this.context, "地點:" + result, Toast.LENGTH_LONG).show()

                    //check API
                    checkAPI(shipmentStatus, result!!)

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this.context, "解析二维码失败", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    fun permission(){
        if (ContextCompat.checkSelfPermission(this.context!!,  Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((activity as HomeActivity), Manifest.permission.CAMERA)) {
                AlertDialog.Builder(this.context!!)
                    .setMessage("我需要相機才能送貨，給我權限吧？")
                    .setPositiveButton("OK") { _, _ ->
                        ActivityCompat.requestPermissions((activity as HomeActivity),
                            arrayOf(Manifest.permission.CAMERA),
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS
                        )
                    }
                    .setNegativeButton("No") { _, _ -> (activity as HomeActivity).finish() }
                    .show()
            } else {
                ActivityCompat.requestPermissions((activity as HomeActivity),
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS
                )
            }
        }
    }


    fun myTask(){
        API.apiInterface.myTask().enqueue(object: Callback<TasksResponse>{
            override fun onFailure(call: Call<TasksResponse>, t: Throwable) {
            }
            override fun onResponse(call: Call<TasksResponse>, response: Response<TasksResponse>) {
                if (response.isSuccessful){
                    val responsebody = response.body()
                    val name = responsebody!!.good_name
                    val start_station_id = responsebody.start_station_id
                    val des_station_id = responsebody.des_station_id
                    val status = responsebody.status

                    shipmentName = name
                    startStation(start_station_id)
                    destination(des_station_id)
                    shipmentStatus = status
                    renewText()

                }
            }
        })
    }

    //顯示目的地
    fun destination(mode: Int){
        when(mode){
            1 ->{
                shipmentDes = "雅典"
//                flag_1.visibility = View.VISIBLE
            }
            2 ->{
                shipmentDes = "菲基斯"
//                flag_2.visibility = View.VISIBLE
            }
            3 ->{
                shipmentDes = "阿卡迪亞"
//                flag_3.visibility = View.VISIBLE
            }
            4 ->{
                shipmentDes = "斯巴達"
//                flag_4.visibility = View.VISIBLE
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

    //更新字串
    fun renewText(){
        tv_name.text = "訂單名稱：" + shipmentName
        tv_start.text = "起始驛站：" + shipmentStart
        tv_destination.text = "目的驛站：" + shipmentDes
        tv_status.text = "狀態：" + shipmentStatus
    }

    //check in/out
    fun checkAPI(status: String, station: String){
        if (status == "準備中"){
            API.apiInterface.checkin(CheckinRequest(station)).enqueue(object : Callback<CheckResponse>{
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

                        val message = responsebody.message
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }else if (status == "運送中"){
            API.apiInterface.checkout(CheckoutRequest(station)).enqueue(object : Callback<CheckResponse>{
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

                        val message = responsebody.message
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }

    }

}
