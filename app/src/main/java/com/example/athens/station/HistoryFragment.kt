package com.example.athens.station


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.athens.R
import com.example.athens.api.API
import com.example.athens.api.StationGoods
import com.example.athens.api.StationGoodsResponse
import com.example.athens.main.ChooseActivity
import com.example.athens.station.station_Iifo.InfoAdapter
import kotlinx.android.synthetic.main.fragment_history.*
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
class HistoryFragment(val mode: Int) : Fragment() {
    private val station_historyAdapter = InfoAdapter()
    private val station_historyList : MutableList<StationGoods> = arrayListOf()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        switchColor(mode)

        rv_station_history.layoutManager = LinearLayoutManager(context)
        rv_station_history.adapter = station_historyAdapter

        callHistory(mode)

        toolbar_history_back.setOnClickListener {
            val intent = Intent(this.context, ChooseActivity::class.java)
            startActivity(intent)
        }

        toolbar_history_renew.setOnClickListener {
            callHistory(mode)
        }

    }
    fun callHistory(mode: Int){
        gif_history.visibility = View.VISIBLE
        API.apiInterface.stationInfo().enqueue(object: Callback<StationGoodsResponse> {
            override fun onFailure(call: Call<StationGoodsResponse>, t: Throwable) {
            }
            override fun onResponse(call: Call<StationGoodsResponse>, response: Response<StationGoodsResponse>) {
                if (response.isSuccessful){
                    com.example.athens.main.println("===========$response")
                    val responsebody = response.body()
                    val dataList = responsebody!!.data
                    val athensList = dataList.filter {it.now_station_id == mode }
                    station_historyList.clear()
                    station_historyList.addAll(athensList.filter { it.status == "已抵達"})
                    station_historyList.addAll(athensList.filter { it.status == "已註銷"})

                    if(!isResumed) return
                    station_historyAdapter.update(station_historyList)
                    gif_history.visibility = View.INVISIBLE
                }
            }
        })
    }
    //改變顏色
    fun switchColor(mode: Int) {
        when (mode) {
            1 -> {
                toolbar_history.setBackgroundColor(Color.rgb(0, 84, 147))
            }
            2 -> {
                toolbar_history.setBackgroundColor(Color.rgb(70, 124, 36))
            }
            3 -> {
                toolbar_history.setBackgroundColor(Color.rgb(245, 180, 51))
            }
            4 -> {
                toolbar_history.setBackgroundColor(Color.rgb(148, 17, 0))
            }
        }
    }



}
