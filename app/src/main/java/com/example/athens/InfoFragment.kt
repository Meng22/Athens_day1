package com.example.athens


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.athens.api.API
import com.example.athens.api.StationGoods
import com.example.athens.api.StationGoodsResponse
import com.example.athens.main.ChooseActivity
import com.example.athens.main.StationActivity
import kotlinx.android.synthetic.main.fragment_info.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class InfoFragment : Fragment() {
    private val infoAdapter = InfoAdapter()
    private val stationList : MutableList<StationGoods> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val manager = (activity as StationActivity).supportFragmentManager
        val addGoodsFragment = AddGoodsFragment()
        stationList.clear()
        rv_allgoods.layoutManager = LinearLayoutManager(context)
        rv_allgoods.adapter = infoAdapter

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
            callApi()
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
        callApi()


    }
    fun callApi(){
        API.apiInterface.stationInfo().enqueue(object: Callback<StationGoodsResponse> {
            override fun onFailure(call: Call<StationGoodsResponse>, t: Throwable) {
            }
            override fun onResponse(call: Call<StationGoodsResponse>, response: Response<StationGoodsResponse>) {
                if (response.isSuccessful){
                    println("===========$response")
                    val responsebody = response.body()
                    val dataList = responsebody!!.data
                    val athensList = dataList.filter {it.start_station.name == "雅典"}
                    stationList.clear()
                    stationList.addAll(athensList.filter { it.status == "準備中"})
                    stationList.addAll(athensList.filter { it.status == "運送中"})
                    println("===========$stationList")

                    if(!isResumed) return
                    infoAdapter.update(stationList)
                    gif_waiting.visibility = View.INVISIBLE
                }
            }
        })
    }


}
