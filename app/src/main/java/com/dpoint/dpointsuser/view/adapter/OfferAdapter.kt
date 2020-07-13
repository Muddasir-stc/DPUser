package com.dpoints.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dpoint.dpointsuser.utilities.OnItemClickListener
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.dpoint.dpointsuser.R
import com.dpoints.dpointsmerchant.datasource.remote.offer.Data


class OfferAdapter (
    private val listitem :List<Data>,
    private val listener: OnItemClickListener,
    val context:Context
):RecyclerView.Adapter<OfferAdapter.ViewHolder>(){
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.cardView)
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val textView_offerName: TextView = view.findViewById(R.id.textView_offerName)
        val textView_restrauntName: TextView = view.findViewById(R.id.textView_restrauntName)
        val textView_offer: TextView = view.findViewById(R.id.textView_offer)
        val textView_coins: TextView = view.findViewById(R.id.textView_coins)
        val mEarn: TextView = view.findViewById(R.id.textView_assign)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_offer,parent,false)

        return ViewHolder(view)
    }


    override fun getItemCount()=listitem.size

    override fun onBindViewHolder(holder: OfferAdapter.ViewHolder, position: Int) {
        var model = listitem[position]
        Glide.with(context).load(model.image).placeholder(R.drawable.error).into(holder.imageView)
        holder.textView_offerName.text = model.title
        holder.textView_restrauntName.text = "Shop - " + model.shop_name
        holder.textView_offer.text = model.description
        holder.textView_coins.text = model.offer + " DPoints on Shopping of " + model.amount
        holder.mEarn.setOnClickListener {
                listener.onItemClick(position,1)
        }


    }
}