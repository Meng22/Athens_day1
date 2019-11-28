package com.example.athens.portfolio


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

        btn_level.setOnClickListener {
            runnerLevel(badge)
        }
        btn_accomplishment.setOnClickListener {
            Toast.makeText(context, "蒐集成就，功能尚未開放", Toast.LENGTH_SHORT).show()
        }
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
                    total_distance.text = "${totalDistance}"
                }
            }
        })
    }
    fun runnerLevel(medal: String) {
        val inflater = LayoutInflater.from(this.context)
        val view = inflater.inflate(R.layout.medal_dialog_view, null)
        val dialog_medal = AlertDialog.Builder(this.context!!)
            .setView(view)
            .show()
        val medal_name = view.findViewById<TextView>(R.id.medal_level)
        val medal_image = view.findViewById<ImageView>(R.id.medal_image)
        val medal_num = view.findViewById<TextView>(R.id.medal_number)
        val btn_check = view.findViewById<TextView>(R.id.btn_check)

        medal_name.text = medal
        btn_check.setOnClickListener {
            dialog_medal.dismiss()
        }

    }

}
