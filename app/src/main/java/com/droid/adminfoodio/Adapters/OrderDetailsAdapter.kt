package com.droid.adminfoodio.Adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.droid.adminfoodio.databinding.ActivityOrderDetailsBinding
import com.droid.adminfoodio.databinding.OrderdetailsItemBinding

class OrderDetailsAdapter(
    private val context: Context,
    private val foodNames: ArrayList<String>,
    private val foodPrice: ArrayList<String>,
    private val foodQuantity: ArrayList<Int>,
    private val foodImage: ArrayList<String>
): RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder>() {



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderDetailsAdapter.OrderDetailsViewHolder {
        val binding=OrderdetailsItemBinding.inflate(LayoutInflater.from(parent.context),parent,false )
        return OrderDetailsViewHolder(binding)
}

    override fun onBindViewHolder(
        holder: OrderDetailsAdapter.OrderDetailsViewHolder,
        position: Int
    ) {
      holder.bind(position)
    }

    override fun getItemCount(): Int {
        return foodNames.size
    }
   inner class OrderDetailsViewHolder(private val binding: OrderdetailsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int)
        {
            binding.apply {
                foodName.text=foodNames[position]
                price.text="$ " + foodPrice[position]
                quantity.text=foodQuantity[position].toString()

                val uriString = foodImage[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(foodImages)
            }
        }
    }
}