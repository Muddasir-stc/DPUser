package com.dpoints.view.module.gifts

import android.content.Intent
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.view.module.gifts.GiftCardsViewModel
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.datasource.remote.gift.GiftModel
import com.dpoints.dpointsmerchant.datasource.remote.offer.OfferModel
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.OnRemoveClickListener
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity
import com.dpoints.view.adapter.GiftAdapter
import kotlinx.android.synthetic.main.activity_gifts.*
import kotlinx.android.synthetic.main.activity_offers.recyclerView

class Gifts : BaseActivity() {
    override val layout: Int = R.layout.activity_gifts
    private val viewModel by lazy { getVM<GiftCardsViewModel>(this) }
    private var giftModel: GiftModel? = null
    override fun init() {
        viewModel.getGiftCards(UserPreferences.instance.getTokken(this)!!)
        if(UserPreferences.instance.getUser(this)!!.points_used != null){
            pointsUser.text = UserPreferences.instance.getUser(this)!!.points_used
        }else {
            pointsUser.text = "0"
        }
        var backBtn=findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }
        addObserver()
    }


    private fun addObserver(){
        viewModel.giftCardState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer
            }
//             hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA",state.data?.message.toString())
                    giftModel = state?.data
                    setupRecyclerView(state?.data)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })

    }

    override fun onResume() {
        super.onResume()
        viewModel.getGiftCards(
            UserPreferences.instance.getTokken(this)!!
        )
    }

    private fun setupRecyclerView(giftModel: GiftModel?) {
        var linearlayoutmanger = GridLayoutManager(this, 2)
        linearlayoutmanger.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearlayoutmanger
        recyclerView.isNestedScrollingEnabled = true
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = GiftAdapter(this, giftModel!!.data!!)
    }

}
