package com.dpoints.view.module.shops

import android.content.Intent
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.shop.Shop
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.OnItemClickListener
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity
import com.dpoints.dpointsmerchant.view.module.shops.ShopViewModel
import com.dpoints.view.adapter.ShopAdapter

class Shops : BaseActivity(), OnItemClickListener {

    lateinit var adapter:ShopAdapter


    override val layout: Int= R.layout.activity_shops
    lateinit var recyclerView:RecyclerView
    lateinit var list:List<Shop>
    private val viewModel by lazy { getVM<ShopViewModel>(this) }
    override fun init() {
        recyclerView = findViewById<RecyclerView>(R.id.shop_recyclerview)
        val gridManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = gridManager as RecyclerView.LayoutManager?
        val btnBack=findViewById<ImageView>(R.id.backBtn)
        btnBack.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onResume() {
        super.onResume()
        loadData()
    }
    private fun loadData() {
        viewModel.getShops(UserPreferences.instance.getTokken(this)!!)

        addObserver()
    }

    override fun onItemClick(index: Int, adapter: Int) {
       // Toast.makeText(this,,Toast.LENGTH_SHORT).show()
        Log.e("Shop",list.get(index).shop_name)
        val intent= Intent(this,ShopDetailActivity::class.java)
        intent.putExtra("SHOP",list.get(index))
        startActivity(intent)

    }

    private fun addObserver() {
        viewModel.shopsState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer showProgress(this)
            }
             hideProgress()
            Log.e("DATA",state.toString())
            when (state) {
                is NetworkState.Success -> {
                   Log.e("DATA",state.data?.message)
                    list=state?.data?.data!!
                    setupShops(state?.data?.data)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }
    private fun setupShops(data: List<Shop>?) {
        adapter = ShopAdapter(data!!,this,this)
        recyclerView.adapter = adapter

    }
}
