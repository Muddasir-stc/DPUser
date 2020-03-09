package com.dpoint.dpointsuser.view.module.membership

import android.widget.ImageView
import androidx.lifecycle.Observer
import com.dpoint.dpointsuser.R
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity
import android.util.Log
import com.dpoint.dpointsuser.datasource.remote.shop.MenuModel

class MemberShipCardActivity : BaseActivity() {
    override val layout: Int
        get() = R.layout.activity_member_ship__cards
    private val viewModel by lazy { getVM<MemberShipCardViewModel>(this) }
    override fun init() {
        var backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }
        viewModel.getMemberShipCard(UserPreferences.instance.getTokken(this)!!)
        addObserver()
    }

    private fun addObserver() {
        viewModel.getMemberShipCardState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer
            }
            //hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA", state.data?.message.toString())
                    setData(state.data)

                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }

    private fun setData(data: MenuModel?) {

    }

}
