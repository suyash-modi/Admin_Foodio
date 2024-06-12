package com.droid.adminfoodio.Adapters

import android.content.Context
import android.net.Uri
import com.droid.adminfoodio.databinding.AllItemRvBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.droid.adminfoodio.models.AllMenu
import com.google.firebase.database.DatabaseReference


class menuItemRVadapter(
    private val context: Context,
    private val menuList: ArrayList<AllMenu>,
    databaseReference: DatabaseReference
):RecyclerView.Adapter<menuItemRVadapter.cartViewHolder>() {

    private val itemQuantities=IntArray(menuList.size){1}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cartViewHolder {
        val binding=AllItemRvBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return cartViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    override fun onBindViewHolder(holder: cartViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class cartViewHolder(private val binding: AllItemRvBinding):RecyclerView.ViewHolder(binding.root)
    {
        fun bind(position: Int)
        {
            binding.apply {
                val menuItem=menuList[position]
                val uriString=menuItem.foodImage
                val uri= Uri.parse(uriString)
                val quantitys=itemQuantities[position]
                cartName.text=menuItem.foodName
                cartItemPrice.text=menuItem.foodPrice
                Glide.with(context).load(uri).into(cartImage)
                quantity.text=quantitys.toString()

                minusButton.setOnClickListener{
                    decreaseQuantity(position)
                }
                plusButton.setOnClickListener {
                    increaseQuantity(position)
                }
                deleteButton.setOnClickListener{

                    val itemPos=adapterPosition
                    if(itemPos!=RecyclerView.NO_POSITION)
                        deleteItem(itemPos)
                }

            }
        }

        private fun decreaseQuantity(position: Int)
        {
            if(itemQuantities[position]>1)
            {
                itemQuantities[position]--;
                binding.quantity.text= itemQuantities[position].toString()
            }
        }
        private fun increaseQuantity(position: Int)
        {
            if(itemQuantities[position]<10)
            {
                itemQuantities[position]++;
                binding.quantity.text= itemQuantities[position].toString()
            }
        }

        private fun deleteItem(position: Int)
        {
            menuList.removeAt(position)
            menuList.removeAt(position)
            menuList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position,menuList.size)
        }

    }
}