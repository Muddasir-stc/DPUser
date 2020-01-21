package com.dpoints.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dpoint.dpointsuser.R
import com.dpoints.dpointsmerchant.datasource.remote.transaction.Tran
import com.dpoints.dpointsmerchant.utilities.OnItemClickListener

class TransactionsAdapter(
    private val listitem: List<Tran>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<TransactionsAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val offername: TextView = view.findViewById(R.id.textView5)
        val offerdetail: TextView = view.findViewById(R.id.text)
        val transactionsid: TextView = view.findViewById(R.id.textView4)
        val points: TextView = view.findViewById(R.id.textView6)
        val txtType: TextView = view.findViewById(R.id.txtType)
        val typeIcon: ImageView = view.findViewById(R.id.typeIcon)
        val bindview = view
        fun bindto(itemtype: Tran) {

          offername.text = itemtype.transaction_title
            txtType.text = itemtype.type
           offerdetail.text = "$${itemtype.offer_amount} for ${itemtype.coins} DPoints"
            transactionsid.text = "txn-zypg-${itemtype.id}-trans"
            points.text = itemtype.coins
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionsAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_transactions, parent, false)
        return TransactionsAdapter.ViewHolder(view)
    }

    override fun getItemCount() = listitem.size

    override fun onBindViewHolder(holder: TransactionsAdapter.ViewHolder, position: Int) {
        holder.bindto(listitem[position])
        holder.bindview.setOnClickListener {
            listener.onItemClick(position, 1)
        }
        if(listitem[position].type.trim().equals("Redeem")){
            holder.typeIcon.setImageResource(R.drawable.redeem)
        }else{
            holder.typeIcon.setImageResource(R.drawable.assign)
        }
    }
}