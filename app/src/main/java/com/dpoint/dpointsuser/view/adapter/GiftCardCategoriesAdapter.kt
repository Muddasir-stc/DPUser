package com.dpoint.dpointsuser.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.model.GiftCardCategory
import com.dpoint.dpointsuser.utilities.OnRemoveClickListener
import com.dpoints.dpointsmerchant.utilities.GlideApp


class GiftCardCategoriesAdapter(
    private var context: Context,
    private var listener: OnRemoveClickListener,
    private var data: ArrayList<GiftCardCategory>
) : RecyclerView.Adapter<GiftCardCategoriesAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_gift_category, parent, false)
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
        holder.bindto(model, context, position)
        /*holder.layout.setOnClickListener {
            listener.onRemoveClick(position, model.category!!)
        }*/
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.txt_title)
        val layout: CardView = view.findViewById(R.id.layout)
        val shops: LinearLayout = view.findViewById(R.id.shops)
        var set = HashSet<String>()
        fun bindto(
            itemtype: GiftCardCategory,
            context: Context,
            position: Int
        ) {
            try {
                if (itemtype.list!!.isNotEmpty()) {
                    title.text = itemtype.category
                    for (data in itemtype.list) {
                        if (set.contains(data.shop_id.toString())) {
                            continue
                        } else {
                            set.add(data.shop_id.toString())
                            var params = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            params.setMargins(4, 0, 12, 4)
                            var view =
                                LayoutInflater.from(context).inflate(R.layout.item_shop, null)
                            var image = view.findViewById<ImageView>(R.id.img)
                            var shop_detail = view.findViewById<TextView>(R.id.shop_details)
                            var shop_name = view.findViewById<TextView>(R.id.shop_name)
                            var layout = view.findViewById<LinearLayout>(R.id.linearLayout_rating)
                            GlideApp.with(context).load(data.shop_profile_picture)
                                .error(R.drawable.shop_place).placeholder(R.drawable.shop_place)
                                .into(image)
                            shop_name.text = data.shop_name
                            shop_detail.visibility = View.GONE
                            layout.visibility = View.GONE

                            view.layoutParams = params
                            view.setOnClickListener {
                                listener.onRemoveClick(
                                    position,
                                    itemtype.category!!,
                                    data.shop_name
                                )
                            }
                            shops.addView(view)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}