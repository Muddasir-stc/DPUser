package com.dpoint.dpointsuser.view.offers

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dpoint.dpointsuser.R
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.datasource.remote.offer.Data
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoint.dpointsuser.utilities.OnItemClickListener
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.view.commons.base.BaseFragment
import com.dpoints.dpointsmerchant.view.module.shops.ShopViewModel
import com.dpoints.view.adapter.OfferAdapter

class OfferFragment : BaseFragment(), OnItemClickListener {
    override val layout: Int = R.layout.fragment_offer
    private val viewModel by lazy { getVM<ShopViewModel>(activity!!) }
    lateinit var list: List<Data>
    lateinit var recyclerView_offers: RecyclerView

    override fun init(view: View) {
        activity?.title = "Offers"

        viewModel.getAllMerchantOffers(UserPreferences.instance.getTokken(context!!)!!)
        recyclerView_offers = view.findViewById<RecyclerView>(R.id.recyclerView_offers)
        recyclerView_offers.layoutManager = LinearLayoutManager(context!!)
        recyclerView_offers.setHasFixedSize(true)
        addObserver()
    }

    private fun addObserver() {
        viewModel.allMerchantOfferState.observe(activity!!, Observer {
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
                    list = state.data!!.data
                    setupShops(list)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }

    lateinit var adapter: OfferAdapter
    private fun setupShops(data: List<Data>?) {
        adapter = OfferAdapter(data!!, this, context!!)
        recyclerView_offers.adapter = adapter
    }

    override fun onItemClick(index: Int, adapter: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
