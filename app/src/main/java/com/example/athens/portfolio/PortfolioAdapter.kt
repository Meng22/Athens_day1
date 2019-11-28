package com.example.athens.portfolio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.athens.R
import com.example.athens.api.HistoryData

class PortfolioAdapter: RecyclerView.Adapter<PortfolioAdapter.ViewHolder>() {
    val list : MutableList<HistoryData> = arrayListOf()
    private var startText = ""
    private var desText = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.history_itemview, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binde(list[position])
    }
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name = view.findViewById<TextView>(R.id.history_name)
        val where = view.findViewById<TextView>(R.id.history_where)
        val distance = view.findViewById<TextView>(R.id.history_distance)
        val weight = view.findViewById<TextView>(R.id.history_weight)

        fun binde(item: HistoryData){
            name.text = "貨品：${item.good_name}"
            where.text = "${item.start_station_name} ─ ${item.des_station_name}"
            distance.text = "${item.distance} 公里"
            weight.text = "${item.weight} kg"
        }
    }
    fun update(newList: MutableList<HistoryData>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}