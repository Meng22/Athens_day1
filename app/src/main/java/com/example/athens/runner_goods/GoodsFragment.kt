package com.example.athens.runner_goods


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.athens.R
import com.example.athens.api.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_shop.*
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
class ShopFragment : Fragment() {
    private val goodsAdapter = GoodsAdapter()
    private var athensList: MutableList<GoodsData> = arrayListOf()
    private var arkadiaList: MutableList<GoodsData> = arrayListOf()
    private var phokisList: MutableList<GoodsData> = arrayListOf()
    private var spartaList: MutableList<GoodsData> = arrayListOf()
    private var isEnter = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop, container, false)
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        if (enter && !isEnter){
            isEnter = true
            renewList()
            com.example.athens.println("======onCreateAnimation")
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
        rv_station.layoutManager = LinearLayoutManager(context)
        rv_station.adapter = goodsAdapter
        goodsAdapter.setToClick(object : GoodsAdapter.ItemClickListener {
            override fun toClick(item: GoodsData) {
                showDialog(item)
            }
        })

        renewList()

        btn_athens.setOnClickListener {
            updateLayout(1)
        }
        btn_phokis.setOnClickListener {
            updateLayout(2)
        }
        btn_arcadia.setOnClickListener {
            updateLayout(3)
        }
        btn_sparta.setOnClickListener {
            updateLayout(4)
        }


    }

    //顯示清單
    fun renewList(){
        API.apiInterface.showGoods().enqueue(object : Callback<GoodsResponse>{
            override fun onFailure(call: Call<GoodsResponse>, t: Throwable) {
                com.example.athens.println("================$t")
            }
            override fun onResponse(call: Call<GoodsResponse>, response: Response<GoodsResponse>) {
                if (response.code() == 200){
                    val responsebody = response.body()
                    val responselist = responsebody!!.data
                    val goodsList = responselist.toMutableList()
                    com.example.athens.println("=============$goodsList")
                    athensList = goodsList.filter { it.start_station_id == 1 }.toMutableList()
                    phokisList = goodsList.filter { it.start_station_id == 2 }.toMutableList()
                    arkadiaList = goodsList.filter { it.start_station_id == 3 }.toMutableList()
                    spartaList = goodsList.filter { it.start_station_id == 4 }.toMutableList()
                }
                else if(response.code() == 409){
                    val responsebody = response.body()
                    val message = responsebody!!.message
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    //接案
    fun tasks(item: GoodsData){
        API.apiInterface.task(TaskRequest(item.id)).enqueue(object : Callback<TasksResponse>{
            override fun onFailure(call: Call<TasksResponse>, t: Throwable) {
                com.example.athens.println("===============$t")
            }
            override fun onResponse(call: Call<TasksResponse>, response: Response<TasksResponse>) {
                if (response.isSuccessful){
                    val responsebody = response.body()
                    val message = responsebody!!.message
                    Toast.makeText(context, message,Toast.LENGTH_SHORT).show()
                }else if (response.code() == 409){
                    val responsebody = response.errorBody()!!.string()
                    val gson = Gson()
                    val errorMessage = gson.fromJson<ErrorMessage>(responsebody, ErrorMessage::class.java)
                    println("409===========${errorMessage.message}")
                    Toast.makeText(context, errorMessage.message, Toast.LENGTH_SHORT).show()

                }
            }
        })
    }
    fun updateLayout(mode: Int){
        when(mode){
            1 ->{
                goodsAdapter.update(athensList)
            }
            2 ->{
                goodsAdapter.update(phokisList)
            }
            3 ->{
                goodsAdapter.update(arkadiaList)
            }
            4 ->{
                goodsAdapter.update(spartaList)
            }
        }
    }

    fun showDialog(item: GoodsData){
        val inflater = LayoutInflater.from(this.context)
        val view = inflater.inflate(R.layout.dialog_view, null)
        val dialog_mission = AlertDialog.Builder(this.context!!)
            .setView(view)
            .show()
        val dialog_image = view.findViewById<ImageView>(R.id.dialog_image)
        val dialog_title = view.findViewById<TextView>(R.id.dialog_title)
        val dialog_message = view.findViewById<TextView>(R.id.dialog_message)
        val dialog_start = view.findViewById<TextView>(R.id.dialog_start)
        val dialog_des = view.findViewById<TextView>(R.id.dialog_des)
        val dialog_weight = view.findViewById<TextView>(R.id.dialog_weight)
        val btn_confirm = view.findViewById<Button>(R.id.btn_confirm)
        val btn_cancel = view.findViewById<Button>(R.id.btn_cancel)

        Glide.with(dialog_image.context).load(item.photo_url).into(dialog_image)
        dialog_title.text = "訂單資訊"
        dialog_message.text = "是否要運送${item.good_name}呢？"
        dialog_weight.text = "貨品總重：${item.weight} kg"
        showStart(dialog_start, item.start_station_id)
        showDes(dialog_des, item.des_station_id)

        btn_confirm.setOnClickListener {
            //接任務api
            tasks(item)
            dialog_mission.cancel()

//            //重新load資料
//            renewList()
//            updateLayout(item.start_station_id)
//            com.example.athens.println("================${updateLayout(item.start_station_id)}")
            dialog_mission.cancel()

        }
        btn_cancel.setOnClickListener {
            dialog_mission.cancel()
        }



    }
    fun showStart(station: TextView,mode: Int){
        when(mode){
            1 ->{
                station.text = "貨物所在：雅典"
            }
            2 ->{
                station.text = "貨物所在：菲基斯"
            }
            3 ->{
                station.text = "貨物所在：阿卡迪亞"
            }
            4 ->{
                station.text = "貨物所在：斯巴達"
            }
        }
    }
    fun showDes(station: TextView,mode: Int){
        when(mode){
            1 ->{
                station.text = "即將送往：雅典"
            }
            2 ->{
                station.text = "即將送往：菲基斯"
            }
            3 ->{
                station.text = "即將送往：阿卡迪亞"
            }
            4 ->{
                station.text = "即將送往：斯巴達"
            }
        }
    }
}

data class ErrorMessage(
    val message: String
)
