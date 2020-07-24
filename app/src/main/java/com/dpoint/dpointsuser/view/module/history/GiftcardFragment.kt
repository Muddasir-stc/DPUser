package com.dpoints.view.module.dashboard

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.history.HistoryGiftData
import com.dpoint.dpointsuser.view.module.history.HistoryViewModel
import com.dpoint.dpointsuser.datasource.remote.NetworkState
import com.dpoint.dpointsuser.preferences.UserPreferences
import com.dpoint.dpointsuser.utilities.fromJson
import com.dpoint.dpointsuser.utilities.getVM
import com.dpoint.dpointsuser.utilities.toJson
import com.dpoint.dpointsuser.view.commons.base.BaseFragment
import com.dpoints.view.adapter.RedeemAdapter


class GiftcardFragment : BaseFragment(){
    override val layout: Int=R.layout.fragment_giftcard_history
    lateinit var redeemList:RecyclerView
    private val viewModel by lazy { getVM<HistoryViewModel>(activity!!) }
    override fun init(view: View) {
        redeemList=view.findViewById(R.id.redeemList)
        redeemList.setHasFixedSize(true)
        redeemList.layoutManager= LinearLayoutManager(context)

        viewModel.getAllUserUsedGiftCards(UserPreferences.instance.getTokken(context!!)!!)
        addObserver()
    }
    private fun addObserver() {
        viewModel.historyGiftState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer
            }
            //hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA", state.data?.data.toJson())
               //     loadData(state.data?.data)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }

    private fun loadData(data: List<HistoryGiftData>?) {
      //  redeemList.adapter= RedeemAdapter(data!!,context!!)
    }

}