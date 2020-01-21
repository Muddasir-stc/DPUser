package com.dpoints.view.adapter

import android.content.Context
import android.content.Intent
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dpoint.dpointsuser.R
import com.dpoints.dpointsmerchant.datasource.remote.gift.Data
import com.dpoints.dpointsmerchant.utilities.OnItemClickListener
import com.dpoints.dpointsmerchant.utilities.OnRemoveClickListener


class GiftAdapter(
    private var context: Context,
    private var listener: OnItemClickListener,
    private var data: List<Data>
) : RecyclerView.Adapter<GiftAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_giftcard, parent, false)
        return MyViewHolder(view)
    }

    private var position = 0

    fun getPosition(): Int {
        return position
    }

    fun setPosition(position: Int) {
        this.position = position
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var model = data[position]
        holder.textView_coin.text = model.coins
        holder.textView_offer.text = model.offer + "\$ Gift Card"
        Glide.with(context).load(model.image).placeholder(R.drawable.error).into(holder.imageView)
        holder.option_menu.setOnClickListener {
            listener.onItemClick(position,2)
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.cardView)
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val option_menu: TextView = view.findViewById(R.id.option_menu)
        val textView_offer: TextView = view.findViewById(R.id.textView_offer)
        val textView_coin: TextView = view.findViewById(R.id.textView_coin)
    }

}