package com.dpoints.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.history.Exchange
import com.dpoint.dpointsuser.datasource.remote.history.Redeem
import com.dpoints.dpointsmerchant.datasource.remote.transaction.Tran
import com.dpoints.dpointsmerchant.utilities.OnItemClickListener

class RedeemAdapter(
    private val listitem: List<Redeem>,
    private val context: Context
) : RecyclerView.Adapter<RedeemAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtShop=view.findViewById<TextView>(R.id.shop_name)
        val txtUnits=view.findViewById<TextView>(R.id.txtUnits)
        val txtDate=view.findViewById<TextView>(R.id.txtDate)
        val unit=view.findViewById<TextView>(R.id.unit)
        val bindview = view
        fun bindto(itemtype: Redeem) {
            txtShop.text="@${itemtype.shop_name}"
            txtUnits.text="${itemtype.number_of_units}"
            txtDate.text=itemtype.created_at.split(" ")[0]
            unit.text=itemtype.unit
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_redeem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = listitem.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindto(listitem[position])
    }
}