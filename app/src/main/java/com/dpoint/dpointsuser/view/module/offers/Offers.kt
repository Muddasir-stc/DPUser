package com.dpoints.view.module.offers

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.nfc.NfcAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.view.module.offers.OfferViewModel
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.datasource.remote.offer.Data
import com.dpoints.dpointsmerchant.datasource.remote.offer.OfferModel
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.GetDialogInterface
import com.dpoints.dpointsmerchant.utilities.OnRemoveClickListener
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity
import kotlinx.android.synthetic.main.activity_offers.*

class Offers : BaseActivity(), GetDialogInterface {

    private lateinit var banners: ArrayList<Int>
    override val layout: Int = R.layout.activity_offers
    private var offerModel: OfferModel? = null
    private val viewModel by lazy { getVM<OfferViewModel>(this) }
    override fun init() {
        banners = ArrayList()
        banners.add(R.drawable.group_3)
        banners.add(R.drawable.group_3)
        banners.add(R.drawable.group_3)
        banners.add(R.drawable.group_3)
        setupViewPager()
        backBtn.setOnClickListener {
            onBackPressed()
        }
        //addOffer.bringToFront()
        addObserver()

    }

    private fun addObserver() {
        viewModel.offersState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer showProgress(this)
            }
             hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA", state.data?.message.toString())
                    offerModel = state?.data
                    setupRecyclerView(state?.data)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })

    }

    override fun onResume() {
        super.onResume()
        viewModel.getOffers(
            UserPreferences.instance.getTokken(this)!!
        )
    }


    private fun setupRecyclerView(data: OfferModel?) {
        var linearlayoutmanger = LinearLayoutManager(this)
        linearlayoutmanger.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearlayoutmanger
        recyclerView.isNestedScrollingEnabled = true
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = OfferAdapter(this, data!!.data)
    }

    private fun setupViewPager() {

        val pager = findViewById<ViewPager>(R.id.viewPager)
        pager.adapter = CustomViewPagerAdapter(this, banners)
    }


    private class CustomViewPagerAdapter(@param:NonNull @field:NonNull internal val mContext: Context?, @param:NonNull @field:NonNull internal val mObjects: List<Int>) :
        PagerAdapter() {

        override fun getCount(): Int {
            return mObjects.size
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {

            val layout = LayoutInflater.from(mContext).inflate(
                R.layout.item_banner,
                container,
                false
            ) as ViewGroup
            val imageView = layout.findViewById<ImageView>(R.id.img_banner)
            imageView.setImageResource(mObjects[position])
            container.addView(layout)
            return layout

        }
    }

    class OfferAdapter(
        private var context: Context,
        private var data: List<Data>
    ) : RecyclerView.Adapter<OfferAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_offer, parent, false)
            return MyViewHolder(view)
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            var model = data[position]
            holder.textView_offerName.text = model.title
            holder.textView_restrauntName.text = "Shop - " + model.shop_id
            holder.textView_offer.text = model.description
            holder.textView_coins.text = model.offer + " DPoints on Shopping of " + model.amount
            Glide.with(context).load(model.image).placeholder(context.getDrawable(R.drawable.bitmap)).into(holder.imageView)
//            holder.textView_assign.setOnClickListener {
//                var intent = Intent(context, Redeem::class.java)
//                intent.putExtra("data", model)
//                intent.putExtra("type", "offer")
//                context.startActivity(intent)
//            }
//            holder.textView_edit.setOnClickListener {
//                var intent = Intent(context, EditOffer::class.java)
//                intent.putExtra("actionType", "edit")
//                intent.putExtra("data", model)
//                context.startActivity(intent)
//            }
//            holder.textView_delete.setOnClickListener {
//                removeListener.onRemoveClick(position, "h")
//            }

        }

        class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val cardView: CardView = view.findViewById(R.id.cardView)
            val imageView: ImageView = view.findViewById(R.id.imageView)
            val textView_offerName: TextView = view.findViewById(R.id.textView_offerName)
            val textView_restrauntName: TextView = view.findViewById(R.id.textView_restrauntName)
            val textView_offer: TextView = view.findViewById(R.id.textView_offer)
            val textView_coins: TextView = view.findViewById(R.id.textView_coins)
            val textView_assign: TextView = view.findViewById(R.id.textView_assign)
        }
    }

    override fun getDialogInterface(dialog: DialogInterface) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

//    override fun onRemoveClick(position: Int, itemId: String) {
//        var builder = AlertDialog.Builder(this)
//        builder
//            .setMessage("Do you want to delete this offer ?")
//            .setCancelable(false)
//            .setPositiveButton("Yes") { dialog, id ->
//                viewModel_editoffer.deleteOffer(
//                    UserPreferences.instance.getTokken(this)!!,
//                    offerModel!!.data[position].id.toString()
//                )
//                dialog.dismiss()
//            }
//            .setNegativeButton(
//                "No"
//            ) { dialog, id ->
//                dialog.dismiss()
//            }
////Creating dialog box
//        val alert = builder.create()
////Setting the title manually
//        alert.setTitle("Warning")
//        alert.show()
//    }

}
