package com.dpoints.view.module.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.NetworkState
import com.dpoint.dpointsuser.datasource.remote.order.Order
import com.dpoint.dpointsuser.preferences.UserPreferences
import com.dpoint.dpointsuser.utilities.OnItemClickListener
import com.dpoint.dpointsuser.utilities.getVM
import com.dpoint.dpointsuser.view.commons.base.BaseActivity
import com.dpoint.dpointsuser.view.module.order.OrderViewModel
import com.dpoints.view.adapter.OrdersAdapter
import kotlinx.android.synthetic.main.activity_order.*

class Order : BaseActivity(), OnItemClickListener {
    private lateinit var linearLayoutManager: LinearLayoutManager
    override val layout: Int= R.layout.activity_order
    lateinit var  recyclerView:RecyclerView
    private val viewModel by lazy { getVM<OrderViewModel>(this) }
    override fun init() {

      recyclerView = findViewById<RecyclerView>(R.id.order_recyclerview)

        backBtn.setOnClickListener {
            onBackPressed()
        }
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager


      viewModel.getOrders(UserPreferences.instance.getTokken(this)!!)

        addObserver()

    }

    private fun addObserver() {
        viewModel.orderState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer showProgress(this)
            }
             hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA",state.data?.message)
                    setupOrders(state?.data?.data)
                }
//                is NetworkState.Error -> onError(state.message)
//                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
//                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }

    private fun setupOrders(orders: List<Order>?) {
        val adapter = OrdersAdapter(orders!!,this)
        recyclerView.adapter = adapter
    }

    override fun onItemClick(index: Int, adapter: Int) {
      //  Toast.makeText(this,"ftbhn",Toast.LENGTH_SHORT).show()
    }
}
