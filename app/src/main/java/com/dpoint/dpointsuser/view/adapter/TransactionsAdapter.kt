package com.dpoints.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val bindview = view
        fun bindto(itemtype: Tran) {

          offername.text = "Bindaas Offer"
            txtType.text = "Redeem"
           offerdetail.text = "$200 for 20 DPoints"
            transactionsid.text = "34567890"
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
    }
}