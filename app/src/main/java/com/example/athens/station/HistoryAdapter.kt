package com.example.athens.station

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.athens.R
import com.example.athens.api.StationGoods

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    val list : MutableList<StationGoods> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.station_goods_itemview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val image = view.findViewById<ImageView>(R.id.shopGoods_image)
        val name = view.findViewById<TextView>(R.id.shopGoods_name)
        val weight = view.findViewById<TextView>(R.id.shopGoods_weight)
        val status = view.findViewById<TextView>(R.id.shopGoods_status)

        fun bind(item: StationGoods){
            Glide.with(image.context).load(item.photo_url).into(image)
            name.text = "訂單名稱：${item.name}"
            weight.text = "重量：${item.weight}"
            status.text = "狀態：${item.status}"

        }
    }

    fun update(newList: MutableList<StationGoods>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}