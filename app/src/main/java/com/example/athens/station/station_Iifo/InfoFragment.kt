package com.example.athens.station.station_Iifo


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.athens.R
import com.example.athens.api.API
import com.example.athens.api.StationGoods
import com.example.athens.api.StationGoodsResponse
import com.example.athens.main.ChooseActivity
import com.example.athens.main.StationActivity
import kotlinx.android.synthetic.main.activity_station.*
import kotlinx.android.synthetic.main.fragment_info.*
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
class InfoFragment(val mode: Int) : Fragment() {
    private val infoAdapter = InfoAdapter()
    private val stationList : MutableList<StationGoods> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val manager = (activity as StationActivity).supportFragmentManager

        switchColor(mode)

        val addGoodsFragment = AddGoodsFragment(mode)
        stationList.clear()
        rv_allgoods.layoutManager = LinearLayoutManager(context)
        rv_allgoods.adapter = infoAdapter
        infoAdapter.setToClick(object: InfoAdapter.ItemClickListener{
            override fun toClick(item: StationGoods) {
                stationDialog(item)
            }
        })

        ed_search.addTextChangedListener (object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val search_word = ed_search.text.toString()
                val searchList = stationList.filter { "$search_word" in it.name }.toMutableList()
                infoAdapter.update(searchList)
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        toolbar_renew.setOnClickListener{
            callApi(mode)
        }

        toolbar_upbutton.setOnClickListener {
            //之後改掉，fragment返回建
            val intent = Intent(this.context, ChooseActivity::class.java)
            startActivity(intent)
        }
        toolbar_addButton.setOnClickListener {
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.framelayout_station, addGoodsFragment).addToBackStack(null).commit()
        }
        callApi(mode)

    }
    fun callApi(mode: Int){
        gif_waiting.visibility = View.VISIBLE
        API.apiInterface.stationInfo().enqueue(object: Callback<StationGoodsResponse> {
            override fun onFailure(call: Call<StationGoodsResponse>, t: Throwable) {
            }
            override fun onResponse(call: Call<StationGoodsResponse>, response: Response<StationGoodsResponse>) {
                if (response.isSuccessful){
                    com.example.athens.main.println("===========$response")
                    val responsebody = response.body()
                    val dataList = responsebody!!.data
                    val athensList = dataList.filter {it.now_station_id == mode }
                    stationList.clear()
                    stationList.addAll(athensList.filter { it.status == "準備中"})
                    stationList.addAll(athensList.filter { it.status == "運送中"})
                    com.example.athens.main.println("===========$stationList")

                    if(!isResumed) return
                    infoAdapter.update(stationList)
                    gif_waiting.visibility = View.INVISIBLE
                }
            }
        })
    }
    fun stationDialog(item: StationGoods){
        val inflater = LayoutInflater.from(this.context)
        val view = inflater.inflate(R.layout.station_dialog_view, null)
        val dialog_station = AlertDialog.Builder(this.context!!)
            .setView(view)
            .show()
        val dialog_image = view.findViewById<ImageView>(R.id.stationDialog_image)
        val dialog_details = view.findViewById<TextView>(R.id.stationDialog_details)
        val btn_confirm = view.findViewById<Button>(R.id.stationDialog_confirm)

        Glide.with(dialog_image.context).load(item.photo_url).into(dialog_image)
        dialog_details.text = "物品名稱：${item.name}\n重量：${item.weight}\n描述：${item.description}\n狀態：${item.status}\n現在位置：${item.now_station.name}\n目的地：${item.des_station.name}"
        btn_confirm.setOnClickListener {
            dialog_station.dismiss()
        }
    }

    //改變顏色
    fun switchColor(mode: Int) {
        when (mode) {
            1 -> {
                toolbar_info.setBackgroundColor(Color.rgb(0, 84, 147))
            }
            2 -> {
                toolbar_info.setBackgroundColor(Color.rgb(70, 124, 36))
            }
            3 -> {
                toolbar_info.setBackgroundColor(Color.rgb(245, 180, 51))
            }
            4 -> {
                toolbar_info.setBackgroundColor(Color.rgb(148, 17, 0))
            }
        }
    }




}
