package com.dpoints.view.module.dashboard

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.model.ScanedOffer
import com.dpoint.dpointsuser.datasource.remote.history.Redeem
import com.dpoint.dpointsuser.view.module.history.HistoryViewModel
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.fromJson
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.utilities.toJson
import com.dpoints.dpointsmerchant.view.commons.base.BaseFragment
import com.dpoints.view.adapter.RedeemAdapter
import kotlinx.android.synthetic.main.content_dashboard.*


class GiftcardFragment : BaseFragment(){
    override val layout: Int=R.layout.fragment_giftcard_history
    lateinit var redeemList:RecyclerView
    private val viewModel by lazy { getVM<HistoryViewModel>(activity!!) }
    override fun init(view: View) {
        redeemList=view.findViewById(R.id.redeemList)
        redeemList.setHasFixedSize(true)
        redeemList.layoutManager= LinearLayoutManager(context)

        viewModel.getRedeems(UserPreferences.instance.getTokken(context!!)!!)
        addObserver()
    }
    private fun addObserver() {
        viewModel.redeemHistoryState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer
            }
            //hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA", state.data?.data.toJson())
                    loadData(state.data?.data)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }

    private fun loadData(data: List<Redeem>?) {
        redeemList.adapter= RedeemAdapter(data!!,context!!)
    }

}