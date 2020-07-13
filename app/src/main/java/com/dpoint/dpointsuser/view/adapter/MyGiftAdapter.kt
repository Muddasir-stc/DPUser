package com.dpoints.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.userdata.MyGift
import com.dpoint.dpointsuser.utilities.OnItemClickListener
import java.text.SimpleDateFormat
import java.util.*


class MyGiftAdapter(
    private var context: Context,
    private var listener: OnItemClickListener,
    private var data: List<MyGift>
) : RecyclerView.Adapter<MyGiftAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.my_giftcard_item, parent, false)

        return MyViewHolder(view)
    }


    override fun getItemCount(): Int {
        Log.e("NUMBERS", data.size.toString())
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var model = data[position]
        holder.bindto(model, context)
        holder.btnReddem.setOnClickListener {
            listener.onItemClick(position, 1)
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.cardView)
        val txtUnits: TextView = view.findViewById(R.id.txtUnits)
        val txtCardNo: TextView = view.findViewById(R.id.txtCardNo)
        val txtAmount: TextView = view.findViewById(R.id.txtAmount)
        val textView_title: TextView = view.findViewById(R.id.textView_title)
        val txtPurchased: TextView = view.findViewById(R.id.txtPurchased)
        val txtExpired: TextView = view.findViewById(R.id.txtExpired)
        val layout: ImageView = view.findViewById(R.id.img_expired)
        val img_strip: ImageView = view.findViewById(R.id.img_strip)
        val btnReddem: Button = view.findViewById(R.id.btnRedeem)
        fun bindto(itemtype: MyGift, context: Context) {
            var expired_at = itemtype.expired_at.trim()
            try {
                val date1 = SimpleDateFormat("dd/MM/yyyy").parse(expired_at)
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val currentDate = sdf.format(Date())
                val date2 = SimpleDateFormat("dd/MM/yyyy").parse(currentDate)
                if (date1.compareTo(date2) < 0) {
                    layout.visibility = View.VISIBLE
                    img_strip.visibility = View.GONE
                    btnReddem.isEnabled = false
                }
                txtExpired.setText(expired_at)

                txtPurchased.setText(itemtype.created_at.split(" ")[0])
                txtAmount.text = itemtype.amount
                textView_title.text = itemtype.title+" By "+itemtype.shop_name
                txtCardNo.text = itemtype.rand_text
                txtUnits.text = "${itemtype.number_of_units} ${itemtype.unit}"
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}