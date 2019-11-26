package com.example.athens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.athens.api.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //註冊
        btn_register.setOnClickListener {
            if(ed_name.text.isNullOrEmpty() || ed_password.text.isNullOrEmpty()){
                Toast.makeText(this@MainActivity,"請輸入帳號密碼", Toast.LENGTH_SHORT).show()
            }else{
                API.apiInterface.register(
                    RegisterRequest(
                        ed_name.text.toString(),
                        ed_password.text.toString(),
                        "runner"
                    )
                ).enqueue(object : Callback<RegisterResponse>{
                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        println("=================$t")
                    }

                    override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                        if (response.code() == 201){
//                        ed_name.text = null
//                        ed_password.text = null
                            Toast.makeText(this@MainActivity,"註冊成功", Toast.LENGTH_SHORT).show()
                        } else if(response.code() == 409){
                            Toast.makeText(this@MainActivity,"該使用者名稱已存在", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }
        //登入
        btn_login.setOnClickListener {
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
                        API.token = token
                        println("============token=${API.token}")
                        val intent = Intent(this@MainActivity, HomeActivity::class.java)
                        startActivity(intent)
                    }
                }
            })
        }
    }
}
