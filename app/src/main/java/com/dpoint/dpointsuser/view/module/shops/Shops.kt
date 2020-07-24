package com.dpoints.view.module.shops

import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.shop.Shop
import com.dpoint.dpointsuser.datasource.remote.NetworkState
import com.dpoint.dpointsuser.preferences.UserPreferences
import com.dpoint.dpointsuser.utilities.getVM
import com.dpoint.dpointsuser.view.commons.base.BaseActivity
import com.dpoint.dpointsuser.view.module.shops.ShopViewModel
import com.dpoints.view.adapter.ShopAdapter

class Shops : BaseActivity() {

    lateinit var adapter: ShopAdapter


    override val layout: Int = R.layout.activity_shops
    lateinit var recyclerView: RecyclerView
    lateinit var list: List<Shop>
    private val viewModel by lazy { getVM<ShopViewModel>(this) }
    private var data: String? = null
    override fun init() {
        data = intent.getStringExtra("data");
        recyclerView = findViewById(R.id.shop_recyclerview)

        recyclerView.layoutManager = LinearLayoutManager(this)
        val btnBack = findViewById<ImageView>(R.id.backBtn)
        btnBack.setOnClickListener {
            onBackPressed()
        }
        addObserver()

    }

    override fun onResume() {
        super.onResume()
        if (data.equals("offer", true)) {
            viewModel.getShopsWithOffers(UserPreferences.instance.getTokken(this)!!)
        } else
            loadData()
    }

    private fun loadData() {
        viewModel.getShops(UserPreferences.instance.getTokken(this)!!)
    }


    private fun addObserver() {
        viewModel.shopsState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer showProgress(this)
            }
            hideProgress()
            Log.e("DATA", state.toString())
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA", state.data?.message)
                    list = state?.data?.data!!
                    setupShops(state?.data?.data)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })

        viewModel.shopsWithOfferState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer showProgress(this)
            }
            hideProgress()
            Log.e("DATA", state.toString())
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA", state.data?.message)
                    list = state?.data?.data!!
                    setupShops(state?.data?.data)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }

    private fun setupShops(data: List<Shop>?) {
        adapter = ShopAdapter(data!!, this, 1)
        recyclerView.adapter = adapter
    }
}
