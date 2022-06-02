package com.example.mptfood_staff

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mptfood_staff.models.ProductInList

class OrderAdapter(val context: Context, val listProducts : ArrayList<ProductInList>) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val prodName = view.findViewById<TextView>(R.id.prod_name)
        val prodPrice = view.findViewById<TextView>(R.id.prod_price)

        fun bindView(item : ProductInList){
            prodName.text = item.product.name
            prodPrice.text = "${item.quantity} X ${item.product.price}"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(listProducts[position])
    }

    override fun getItemCount(): Int = listProducts.size
}