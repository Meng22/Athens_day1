package com.example.athens.goods

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.athens.R
import com.example.athens.api.GoodsData

class GoodsAdapter: RecyclerView.Adapter<GoodsAdapter.ViewHolder>() {
    val list : MutableList<GoodsData> = arrayListOf()
    private var sendListener : ItemClickListener? = null

    interface ItemClickListener{
        fun toClick(item: GoodsData)
    }
    fun setToClick(listener: ItemClickListener){
        sendListener = listener
    }

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
        val price = view.findViewById<TextView>(R.id.goods_price)
        val station = view.findViewById<TextView>(R.id.goods_station)
        val status = view.findViewById<TextView>(R.id.goods_status)

        fun bind(item: GoodsData){
            Glide.with(image.context).load(item.photo_url).into(image)
            name.text = "訂單名稱：${item.good_name}"
            showStation(item.des_station_id, station)
            status.text = "貨品狀態：${item.status}"
            price.text = "運送費用：${item.price}"
            itemView.setOnClickListener{
                sendListener?.toClick(item)
            }
        }
    }
    fun showStation(mode: Int, station: TextView){
        when(mode){
            1->{
                station.text = "目的驛站：雅典"
            }
            2 ->{
                station.text = "目的驛站：菲基斯"
            }
            3 ->{
                station.text = "目的驛站：阿卡迪亞"
            }
            4 ->{
                station.text = "目的驛站：斯巴達"
            }
        }
    }

    fun update(newList: MutableList<GoodsData>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}