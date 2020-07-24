package com.dpoints.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.shop.Menu
import com.dpoint.dpointsuser.datasource.remote.shop.Shop
import com.dpoint.dpointsuser.utilities.OnItemClickListener


class MenuAdapter(
    private var context: Context,
    private var data: List<Menu>
) : RecyclerView.Adapter<MenuAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)

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
        holder.bindto(model,context)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val menu_name:TextView = view.findViewById(R.id.shop_name)
        val menu_detail:TextView = view.findViewById(R.id.shop_details)
        val img:ImageView = view.findViewById(R.id.img)
        val bindview=view
        fun bindto(itemtype: Menu, context: Context){
            menu_name.text= itemtype.title
            menu_detail.text = itemtype.description
            Glide.with(context).load(itemtype.image).placeholder(R.drawable.error).into(img)
        }
    }

}