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
import com.dpoint.dpointsuser.datasource.remote.shop.Menu
import com.dpoint.dpointsuser.utilities.OnItemClickListener

class MembershipAdapter (
    private var context: Context,
    private var listener: OnItemClickListener,
    private var data: List<Menu>
): RecyclerView.Adapter<MembershipAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MembershipAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_membership_card, parent, false)
        return ViewHolder(view)    }

    override fun getItemCount(): Int {
return data.size
    }

    override fun onBindViewHolder(holder: MembershipAdapter.ViewHolder, position: Int) {
        var model = data[position]
        holder.bindto(model, context)
        holder.delete.setOnClickListener {
            listener.onItemClick(position, 2)
        }

    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){

        val img_strip: ImageView = view.findViewById(R.id.imageView)
        val membership_title:TextView=view.findViewById(R.id.membership_title)
        val delete:ImageView=view.findViewById(R.id.imageView_delete)
        fun bindto(itemtype: Menu, context: Context) {
            Glide.with(context).load(itemtype.image).placeholder(R.drawable.error).into(img_strip)

            membership_title.text=itemtype.title
        }

    }
}