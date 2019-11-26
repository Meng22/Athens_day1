package com.example.athens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.athens.api.GoodsData

class GoodsAdapter: RecyclerView.Adapter<GoodsAdapter.ViewHolder>() {
    val list : MutableList<GoodsData> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.goods_itemview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val image = view.findViewById<ImageView>(R.id.goods_image)
        val name = view.findViewById<TextView>(R.id.goods_name)
        val weight = view.findViewById<TextView>(R.id.goods_weight)
        val station = view.findViewById<TextView>(R.id.goods_station)
        val status = view.findViewById<TextView>(R.id.goods_status)

        fun bind(item: GoodsData){
//            Glide.with(image.context).load("url").into(image)
            name.text = "訂單名稱：${item.name}"
            station.text = "目的驛站：${item.des_station.name}"
            status.text = "貨品狀態：${item.status}"
            weight.text = "重量：${item.weight}g"
        }
    }
    fun update(newList: MutableList<GoodsData>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}