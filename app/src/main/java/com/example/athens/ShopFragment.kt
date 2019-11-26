package com.example.athens


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.athens.api.API
import com.example.athens.api.GoodsData
import com.example.athens.api.GoodsResponse
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rv_station.layoutManager = LinearLayoutManager(context)
        rv_station.adapter = goodsAdapter

        API.apiInterface.showGoods().enqueue(object : Callback<GoodsResponse>{
            override fun onFailure(call: Call<GoodsResponse>, t: Throwable) {
                println("================$t")
            }
            override fun onResponse(call: Call<GoodsResponse>, response: Response<GoodsResponse>) {
                val responsebody = response.body()
                val responselist = responsebody!!.data
                val goodsList = responselist.toMutableList()
                athensList = goodsList.filter { it.des_station.id == 1 }.toMutableList()
                phokisList = goodsList.filter { it.des_station.id == 2 }.toMutableList()
                arkadiaList = goodsList.filter { it.des_station.id == 3 }.toMutableList()
                spartaList = goodsList.filter { it.des_station.id == 4 }.toMutableList()

            }
        })

//        rv_station.layoutManager = LinearLayoutManager(context)
//        rv_station.adapter = goodsAdapter

        //下拉式選單
        val station = arrayListOf("雅典","菲基斯","阿卡迪亞","斯巴達")
        val spinnerAdapter = ArrayAdapter(this.context!!, android.R.layout.simple_spinner_dropdown_item, station)
        spinner_station.adapter = spinnerAdapter
        spinner_station.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                Toast.makeText(context, "你選的是" + station[position], Toast.LENGTH_SHORT).show()
                when(station[position]){
                    "雅典" ->{
                        println("=============$athensList")
                        goodsAdapter.update(athensList)
                    }
                    "菲基斯" ->{
                        println("=============$phokisList")
                        goodsAdapter.update(phokisList)
                    }
                    "阿卡迪亞" ->{
                        println("=============$arkadiaList")
                        goodsAdapter.update(arkadiaList)
                    }
                    "斯巴達" ->{
                        println("=============$spartaList")
                        goodsAdapter.update(spartaList)
                    }
                }
            }
        }

    }


}
