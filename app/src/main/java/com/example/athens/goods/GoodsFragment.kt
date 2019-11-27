package com.example.athens.goods


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.athens.R
import com.example.athens.api.*
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
                toSend(item)
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
    fun toSend(item: GoodsData){
        AlertDialog.Builder(this.context!!)
            .setTitle("訂單資訊")
            .setMessage("是否要運送貨物${item.good_name}？")
            .setPositiveButton("沒問題"){dialog,_ ->
                //接任務api
                tasks(item)

                //重新load資料
                renewList()
                updateLayout(item.start_station_id)
                com.example.athens.println("================${updateLayout(item.start_station_id)}")

            }
            .setNegativeButton("取消"){dialog,_ ->
                dialog.cancel()
            }
            .show()
    }
    fun renewList(){
        API.apiInterface.showGoods().enqueue(object : Callback<GoodsResponse>{
            override fun onFailure(call: Call<GoodsResponse>, t: Throwable) {
                com.example.athens.println("================$t")
            }
            override fun onResponse(call: Call<GoodsResponse>, response: Response<GoodsResponse>) {
                if (response.isSuccessful){
                    val responsebody = response.body()
                    val responselist = responsebody!!.data
                    val goodsList = responselist.toMutableList()
                    com.example.athens.println("=============$goodsList")
                    athensList = goodsList.filter { it.start_station_id == 1 }.toMutableList()
                    phokisList = goodsList.filter { it.start_station_id == 2 }.toMutableList()
                    arkadiaList = goodsList.filter { it.start_station_id == 3 }.toMutableList()
                    spartaList = goodsList.filter { it.start_station_id == 4 }.toMutableList()
                }
            }
        })
    }
    fun tasks(item: GoodsData){
        API.apiInterface.task(TaskRequest(item.id)).enqueue(object : Callback<TasksResponse>{
            override fun onFailure(call: Call<TasksResponse>, t: Throwable) {
                com.example.athens.println("===============$t")
            }
            override fun onResponse(call: Call<TasksResponse>, response: Response<TasksResponse>) {
                if (response.isSuccessful){
                    val responsebody = response.body()
                    val runner_id = responsebody!!.runner_id
                    com.example.athens.println("===============runner_id=$runner_id")
                    Toast.makeText(context, "已接到任務",Toast.LENGTH_SHORT).show()
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



}
