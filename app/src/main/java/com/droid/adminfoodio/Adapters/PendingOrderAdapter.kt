package com.droid.adminfoodio.Adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.droid.adminfoodio.databinding.PendingOrderRvBinding


class PendingOrderAdapter(
    private var context: Context,
    private val costumerName: MutableList<String>,
    private val ItemQuantities: MutableList<String>,
    private val foodImages: MutableList<String>,
    private val itemClicked : OnItemClicked,

) :
    RecyclerView.Adapter<PendingOrderAdapter.cartViewHolder>() {

    interface OnItemClicked {
        fun onItemClicked(position: Int)
        fun onItemAcceptClickListener(position: Int)
        fun onItemDispatchClickListener(position: Int)
    }

    private val itemQuantities = IntArray(costumerName.size) { 1 }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cartViewHolder {
        val binding =
            PendingOrderRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return cartViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return costumerName.size
    }

    override fun onBindViewHolder(holder: cartViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class cartViewHolder(private val binding: PendingOrderRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isAccepteed = false
        fun bind(position: Int) {
            binding.apply {
                item.text = costumerName[position]
                quantity.text = ItemQuantities[position]
                var uriString =foodImages[position]
                var uri= Uri.parse(uriString)
                Glide.with(context).load(uri).into(cartImage)

                acceptBtn.apply {

                    if (isAccepteed) {
                        text = "Dispatched"
                    } else {
                        text = "Accept"
                    }
                    setOnClickListener {
                        if (isAccepteed) {
                            costumerName.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            showToast("Order Dispatched")
                            itemClicked.onItemDispatchClickListener(position)

                        } else {
                            text = "Dispatch"
                            showToast("Order Accepted")
                            isAccepteed = true
                            itemClicked.onItemAcceptClickListener(position)
                        }

                    }
                }
                itemView.setOnClickListener {
                    itemClicked.onItemClicked(adapterPosition)
                }
            }
        }

        fun showToast(message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}