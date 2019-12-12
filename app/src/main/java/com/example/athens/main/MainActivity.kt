package com.example.athens.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.athens.R
import com.example.athens.api.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    companion object {
        private const val RUNNER_REQUEST = 11
        private const val STATION_REQUEST = 12
    }
    private var role = "runner"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        switch_view.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val isOpened = switch_view.isOpened
                if (isOpened){
                    role = "station"
                    println("===========$role")
                }else{
                    role = "runner"
                    println("===========$role")
                }
            }
        })

        //註冊
        btn_register.setOnClickListener {
            if(ed_name.text.isNullOrEmpty() || ed_password.text.isNullOrEmpty()){
                Toast.makeText(this@MainActivity,"請輸入帳號密碼", Toast.LENGTH_SHORT).show()
            }else{
                API.apiInterface.register(
                    RegisterRequest(
                        ed_name.text.toString(),
                        ed_password.text.toString(),
                        role    //選擇身分
                    )
                ).enqueue(object : Callback<RegisterResponse>{
                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        println("=================$t")
                    }

                    override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                        if (response.code() == 201){
                            Toast.makeText(this@MainActivity,"註冊成功", Toast.LENGTH_SHORT).show()
                        } else if(response.code() == 409){
                            AlertDialog.Builder(this@MainActivity)
                                .setTitle("請洽系統管理員")
                                .setMessage("請寄 email 至： 123@gmail.com")
                                .setNeutralButton("OK"){dialog,_ ->
                                    dialog.dismiss()
                                }
                                .show()
                        }
                    }
                })
            }
        }
        //登入
        btn_login.setOnClickListener {
            if(ed_name.text.isNullOrEmpty() || ed_password.text.isNullOrEmpty()){
                Toast.makeText(this@MainActivity,"請輸入帳號密碼", Toast.LENGTH_SHORT).show()
            }else{
                API.apiInterface.login(
                    LoginRequest(
                        ed_password.text.toString(),
                        ed_name.text.toString()
                    )
                ).enqueue(object: Callback<LoginResponse>{
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        println("=================$t")
                    }
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if (response.code() == 200){
                            val responseBody =  response.body()
                            val token = responseBody!!.data.api_token
                            val role_name = responseBody.data.role_name
                            API.token = token
                            println("============token=${API.token}")

                            if (role_name == "runner"){   //跑者頁面
                                val intent = Intent(this@MainActivity, RunnerActivity::class.java)
                                startActivityForResult(intent, RUNNER_REQUEST)
                            }else if (role_name == "station"){   //管理者頁面
                                if (switch_view.isOpened){
                                    val intent = Intent(this@MainActivity, ChooseActivity::class.java)
                                    startActivityForResult(intent, STATION_REQUEST)
                                }else{
                                    Toast.makeText(this@MainActivity,"無此帳號", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        else if(response.code() == 404){
                            Toast.makeText(this@MainActivity,"無此帳號", Toast.LENGTH_SHORT).show()
                        }
                        else if(response.code() == 401){
                            Toast.makeText(this@MainActivity,"密碼錯誤", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }
    }
}
