package com.dpoints.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dpoint.dpointsuser.R
import com.dpoints.datasource.model.OrdersModel
import com.dpoints.dpointsmerchant.datasource.remote.order.Order
import com.dpoints.dpointsmerchant.utilities.OnItemClickListener

class OrdersAdapter(
    private val listitem :List<Order>,
    private val listener: OnItemClickListener
):RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {
    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val orderoffername: TextView = view.findViewById(R.id.textView5)
        val orderofferdetail: TextView = view.findViewById(R.id.text)
        val ordershopname:TextView=view.findViewById(R.id.textView8)
        val orderamount:TextView=view.findViewById(R.id.textView_amount)
        val ordertransactionsid: TextView = view.findViewById(R.id.textView4)
        val points: TextView = view.findViewById(R.id.textView6)
        val orderType: TextView = view.findViewById(R.id.orderType)
        val bindview=view
        fun bindto(itemtype: Order){

            orderoffername.text= "Bindaas Offer"
//            orderofferdetail.text = itemtype.ord  ersdetails
            ordertransactionsid.text= "1234567890"
            orderType.text= "Redeem"
            points.text=itemtype.coins
           ordershopname.text="Resto Shop"
          orderamount.text=itemtype.offer_amount

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_orders,parent,false)
        return OrdersAdapter.ViewHolder(view)
    }

    override fun getItemCount()=listitem.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindto(listitem[position])
        holder.bindview.setOnClickListener {
            listener.onItemClick(position,1)
        }
    }
}