package com.dpoints.dpointsmerchant.view.module.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dpoint.dpointsuser.datasource.remote.offer.AssignModel
import com.dpoints.dpointsmerchant.datasource.remote.ApiCallbackImpl
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.datasource.remote.offer.OfferModel
import com.dpoints.dpointsmerchant.datasource.remote.offer.OfferService
import com.dpoints.dpointsmerchant.datasource.remote.shop.ShopModel
import com.dpoints.dpointsmerchant.datasource.remote.shop.ShopService
import com.dpoints.dpointsmerchant.utilities.Event


class DashboardViewModel : ViewModel() {

    private val _shopsState = MutableLiveData<Event<NetworkState<ShopModel>>>()
    val shopsState: LiveData<Event<NetworkState<ShopModel>>> get() = _shopsState

    private val _offersState = MutableLiveData<Event<NetworkState<OfferModel>>>()
    val offersState: LiveData<Event<NetworkState<OfferModel>>> get() = _offersState

    private val _assignState = MutableLiveData<Event<NetworkState<AssignModel>>>()
    val assignState: LiveData<Event<NetworkState<AssignModel>>> get() = _assignState


    fun getShops(token: String) {

        _shopsState.value = Event(NetworkState.Loading())

        ShopService.instance.getShops(token,
            object : ApiCallbackImpl<ShopModel>(_shopsState) {
                override fun onSuccess(success: ShopModel?) {
                    Log.e("Data",success?.message)
                    _shopsState.value = Event(NetworkState.Success(success))
                }
            })
    }

    fun assignOffer(token: String,userId:String,merchantId:String,ShopId:String,coinOfferId:String,offerTitle:String,offer:String,ammount:String) {

        _assignState.value = Event(NetworkState.Loading())

        ShopService.instance.assignOffer(token,userId,merchantId,ShopId,coinOfferId,offerTitle,offer,ammount,
            object : ApiCallbackImpl<AssignModel>(_assignState) {
                override fun onSuccess(success: AssignModel?) {
                    Log.e("ASSIGN",success?.message)
                    _assignState.value = Event(NetworkState.Success(success))
                }
            })
    }
    fun getOffers(token: String) {
        _offersState.value = Event(NetworkState.Loading())

        OfferService.instance.getOffers(token,
            object : ApiCallbackImpl<OfferModel>(_offersState) {
                override fun onSuccess(success: OfferModel?) {
                    Log.e("TAG", success?.message.toString())
                    _offersState.value = Event(NetworkState.Success(success))
                }

            })
    }

}