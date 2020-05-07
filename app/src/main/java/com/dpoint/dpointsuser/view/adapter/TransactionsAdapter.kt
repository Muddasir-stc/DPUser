package com.dpoints.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.transaction.UsedOffer
import com.dpoints.dpointsmerchant.utilities.DateTime
import java.util.*

class TransactionsAdapter(
    private val listitem: List<UsedOffer>,
    private val context: Context
) : RecyclerView.Adapter<TransactionsAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.cardView)
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val textView_offerName: TextView = view.findViewById(R.id.textView_offerName)
        val textView_restrauntName: TextView = view.findViewById(R.id.textView_restrauntName)
        val textView_offer: TextView = view.findViewById(R.id.textView_offer)
        val textView_coins: TextView = view.findViewById(R.id.textView_coins)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val mEarn: TextView = view.findViewById(R.id.textView_assign)


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_offer_date, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = listitem.size

    override fun onBindViewHolder(holder: TransactionsAdapter.ViewHolder, position: Int) {
        var model = listitem[position]
        Glide.with(context).load(model.offer_image).placeholder(R.drawable.error)
            .into(holder.imageView)
        holder.textView_offerName.text = model.offer
        holder.textView_restrauntName.text = "Shop - " + model.shop_name
        holder.textView_offer.text = model.shop_description
        holder.textView_coins.text = model.offer + " DPoints on Shopping of " + model.amount
        holder.tvDate.text = model.created_at.split(" ")[0];
    }
}