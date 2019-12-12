package com.example.athens.station


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide

import com.example.athens.R
import com.example.athens.api.API
import com.example.athens.api.StationLevelResponse
import com.example.athens.main.ChooseActivity
import kotlinx.android.synthetic.main.fragment_info.*
import kotlinx.android.synthetic.main.fragment_level.*
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
class LevelFragment(val mode: Int) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_level, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        switchColor(mode)
        showLevel()
        toolbar_level_upbutton.setOnClickListener{
            val intent = Intent(this.context, ChooseActivity::class.java)
            startActivity(intent)
        }

    }
    fun showLevel(){
        API.apiInterface.stationLevel().enqueue(object: Callback<StationLevelResponse>{
            override fun onFailure(call: Call<StationLevelResponse>, t: Throwable) {
            }
            override fun onResponse(call: Call<StationLevelResponse>, response: Response<StationLevelResponse>) {
                val responsebody = response.body()
                val dataList = responsebody!!.data
                val index = mode-1
                val data = dataList[index]
                val totalWeight = data.totalWeight.toInt()
                val level = data.level
                val income = data.income
                println("==========$data")

                if(!isResumed) return
                tv_totalweight.text = "累積運送量：$totalWeight"
                tv_stationlevel.text = "目前等級：$level"
                tv_income.text = "收入：$income"
                val ratio = (totalWeight/100)
                println("==========$ratio")
                switchImage(level)
                switchProgress(totalWeight)
            }
        })
    }

    fun switchColor(mode: Int) {
        when (mode) {
            1 -> {
                toolbar_level_back.setBackgroundColor(Color.rgb(0, 84, 147))
                linear_board.setBackgroundColor(Color.rgb(0, 84, 147))
            }
            2 -> {
                toolbar_level_back.setBackgroundColor(Color.rgb(70, 124, 36))
                linear_board.setBackgroundColor(Color.rgb(70, 124, 36))
            }
            3 -> {
                toolbar_level_back.setBackgroundColor(Color.rgb(245, 180, 51))
                linear_board.setBackgroundColor(Color.rgb(245, 180, 51))
            }
            4 -> {
                toolbar_level_back.setBackgroundColor(Color.rgb(148, 17, 0))
                linear_board.setBackgroundColor(Color.rgb(148, 17, 0))

            }
        }
    }

    fun switchImage(level: Int) {
        if (level == 1) {
            Glide.with(level_image.context).load(R.drawable.level1).into(level_image)
        }else if (level == 2){
            Glide.with(level_image.context).load(R.drawable.level2).into(level_image)
        }else if (level == 3){
            Glide.with(level_image.context).load(R.drawable.level3).into(level_image)
        }else{
            Glide.with(level_image.context).load(R.drawable.level4).into(level_image)
        }
    }

    fun switchProgress(totalWeight: Int){
        if(totalWeight < 100){
            progresss_level.setProgress(totalWeight)
        }else{
            progresss_level.setProgress(100)
        }
    }





}
