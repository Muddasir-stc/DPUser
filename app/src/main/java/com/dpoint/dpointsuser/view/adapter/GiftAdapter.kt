package com.dpoints.view.adapter

import android.content.Context
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.gift.Data
import com.dpoints.dpointsmerchant.utilities.OnItemClickListener


class GiftAdapter(
    private var context: Context,
    private var listener: OnItemClickListener,
    private var data: List<Data>
) : RecyclerView.Adapter<GiftAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.giftcard_item, parent, false)
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
        holder.txtAmount.text = "$ ${model.amount}"
        holder.txtCardNo.text = model.rand_text
        holder.txtUnits.text = "${model.number_of_units } ${model.unit}"
        holder.btnScan.setOnClickListener {
            listener.onItemClick(position,2)
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.cardView)
        val txtUnits: TextView = view.findViewById(R.id.txtUnits)
        val txtCardNo: TextView = view.findViewById(R.id.txtCardNo)
        val txtAmount: TextView = view.findViewById(R.id.txtAmount)
        val btnScan: ImageView = view.findViewById(R.id.btnScan)
    }

}