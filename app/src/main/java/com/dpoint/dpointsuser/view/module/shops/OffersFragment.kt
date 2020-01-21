package com.dpoints.view.module.dashboard

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.shop.Shop
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.datasource.remote.offer.OfferModel
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.OnItemClickListener
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.view.commons.base.BaseFragment
import com.dpoints.dpointsmerchant.view.module.shops.ShopViewModel
import com.dpoints.view.adapter.OfferAdapter


class OffersFragment(shop: Shop) : BaseFragment(),OnItemClickListener{

    var shop=shop
    override val layout: Int=R.layout.fragment_offers
    lateinit var offers: RecyclerView
    private val viewModel by lazy { getVM<ShopViewModel>(activity!!) }
    override fun init(view: View) {
        Log.e("SHOPID",shop.id.toString())
        offers=view.findViewById(R.id.shop_offers)
        offers.setHasFixedSize(true)
        offers.layoutManager= LinearLayoutManager(context)
        viewModel.getShopOffers(UserPreferences.instance.getTokken(context!!)!!,shop.id.toString())
        addObserver()
    }

    private fun addObserver() {
        viewModel.offersState.observe(this, Observer {
            it ?: return@Observer
            Log.e("GOTDATA 1", "Hello")

            val state = it.getContentIfNotHandled() ?: return@Observer
            Log.e("GOTDATA 2", "Hello")

            if (state is NetworkState.Loading) {
                Log.e("GOTDATA 3", "Hello")

                return@Observer //showProgress(this)
            }
            //hideProgress()
            when (state) {

                is NetworkState.Success -> {
                    Log.e("GOTDATA", state.data?.message.toString())
                    setupRecyclerView(state.data!!)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }
    private fun setupRecyclerView(data: OfferModel) {
        Log.e("GOTDATA",data.data.size.toString())
        offers.adapter= OfferAdapter(data.data,this,context!!)
    }
    override fun onItemClick(index: Int, adapter: Int) {

    }




}