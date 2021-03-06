package com.dpoints.view.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.shop.Shop
import com.dpoint.dpointsuser.utilities.toJson
import com.dpoints.view.module.shops.ShopDetailActivity


class ShopAdapter(
    private val listitem: List<Shop>,
    val context: Context,
    val type: Int
) : RecyclerView.Adapter<ShopAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val shop_name: TextView = view.findViewById(R.id.shop_name)
        val shop_detail: TextView = view.findViewById(R.id.shop_details)
        val txtRating: TextView = view.findViewById(R.id.txtRating)
        val txtcoinValue: TextView = view.findViewById(R.id.txtcoinValue)
        val img: ImageView = view.findViewById(R.id.img)
        val bindview = view
        fun bindto(itemtype: Shop, context: Context) {
            Log.e("adapter", itemtype.shop_name)
            shop_name.text = itemtype.shop_name

            shop_detail.text = itemtype.description
            txtRating.text = itemtype.rating
            txtcoinValue.text = "${itemtype.coin_value.toString()} "
            Glide.with(context).load(itemtype.profile_picture).placeholder(R.drawable.error)
                .into(img)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view: View
        if (type == 0) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_shop, parent, false)
        } else {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_shop_search, parent, false)
        }

        return ViewHolder(view)
    }


    override fun getItemCount() = listitem.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindto(listitem[position], context)
        holder.bindview.setOnClickListener {
            Log.e("SHOPPER22", listitem[position].toJson())
            val intent = Intent(context, ShopDetailActivity::class.java)
            var model = listitem.get(position)
            if (model.website == null)
                model.website = ""
            if (model.twitter == null)
                model.twitter = ""
            if (model.facebook == null)
                model.facebook = ""
            if (model.instagram == null)
                model.instagram = ""
            intent.putExtra("SHOP", model)
            context!!.startActivity(intent)
        }

    }
}