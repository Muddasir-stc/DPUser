package com.dpoint.dpointsuser.view.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dpoint.dpointsuser.R
import com.dpoints.dpointsmerchant.datasource.model.Item
import com.dpoints.dpointsmerchant.utilities.OnItemClickListener

class NavigationAdapter(
    private val items:List<Item>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<NavigationAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_drawer,p0,false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(items[position])
        if(position %2 == 1) {
            holder.view.setBackgroundColor(Color.parseColor("#12a6a2"))


        }
        else {
            holder.view.setBackgroundColor(Color.parseColor("#0abab5"))


        }
        holder.bindView
        holder.bindView.setOnClickListener {
            listener.onItemClick(position, 1)
        }
    }


    class ViewHolder(view : View): RecyclerView.ViewHolder(view){
        val view: LinearLayout = view.findViewById(R.id.parent_view)
        val tvLabel: TextView = view.findViewById(R.id.tv_label)
        val ivImage: ImageView = view.findViewById(R.id.iv_icon)
        val bindView = view

        fun bindTo(labels: Item)
        {
            tvLabel.text = labels.name
            ivImage.setImageResource(labels.iconRes!!)
        }

    }


}