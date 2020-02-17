package com.dpoint.dpointsuser.view.module.history

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.history.Exchange
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.view.commons.base.BaseFragment
import com.dpoints.view.adapter.ExchangeAdapter


class Points : BaseFragment(){
    override val layout: Int= R.layout.fragment_exchange_history
    private val viewModel by lazy { getVM<HistoryViewModel>(activity!!) }

    lateinit var exchangeList: RecyclerView
    override fun init(view: View) {
        exchangeList=view.findViewById(R.id.exchangeList)
        exchangeList.setHasFixedSize(true)
        exchangeList.layoutManager= LinearLayoutManager(context)

        viewModel.getPoints(UserPreferences.instance.getTokken(context!!)!!)
        addObserver()
    }

    private fun addObserver() {
        viewModel.pointsState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer
            }
            //hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA", state.data?.message.toString())
                    loadData(state.data?.data)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }

    private fun loadData(data: List<Exchange>?) {
        exchangeList.adapter= ExchangeAdapter(data!!,context!!)
    }

}