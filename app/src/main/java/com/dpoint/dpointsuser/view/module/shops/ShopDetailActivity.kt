package com.dpoints.view.module.shops

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.bumptech.glide.Glide
import com.dpoint.dpointsuser.R
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.datasource.remote.gift.GiftModel
import com.dpoints.dpointsmerchant.datasource.remote.offer.Data
import com.dpoints.dpointsmerchant.datasource.remote.offer.OfferModel
import com.dpoints.dpointsmerchant.datasource.remote.shop.Shop
import com.dpoints.dpointsmerchant.datasource.remote.shop.ShopModel
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.OnItemClickListener
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity
import com.dpoints.dpointsmerchant.view.module.shops.ShopViewModel
import com.dpoints.view.adapter.GiftAdapter
import com.dpoints.view.adapter.OfferAdapter
import kotlinx.android.synthetic.main.activity_shop_detail.*

class ShopDetailActivity : BaseActivity(),OnItemClickListener {


    private var giftData: List<com.dpoints.dpointsmerchant.datasource.remote.gift.Data>?=null
    private var data: List<Data>?=null
    override val layout: Int= R.layout.activity_shop_detail
    private val viewModel by lazy { getVM<ShopViewModel>(this) }
lateinit var shop:Shop
lateinit var offers:RecyclerView
lateinit var gifts:RecyclerView


    override fun init() {
        if(intent.getParcelableExtra<Shop>("SHOP")!=null){
            shop=intent.getParcelableExtra<Shop>("SHOP")
            Log.e("Shop",shop.title)
        }
        titleTag.setText(shop.title)
        txtTitle.text=shop.title
        txtDesc.text=shop.description
        txtPhone.text=shop.contact
        txtEmail.text=shop.email
        txtAddress.text=shop.address
        Glide.with(this).load(shop.image).into(banner)
        Glide.with(this).load(shop.image).into(img)
        offers=findViewById(R.id.offers)
        gifts=findViewById(R.id.gifts)
        offers.setHasFixedSize(true)
        gifts.setHasFixedSize(true)
        val layoutManager=LinearLayoutManager(this,HORIZONTAL,false)
        val layoutManager2=LinearLayoutManager(this,HORIZONTAL,false)
        offers.layoutManager=layoutManager
        gifts.layoutManager=layoutManager2
        viewModel.getShopOffers(UserPreferences.instance.getTokken(this)!!,shop.id.toString())
        viewModel.getShopGifts(UserPreferences.instance.getTokken(this)!!,shop.id.toString())
        addObserver()
        val btnBack=findViewById<ImageView>(R.id.backBtn)
        btnBack.setOnClickListener {
            onBackPressed()
        }

    }

    private fun addObserver() {
        viewModel.offersState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer //showProgress(this)
            }
            //hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA", state.data?.message.toString())
                    data=state?.data?.data!!
                    setupRecyclerView(state?.data)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
        viewModel.giftsState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer// showProgress(this)
            }
           // hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA", state.data?.message.toString())
                    giftData=state?.data?.data!!
                    setupRecyclerViewGifts(state?.data)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }

    private fun setupRecyclerViewGifts(data: GiftModel) {
        gifts.adapter=GiftAdapter(this,this,data.data!!)
    }

    private fun setupRecyclerView(data: OfferModel) {
        offers.adapter=OfferAdapter(data.data,this,this)
    }
    override fun onItemClick(index: Int, adapter: Int) {
        if(adapter==1){

        }else if(adapter==2){

        }
    }
}
