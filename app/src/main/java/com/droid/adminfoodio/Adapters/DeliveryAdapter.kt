package com.droid.adminfoodio.Adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.droid.adminfoodio.R
import com.droid.adminfoodio.databinding.OutForDeliveryRvBinding

class DeliveryAdapter(private val costumerName:MutableList<String>, private val moneyStatus: MutableList<String>):RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val binding = OutForDeliveryRvBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DeliveryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return costumerName.size
    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class DeliveryViewHolder(private val binding: OutForDeliveryRvBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                receivedTxt.text = moneyStatus[position]
                customerNameTxt.text = costumerName[position]

                val colorMap = mapOf(
                    "received" to Color.GREEN,
                    "notReceived" to Color.RED,
                    "Pending" to Color.GRAY
                )
                receivedTxt.setTextColor(colorMap[moneyStatus[position]] ?: Color.BLACK)
                statusColor.backgroundTintList =
                    ColorStateList.valueOf(colorMap[moneyStatus[position]] ?: Color.BLACK)
            }
        }

    }
}