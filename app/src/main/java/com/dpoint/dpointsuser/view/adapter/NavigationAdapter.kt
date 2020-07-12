package com.dpoint.dpointsuser.view.adapter

import android.annotation.SuppressLint
import android.content.Context
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
import com.dpoint.dpointsuser.datasource.model.Item
import com.dpoint.dpointsuser.utilities.OnItemClickListener

class NavigationAdapter(
    private val items:List<Item>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<NavigationAdapter.ViewHolder>() {
    private var context: Context?=null
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        context = p0.context
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_drawer,p0,false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(items[position])
        if(position %2 == 1) {
            holder.view.setBackgroundColor(context!!.resources.getColor(R.color.colorPrimary))


        }
        else {
            holder.view.setBackgroundColor(context!!.resources.getColor(R.color.colorPrimaryDark))


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