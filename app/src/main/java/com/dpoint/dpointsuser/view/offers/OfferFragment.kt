package com.dpoint.dpointsuser.view.offers

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.shop.Shop
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.view.commons.base.BaseFragment
import com.dpoints.dpointsmerchant.view.module.shops.ShopViewModel
import com.dpoints.view.adapter.ShopAdapter

class OfferFragment : BaseFragment() {
    override val layout: Int = R.layout.fragment_offer
    private val viewModel by lazy { getVM<ShopViewModel>(activity!!) }
    lateinit var list: List<Shop>
    lateinit var recyclerView_offers: RecyclerView

    override fun init(view: View) {
        activity?.title = "Offers"

        viewModel.getShopsWithOffers(UserPreferences.instance.getTokken(context!!)!!)
        recyclerView_offers = view.findViewById<RecyclerView>(R.id.recyclerView_offers)
        recyclerView_offers.layoutManager = LinearLayoutManager(context!!)
        recyclerView_offers.setHasFixedSize(true)
        addObserver()
    }

    private fun addObserver() {
        viewModel.shopsWithOfferState.observe(activity!!, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer showProgress(context!!)
            }
            hideProgress()
            Log.e("DATA", state.toString())
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA", state.data?.message)
                    list = state?.data?.data!!
                    setupShops(list)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }

    lateinit var adapter: ShopAdapter
    private fun setupShops(data: List<Shop>?) {
        adapter = ShopAdapter(data!!, context!!, 1)
        recyclerView_offers.adapter = adapter
    }
}
