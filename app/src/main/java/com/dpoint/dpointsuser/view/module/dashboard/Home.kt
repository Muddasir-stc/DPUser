package com.dpoints.view.module.dashboard

import android.Manifest
import android.Manifest.permission_group.CAMERA
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.dpoint.dpointsuser.R
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.datasource.remote.offer.Data
import com.dpoints.dpointsmerchant.datasource.remote.shop.Shop
import com.dpoints.dpointsmerchant.preferences.UserPreferences
import com.dpoints.dpointsmerchant.utilities.OnItemClickListener
import com.dpoints.dpointsmerchant.utilities.getVM
import com.dpoints.dpointsmerchant.utilities.toJson
import com.dpoints.dpointsmerchant.view.commons.base.BaseFragment
import com.dpoints.dpointsmerchant.view.module.dashboard.DashboardViewModel
import com.dpoints.view.adapter.OfferAdapter
import com.dpoints.view.adapter.ShopAdapter
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.fragment_home.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.recyclerview.widget.GridLayoutManager
import com.dpoint.dpointsuser.datasource.model.ScanedOffer
import com.dpoint.dpointsuser.view.module.gifts.GiftCardsViewModel
import com.dpoint.dpointsuser.view.module.offers.OfferViewModel
import com.dpoints.dpointsmerchant.datasource.remote.gift.GiftModel
import com.dpoints.dpointsmerchant.datasource.remote.offer.OfferModel
import com.dpoints.dpointsmerchant.utilities.fromJson
import com.dpoints.view.adapter.GiftAdapter
import kotlinx.android.synthetic.main.activity_offers.*


class Home : BaseFragment(),OnItemClickListener {


    override val layout: Int=R.layout.fragment_home
    lateinit var mShops: RecyclerView
    lateinit var mOffers: RecyclerView
    lateinit var mGifts: RecyclerView
    lateinit var data: List<Data>
    lateinit var adapter: ShopAdapter
    lateinit var offerAdapter: OfferAdapter
    lateinit var giftModel: GiftModel
    private val viewModel by lazy { getVM<DashboardViewModel>(activity!!) }
    private val giftCardsViewModel by lazy { getVM<GiftCardsViewModel>(activity!!) }
    override fun init(view: View) {
        shops.setHasFixedSize(true)
        offers.setHasFixedSize(true)
        gifts.setHasFixedSize(true)
        val layoutManager=LinearLayoutManager(context)
        val layoutManager2=LinearLayoutManager(context)
        val layoutManager3=LinearLayoutManager(context)
        layoutManager.orientation= HORIZONTAL
        layoutManager2.orientation= HORIZONTAL
        layoutManager3.orientation= HORIZONTAL
        mShops=view.findViewById(R.id.shops)
        mOffers=view.findViewById(R.id.offers)
        mGifts=view.findViewById(R.id.gifts)
        mShops.layoutManager=layoutManager
        mOffers.layoutManager=layoutManager2
        mGifts.layoutManager=layoutManager3
        viewModel.getShops(UserPreferences.instance.getTokken(context!!)!!)
        viewModel.getOffers(UserPreferences.instance.getTokken(context!!)!!)
        giftCardsViewModel.getGiftCards(UserPreferences.instance.getTokken(context!!)!!)
        addObserver()
    }

    private fun addObserver() {
        viewModel.shopsState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer
            }
            // hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA",state.data?.message)
                    setUpShops(state.data?.data!!)
                }
            }
        })
        viewModel.offersState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer
            }
                //hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA", state.data?.message.toString())
                    data=state?.data?.data!!
                    setOfferList(data)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
        giftCardsViewModel.giftCardState.observe(this, Observer {
            it ?: return@Observer
            val state = it.getContentIfNotHandled() ?: return@Observer
            if (state is NetworkState.Loading) {
                return@Observer
            }
//             hideProgress()
            when (state) {
                is NetworkState.Success -> {
                    Log.e("DATA",state.data?.message.toString())
                    giftModel = state?.data!!
                    setupRecyclerView(state?.data)
                }
                is NetworkState.Error -> onError(state.message)
                is NetworkState.Failure -> onFailure(getString(R.string.request_error))
                else -> onFailure(getString(R.string.connection_error))
            }
        })
    }

    private fun setOfferList(data: List<Data>) {
        offerAdapter = OfferAdapter(data,this,context!!)
        mOffers.adapter=offerAdapter
    }

    private fun setUpShops(data: List<Shop>) {
        adapter = ShopAdapter(data!!,this,context!!)
        mShops.adapter=adapter
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onItemClick(index: Int, adapter: Int) {
      //  Log.e("OFFER",data[index].toJson().toString())
            //  context?.startActivity(Intent(context,ScanActivity::class.java))

        if (context?.checkSelfPermission(CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions( arrayOf(Manifest.permission.CAMERA), 100);
        }
        val inte = IntentIntegrator(activity)
        inte.initiateScan()
        inte.addExtra("ID", data[index].id.toString())
        inte.setOrientationLocked(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Log.d("RESULT", "Cancelled scan")
            } else {
                Log.d("RESULT", result.contents)
                Log.d("HomeFragment","HomeFragment ${data?.getStringExtra("ID")}")
                val offer=result.contents.toString().fromJson<ScanedOffer>()

            }
        }
    }
    private fun setupRecyclerView(giftModel: GiftModel?) {
        mGifts.adapter = GiftAdapter(context!!, giftModel!!.data!!)
    }
}