package com.dpoints.view.module.offers

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dpoint.dpointsuser.R
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.datasource.remote.offer.Data
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.OnItemClickListener
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity
import com.dpoints.dpointsmerchant.view.module.shops.ShopViewModel
import com.dpoints.view.adapter.OfferAdapter
import kotlinx.android.synthetic.main.activity_offers.*

class Offers : BaseActivity(), OnItemClickListener {
    private lateinit var linearLayoutManager: LinearLayoutManager
    override val layout: Int = R.layout.activity_offers
    private val viewModel by lazy { getVM<ShopViewModel>(this) }
    override fun init() {
        backBtn.setOnClickListener { onBackPressed() }
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        viewModel.getAllMerchantOffers(UserPreferences.instance.getTokken(this)!!)
        addObserver()
    }

    private fun addObserver() {
        viewModel.allMerchantOfferState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer
            }
            // hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATTA", state.data?.data?.size.toString())
                    if (state.data!!.data.isNullOrEmpty()) {
                        textView_noDataFound.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE

                    } else {
                        textView_noDataFound.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        setupTransactions(state?.data?.data)
                    }
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }
    lateinit var adapter: OfferAdapter
    private fun setupTransactions(data: List<Data>) {
        adapter = OfferAdapter(data!!, this, this)
        recyclerView.adapter = adapter
    }
    override fun onItemClick(index: Int, adapter: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}