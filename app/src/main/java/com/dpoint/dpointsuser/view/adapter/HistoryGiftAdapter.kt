package com.dpoint.dpointsuser.view.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.history.Exchange

class HistoryGiftAdapter(
    private val listitem: List<Exchange>,
    private val context: Context
) : RecyclerView.Adapter<HistoryGiftAdapter.ViewHolder>() {
    inner class ViewHolder(view: View, context: Context) : RecyclerView.ViewHolder(view) {
        val txtShop=view.findViewById<TextView>(R.id.txtShop)
        val imageView=view.findViewById<ImageView>(R.id.imageView)
        val txtAmount=view.findViewById<TextView>(R.id.txtAmount)
        val txtDate=view.findViewById<TextView>(R.id.txtDate)
        val txtCoins=view.findViewById<TextView>(R.id.txtCoins)
        val bindview = view
        fun bindto(itemtype: Exchange) {
            txtShop.text=itemtype.shop_name
            txtAmount.text="$${itemtype.coin_value}"
            txtDate.text=itemtype.created_at.split(" ")[0]
            txtCoins.text=itemtype.coins.toString()
            Glide.with(context).load(itemtype.shop_image).into(imageView)
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_exchange, parent, false)
        return ViewHolder(view,parent.context)
    }

    override fun getItemCount() = listitem.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindto(listitem[position])
    }
}