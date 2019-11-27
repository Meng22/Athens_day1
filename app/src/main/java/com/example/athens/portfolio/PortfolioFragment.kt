package com.example.athens.portfolio


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.athens.R
import com.example.athens.api.*
import kotlinx.android.synthetic.main.fragment_portfolio.*
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
class PortfolioFragment : Fragment() {
    private val portfolioAdapter = PortfolioAdapter()
    private var historyList: MutableList<HistoryData> = arrayListOf()
    private var totalDistance = "載入中..."
    private var badge = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_portfolio, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rv_history.layoutManager = LinearLayoutManager(this.context)
        rv_history.adapter = portfolioAdapter

        showHistory()
        showMedal()

    }
    fun showHistory(){
        API.apiInterface.runnerHistory().enqueue(object: Callback<HistoryResponse> {
            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
            }
            override fun onResponse(call: Call<HistoryResponse>, response: Response<HistoryResponse>) {
                if (response.code() == 200){
                    val resposebody = response.body()
                    val responselist = resposebody!!.data
                    historyList = responselist.toMutableList()

                    if(!isResumed) return
                    portfolioAdapter.update(historyList)
                }
            }
        })
    }
    fun showMedal(){
        API.apiInterface.medalStatus().enqueue(object : Callback<medalStatusResponse>{
            override fun onFailure(call: Call<medalStatusResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<medalStatusResponse>, response: Response<medalStatusResponse>) {
                if (response.code() == 200){
                    val responsebody = response.body()
                    val distance = responsebody!!.data.distance
                    val badge_name = responsebody.data.badge_name
                    println("============$badge_name")
                    totalDistance = distance.toString()
                    badge = badge_name

                    if(!isResumed) return
                    total_distance.text = "${totalDistance}.0"

                }
            }
        })
    }






}
